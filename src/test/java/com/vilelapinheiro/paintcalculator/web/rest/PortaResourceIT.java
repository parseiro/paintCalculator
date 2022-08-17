package com.vilelapinheiro.paintcalculator.web.rest;

import static com.vilelapinheiro.paintcalculator.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vilelapinheiro.paintcalculator.IntegrationTest;
import com.vilelapinheiro.paintcalculator.domain.Parede;
import com.vilelapinheiro.paintcalculator.domain.Porta;
import com.vilelapinheiro.paintcalculator.repository.PortaRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PortaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PortaResourceIT {

    private static final BigDecimal DEFAULT_LARGURA = new BigDecimal(1);
    private static final BigDecimal UPDATED_LARGURA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ALTURA = new BigDecimal(1);
    private static final BigDecimal UPDATED_ALTURA = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/portas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PortaRepository portaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPortaMockMvc;

    private Porta porta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Porta createEntity(EntityManager em) {
        Porta porta = new Porta().largura(DEFAULT_LARGURA).altura(DEFAULT_ALTURA);
        // Add required entity
        Parede parede;
        if (TestUtil.findAll(em, Parede.class).isEmpty()) {
            parede = ParedeResourceIT.createEntity(em);
            em.persist(parede);
            em.flush();
        } else {
            parede = TestUtil.findAll(em, Parede.class).get(0);
        }
        porta.setParede(parede);
        return porta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Porta createUpdatedEntity(EntityManager em) {
        Porta porta = new Porta().largura(UPDATED_LARGURA).altura(UPDATED_ALTURA);
        // Add required entity
        Parede parede;
        if (TestUtil.findAll(em, Parede.class).isEmpty()) {
            parede = ParedeResourceIT.createUpdatedEntity(em);
            em.persist(parede);
            em.flush();
        } else {
            parede = TestUtil.findAll(em, Parede.class).get(0);
        }
        porta.setParede(parede);
        return porta;
    }

    @BeforeEach
    public void initTest() {
        porta = createEntity(em);
    }

    @Test
    @Transactional
    void createPorta() throws Exception {
        int databaseSizeBeforeCreate = portaRepository.findAll().size();
        // Create the Porta
        restPortaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(porta)))
            .andExpect(status().isCreated());

        // Validate the Porta in the database
        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeCreate + 1);
        Porta testPorta = portaList.get(portaList.size() - 1);
        assertThat(testPorta.getLargura()).isEqualByComparingTo(DEFAULT_LARGURA);
        assertThat(testPorta.getAltura()).isEqualByComparingTo(DEFAULT_ALTURA);
    }

    @Test
    @Transactional
    void createPortaWithExistingId() throws Exception {
        // Create the Porta with an existing ID
        porta.setId(1L);

        int databaseSizeBeforeCreate = portaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(porta)))
            .andExpect(status().isBadRequest());

        // Validate the Porta in the database
        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLarguraIsRequired() throws Exception {
        int databaseSizeBeforeTest = portaRepository.findAll().size();
        // set the field null
        porta.setLargura(null);

        // Create the Porta, which fails.

        restPortaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(porta)))
            .andExpect(status().isBadRequest());

        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = portaRepository.findAll().size();
        // set the field null
        porta.setAltura(null);

        // Create the Porta, which fails.

        restPortaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(porta)))
            .andExpect(status().isBadRequest());

        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPortas() throws Exception {
        // Initialize the database
        portaRepository.saveAndFlush(porta);

        // Get all the portaList
        restPortaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(porta.getId().intValue())))
            .andExpect(jsonPath("$.[*].largura").value(hasItem(sameNumber(DEFAULT_LARGURA))))
            .andExpect(jsonPath("$.[*].altura").value(hasItem(sameNumber(DEFAULT_ALTURA))));
    }

    @Test
    @Transactional
    void getPorta() throws Exception {
        // Initialize the database
        portaRepository.saveAndFlush(porta);

        // Get the porta
        restPortaMockMvc
            .perform(get(ENTITY_API_URL_ID, porta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(porta.getId().intValue()))
            .andExpect(jsonPath("$.largura").value(sameNumber(DEFAULT_LARGURA)))
            .andExpect(jsonPath("$.altura").value(sameNumber(DEFAULT_ALTURA)));
    }

    @Test
    @Transactional
    void getNonExistingPorta() throws Exception {
        // Get the porta
        restPortaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPorta() throws Exception {
        // Initialize the database
        portaRepository.saveAndFlush(porta);

        int databaseSizeBeforeUpdate = portaRepository.findAll().size();

        // Update the porta
        Porta updatedPorta = portaRepository.findById(porta.getId()).get();
        // Disconnect from session so that the updates on updatedPorta are not directly saved in db
        em.detach(updatedPorta);
        updatedPorta.largura(UPDATED_LARGURA).altura(UPDATED_ALTURA);

        restPortaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPorta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPorta))
            )
            .andExpect(status().isOk());

        // Validate the Porta in the database
        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeUpdate);
        Porta testPorta = portaList.get(portaList.size() - 1);
        assertThat(testPorta.getLargura()).isEqualByComparingTo(UPDATED_LARGURA);
        assertThat(testPorta.getAltura()).isEqualByComparingTo(UPDATED_ALTURA);
    }

    @Test
    @Transactional
    void putNonExistingPorta() throws Exception {
        int databaseSizeBeforeUpdate = portaRepository.findAll().size();
        porta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, porta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(porta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Porta in the database
        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPorta() throws Exception {
        int databaseSizeBeforeUpdate = portaRepository.findAll().size();
        porta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(porta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Porta in the database
        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPorta() throws Exception {
        int databaseSizeBeforeUpdate = portaRepository.findAll().size();
        porta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(porta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Porta in the database
        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePortaWithPatch() throws Exception {
        // Initialize the database
        portaRepository.saveAndFlush(porta);

        int databaseSizeBeforeUpdate = portaRepository.findAll().size();

        // Update the porta using partial update
        Porta partialUpdatedPorta = new Porta();
        partialUpdatedPorta.setId(porta.getId());

        partialUpdatedPorta.altura(UPDATED_ALTURA);

        restPortaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPorta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPorta))
            )
            .andExpect(status().isOk());

        // Validate the Porta in the database
        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeUpdate);
        Porta testPorta = portaList.get(portaList.size() - 1);
        assertThat(testPorta.getLargura()).isEqualByComparingTo(DEFAULT_LARGURA);
        assertThat(testPorta.getAltura()).isEqualByComparingTo(UPDATED_ALTURA);
    }

    @Test
    @Transactional
    void fullUpdatePortaWithPatch() throws Exception {
        // Initialize the database
        portaRepository.saveAndFlush(porta);

        int databaseSizeBeforeUpdate = portaRepository.findAll().size();

        // Update the porta using partial update
        Porta partialUpdatedPorta = new Porta();
        partialUpdatedPorta.setId(porta.getId());

        partialUpdatedPorta.largura(UPDATED_LARGURA).altura(UPDATED_ALTURA);

        restPortaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPorta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPorta))
            )
            .andExpect(status().isOk());

        // Validate the Porta in the database
        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeUpdate);
        Porta testPorta = portaList.get(portaList.size() - 1);
        assertThat(testPorta.getLargura()).isEqualByComparingTo(UPDATED_LARGURA);
        assertThat(testPorta.getAltura()).isEqualByComparingTo(UPDATED_ALTURA);
    }

    @Test
    @Transactional
    void patchNonExistingPorta() throws Exception {
        int databaseSizeBeforeUpdate = portaRepository.findAll().size();
        porta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, porta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(porta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Porta in the database
        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPorta() throws Exception {
        int databaseSizeBeforeUpdate = portaRepository.findAll().size();
        porta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(porta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Porta in the database
        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPorta() throws Exception {
        int databaseSizeBeforeUpdate = portaRepository.findAll().size();
        porta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(porta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Porta in the database
        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePorta() throws Exception {
        // Initialize the database
        portaRepository.saveAndFlush(porta);

        int databaseSizeBeforeDelete = portaRepository.findAll().size();

        // Delete the porta
        restPortaMockMvc
            .perform(delete(ENTITY_API_URL_ID, porta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Porta> portaList = portaRepository.findAll();
        assertThat(portaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
