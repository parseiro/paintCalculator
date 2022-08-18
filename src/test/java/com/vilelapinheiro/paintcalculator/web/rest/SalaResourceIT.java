package com.vilelapinheiro.paintcalculator.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vilelapinheiro.paintcalculator.IntegrationTest;
import com.vilelapinheiro.paintcalculator.domain.Sala;
import com.vilelapinheiro.paintcalculator.repository.SalaRepository;
import com.vilelapinheiro.paintcalculator.service.dto.SalaDTO;
import com.vilelapinheiro.paintcalculator.service.mapper.SalaMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SalaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/salas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private SalaMapper salaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalaMockMvc;

    private Sala sala;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sala createEntity(EntityManager em) {
        Sala sala = new Sala().nome(DEFAULT_NOME);
        return sala;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sala createUpdatedEntity(EntityManager em) {
        Sala sala = new Sala().nome(UPDATED_NOME);
        return sala;
    }

    @BeforeEach
    public void initTest() {
        sala = createEntity(em);
    }

    @Test
    @Transactional
    void createSala() throws Exception {
        int databaseSizeBeforeCreate = salaRepository.findAll().size();
        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);
        restSalaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaDTO)))
            .andExpect(status().isCreated());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeCreate + 1);
        Sala testSala = salaList.get(salaList.size() - 1);
        assertThat(testSala.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void createSalaWithExistingId() throws Exception {
        // Create the Sala with an existing ID
        sala.setId(1L);
        SalaDTO salaDTO = salaMapper.toDto(sala);

        int databaseSizeBeforeCreate = salaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaRepository.findAll().size();
        // set the field null
        sala.setNome(null);

        // Create the Sala, which fails.
        SalaDTO salaDTO = salaMapper.toDto(sala);

        restSalaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaDTO)))
            .andExpect(status().isBadRequest());

        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSalas() throws Exception {
        // Initialize the database
        salaRepository.saveAndFlush(sala);

        // Get all the salaList
        restSalaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sala.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getSala() throws Exception {
        // Initialize the database
        salaRepository.saveAndFlush(sala);

        // Get the sala
        restSalaMockMvc
            .perform(get(ENTITY_API_URL_ID, sala.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sala.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingSala() throws Exception {
        // Get the sala
        restSalaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSala() throws Exception {
        // Initialize the database
        salaRepository.saveAndFlush(sala);

        int databaseSizeBeforeUpdate = salaRepository.findAll().size();

        // Update the sala
        Sala updatedSala = salaRepository.findById(sala.getId()).get();
        // Disconnect from session so that the updates on updatedSala are not directly saved in db
        em.detach(updatedSala);
        updatedSala.nome(UPDATED_NOME);
        SalaDTO salaDTO = salaMapper.toDto(updatedSala);

        restSalaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
        Sala testSala = salaList.get(salaList.size() - 1);
        assertThat(testSala.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void putNonExistingSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().size();
        sala.setId(count.incrementAndGet());

        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().size();
        sala.setId(count.incrementAndGet());

        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().size();
        sala.setId(count.incrementAndGet());

        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalaWithPatch() throws Exception {
        // Initialize the database
        salaRepository.saveAndFlush(sala);

        int databaseSizeBeforeUpdate = salaRepository.findAll().size();

        // Update the sala using partial update
        Sala partialUpdatedSala = new Sala();
        partialUpdatedSala.setId(sala.getId());

        restSalaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSala.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSala))
            )
            .andExpect(status().isOk());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
        Sala testSala = salaList.get(salaList.size() - 1);
        assertThat(testSala.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void fullUpdateSalaWithPatch() throws Exception {
        // Initialize the database
        salaRepository.saveAndFlush(sala);

        int databaseSizeBeforeUpdate = salaRepository.findAll().size();

        // Update the sala using partial update
        Sala partialUpdatedSala = new Sala();
        partialUpdatedSala.setId(sala.getId());

        partialUpdatedSala.nome(UPDATED_NOME);

        restSalaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSala.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSala))
            )
            .andExpect(status().isOk());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
        Sala testSala = salaList.get(salaList.size() - 1);
        assertThat(testSala.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void patchNonExistingSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().size();
        sala.setId(count.incrementAndGet());

        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().size();
        sala.setId(count.incrementAndGet());

        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().size();
        sala.setId(count.incrementAndGet());

        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(salaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSala() throws Exception {
        // Initialize the database
        salaRepository.saveAndFlush(sala);

        int databaseSizeBeforeDelete = salaRepository.findAll().size();

        // Delete the sala
        restSalaMockMvc
            .perform(delete(ENTITY_API_URL_ID, sala.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sala> salaList = salaRepository.findAll();
        assertThat(salaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
