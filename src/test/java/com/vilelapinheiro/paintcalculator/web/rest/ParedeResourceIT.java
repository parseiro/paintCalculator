package com.vilelapinheiro.paintcalculator.web.rest;

import static com.vilelapinheiro.paintcalculator.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vilelapinheiro.paintcalculator.IntegrationTest;
import com.vilelapinheiro.paintcalculator.domain.Parede;
import com.vilelapinheiro.paintcalculator.repository.ParedeRepository;
import com.vilelapinheiro.paintcalculator.service.ParedeService;
import com.vilelapinheiro.paintcalculator.service.dto.ParedeDTO;
import com.vilelapinheiro.paintcalculator.service.mapper.ParedeMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ParedeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ParedeResourceIT {

    private static final BigDecimal DEFAULT_LARGURA = new BigDecimal(0);
    private static final BigDecimal UPDATED_LARGURA = new BigDecimal(1);

    private static final BigDecimal DEFAULT_ALTURA = new BigDecimal(0);
    private static final BigDecimal UPDATED_ALTURA = new BigDecimal(1);

    private static final Integer DEFAULT_NUM_PORTAS = 0;
    private static final Integer UPDATED_NUM_PORTAS = 1;

    private static final Integer DEFAULT_NUM_JANELAS = 0;
    private static final Integer UPDATED_NUM_JANELAS = 1;

    private static final String ENTITY_API_URL = "/api/paredes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParedeRepository paredeRepository;

    @Mock
    private ParedeRepository paredeRepositoryMock;

    @Autowired
    private ParedeMapper paredeMapper;

    @Mock
    private ParedeService paredeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParedeMockMvc;

    private Parede parede;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parede createEntity(EntityManager em) {
        Parede parede = new Parede()
            .largura(DEFAULT_LARGURA)
            .altura(DEFAULT_ALTURA)
            .numPortas(DEFAULT_NUM_PORTAS)
            .numJanelas(DEFAULT_NUM_JANELAS);
        return parede;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parede createUpdatedEntity(EntityManager em) {
        Parede parede = new Parede()
            .largura(UPDATED_LARGURA)
            .altura(UPDATED_ALTURA)
            .numPortas(UPDATED_NUM_PORTAS)
            .numJanelas(UPDATED_NUM_JANELAS);
        return parede;
    }

    @BeforeEach
    public void initTest() {
        parede = createEntity(em);
    }

    @Test
    @Transactional
    void createParede() throws Exception {
        int databaseSizeBeforeCreate = paredeRepository.findAll().size();
        // Create the Parede
        ParedeDTO paredeDTO = paredeMapper.toDto(parede);
        restParedeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paredeDTO)))
            .andExpect(status().isCreated());

        // Validate the Parede in the database
        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeCreate + 1);
        Parede testParede = paredeList.get(paredeList.size() - 1);
        assertThat(testParede.getLargura()).isEqualByComparingTo(DEFAULT_LARGURA);
        assertThat(testParede.getAltura()).isEqualByComparingTo(DEFAULT_ALTURA);
        assertThat(testParede.getNumPortas()).isEqualTo(DEFAULT_NUM_PORTAS);
        assertThat(testParede.getNumJanelas()).isEqualTo(DEFAULT_NUM_JANELAS);
    }

    @Test
    @Transactional
    void createParedeWithExistingId() throws Exception {
        // Create the Parede with an existing ID
        parede.setId(1L);
        ParedeDTO paredeDTO = paredeMapper.toDto(parede);

        int databaseSizeBeforeCreate = paredeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParedeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paredeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Parede in the database
        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLarguraIsRequired() throws Exception {
        int databaseSizeBeforeTest = paredeRepository.findAll().size();
        // set the field null
        parede.setLargura(null);

        // Create the Parede, which fails.
        ParedeDTO paredeDTO = paredeMapper.toDto(parede);

        restParedeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paredeDTO)))
            .andExpect(status().isBadRequest());

        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = paredeRepository.findAll().size();
        // set the field null
        parede.setAltura(null);

        // Create the Parede, which fails.
        ParedeDTO paredeDTO = paredeMapper.toDto(parede);

        restParedeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paredeDTO)))
            .andExpect(status().isBadRequest());

        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParedes() throws Exception {
        // Initialize the database
        paredeRepository.saveAndFlush(parede);

        // Get all the paredeList
        restParedeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parede.getId().intValue())))
            .andExpect(jsonPath("$.[*].largura").value(hasItem(sameNumber(DEFAULT_LARGURA))))
            .andExpect(jsonPath("$.[*].altura").value(hasItem(sameNumber(DEFAULT_ALTURA))))
            .andExpect(jsonPath("$.[*].numPortas").value(hasItem(DEFAULT_NUM_PORTAS)))
            .andExpect(jsonPath("$.[*].numJanelas").value(hasItem(DEFAULT_NUM_JANELAS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllParedesWithEagerRelationshipsIsEnabled() throws Exception {
        when(paredeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restParedeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paredeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllParedesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paredeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restParedeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(paredeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getParede() throws Exception {
        // Initialize the database
        paredeRepository.saveAndFlush(parede);

        // Get the parede
        restParedeMockMvc
            .perform(get(ENTITY_API_URL_ID, parede.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parede.getId().intValue()))
            .andExpect(jsonPath("$.largura").value(sameNumber(DEFAULT_LARGURA)))
            .andExpect(jsonPath("$.altura").value(sameNumber(DEFAULT_ALTURA)))
            .andExpect(jsonPath("$.numPortas").value(DEFAULT_NUM_PORTAS))
            .andExpect(jsonPath("$.numJanelas").value(DEFAULT_NUM_JANELAS));
    }

    @Test
    @Transactional
    void getNonExistingParede() throws Exception {
        // Get the parede
        restParedeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParede() throws Exception {
        // Initialize the database
        paredeRepository.saveAndFlush(parede);

        int databaseSizeBeforeUpdate = paredeRepository.findAll().size();

        // Update the parede
        Parede updatedParede = paredeRepository.findById(parede.getId()).get();
        // Disconnect from session so that the updates on updatedParede are not directly saved in db
        em.detach(updatedParede);
        updatedParede.largura(UPDATED_LARGURA).altura(UPDATED_ALTURA).numPortas(UPDATED_NUM_PORTAS).numJanelas(UPDATED_NUM_JANELAS);
        ParedeDTO paredeDTO = paredeMapper.toDto(updatedParede);

        restParedeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paredeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paredeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Parede in the database
        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeUpdate);
        Parede testParede = paredeList.get(paredeList.size() - 1);
        assertThat(testParede.getLargura()).isEqualByComparingTo(UPDATED_LARGURA);
        assertThat(testParede.getAltura()).isEqualByComparingTo(UPDATED_ALTURA);
        assertThat(testParede.getNumPortas()).isEqualTo(UPDATED_NUM_PORTAS);
        assertThat(testParede.getNumJanelas()).isEqualTo(UPDATED_NUM_JANELAS);
    }

    @Test
    @Transactional
    void putNonExistingParede() throws Exception {
        int databaseSizeBeforeUpdate = paredeRepository.findAll().size();
        parede.setId(count.incrementAndGet());

        // Create the Parede
        ParedeDTO paredeDTO = paredeMapper.toDto(parede);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParedeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paredeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paredeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parede in the database
        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParede() throws Exception {
        int databaseSizeBeforeUpdate = paredeRepository.findAll().size();
        parede.setId(count.incrementAndGet());

        // Create the Parede
        ParedeDTO paredeDTO = paredeMapper.toDto(parede);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParedeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paredeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parede in the database
        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParede() throws Exception {
        int databaseSizeBeforeUpdate = paredeRepository.findAll().size();
        parede.setId(count.incrementAndGet());

        // Create the Parede
        ParedeDTO paredeDTO = paredeMapper.toDto(parede);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParedeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paredeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parede in the database
        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParedeWithPatch() throws Exception {
        // Initialize the database
        paredeRepository.saveAndFlush(parede);

        int databaseSizeBeforeUpdate = paredeRepository.findAll().size();

        // Update the parede using partial update
        Parede partialUpdatedParede = new Parede();
        partialUpdatedParede.setId(parede.getId());

        partialUpdatedParede.altura(UPDATED_ALTURA);

        restParedeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParede.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParede))
            )
            .andExpect(status().isOk());

        // Validate the Parede in the database
        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeUpdate);
        Parede testParede = paredeList.get(paredeList.size() - 1);
        assertThat(testParede.getLargura()).isEqualByComparingTo(DEFAULT_LARGURA);
        assertThat(testParede.getAltura()).isEqualByComparingTo(UPDATED_ALTURA);
        assertThat(testParede.getNumPortas()).isEqualTo(DEFAULT_NUM_PORTAS);
        assertThat(testParede.getNumJanelas()).isEqualTo(DEFAULT_NUM_JANELAS);
    }

    @Test
    @Transactional
    void fullUpdateParedeWithPatch() throws Exception {
        // Initialize the database
        paredeRepository.saveAndFlush(parede);

        int databaseSizeBeforeUpdate = paredeRepository.findAll().size();

        // Update the parede using partial update
        Parede partialUpdatedParede = new Parede();
        partialUpdatedParede.setId(parede.getId());

        partialUpdatedParede.largura(UPDATED_LARGURA).altura(UPDATED_ALTURA).numPortas(UPDATED_NUM_PORTAS).numJanelas(UPDATED_NUM_JANELAS);

        restParedeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParede.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParede))
            )
            .andExpect(status().isOk());

        // Validate the Parede in the database
        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeUpdate);
        Parede testParede = paredeList.get(paredeList.size() - 1);
        assertThat(testParede.getLargura()).isEqualByComparingTo(UPDATED_LARGURA);
        assertThat(testParede.getAltura()).isEqualByComparingTo(UPDATED_ALTURA);
        assertThat(testParede.getNumPortas()).isEqualTo(UPDATED_NUM_PORTAS);
        assertThat(testParede.getNumJanelas()).isEqualTo(UPDATED_NUM_JANELAS);
    }

    @Test
    @Transactional
    void patchNonExistingParede() throws Exception {
        int databaseSizeBeforeUpdate = paredeRepository.findAll().size();
        parede.setId(count.incrementAndGet());

        // Create the Parede
        ParedeDTO paredeDTO = paredeMapper.toDto(parede);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParedeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paredeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paredeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parede in the database
        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParede() throws Exception {
        int databaseSizeBeforeUpdate = paredeRepository.findAll().size();
        parede.setId(count.incrementAndGet());

        // Create the Parede
        ParedeDTO paredeDTO = paredeMapper.toDto(parede);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParedeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paredeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parede in the database
        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParede() throws Exception {
        int databaseSizeBeforeUpdate = paredeRepository.findAll().size();
        parede.setId(count.incrementAndGet());

        // Create the Parede
        ParedeDTO paredeDTO = paredeMapper.toDto(parede);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParedeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paredeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parede in the database
        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParede() throws Exception {
        // Initialize the database
        paredeRepository.saveAndFlush(parede);

        int databaseSizeBeforeDelete = paredeRepository.findAll().size();

        // Delete the parede
        restParedeMockMvc
            .perform(delete(ENTITY_API_URL_ID, parede.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parede> paredeList = paredeRepository.findAll();
        assertThat(paredeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
