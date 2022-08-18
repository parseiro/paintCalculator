package com.vilelapinheiro.paintcalculator.web.rest;

import static com.vilelapinheiro.paintcalculator.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vilelapinheiro.paintcalculator.IntegrationTest;
import com.vilelapinheiro.paintcalculator.domain.Janela;
import com.vilelapinheiro.paintcalculator.domain.Parede;
import com.vilelapinheiro.paintcalculator.repository.JanelaRepository;
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
 * Integration tests for the {@link JanelaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JanelaResourceIT {

    private static final BigDecimal DEFAULT_LARGURA = new BigDecimal(1);
    private static final BigDecimal UPDATED_LARGURA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ALTURA = new BigDecimal(1);
    private static final BigDecimal UPDATED_ALTURA = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/janelas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JanelaRepository janelaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJanelaMockMvc;

    private Janela janela;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Janela createEntity(EntityManager em) {
        Janela janela = new Janela().largura(DEFAULT_LARGURA).altura(DEFAULT_ALTURA);
        // Add required entity
        Parede parede;
        if (TestUtil.findAll(em, Parede.class).isEmpty()) {
            parede = ParedeResourceIT.createEntity(em);
            em.persist(parede);
            em.flush();
        } else {
            parede = TestUtil.findAll(em, Parede.class).get(0);
        }
        janela.setParede(parede);
        return janela;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Janela createUpdatedEntity(EntityManager em) {
        Janela janela = new Janela().largura(UPDATED_LARGURA).altura(UPDATED_ALTURA);
        // Add required entity
        Parede parede;
        if (TestUtil.findAll(em, Parede.class).isEmpty()) {
            parede = ParedeResourceIT.createUpdatedEntity(em);
            em.persist(parede);
            em.flush();
        } else {
            parede = TestUtil.findAll(em, Parede.class).get(0);
        }
        janela.setParede(parede);
        return janela;
    }

    @BeforeEach
    public void initTest() {
        janela = createEntity(em);
    }

    @Test
    @Transactional
    void createJanela() throws Exception {
        int databaseSizeBeforeCreate = janelaRepository.findAll().size();
        // Create the Janela
        restJanelaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(janela)))
            .andExpect(status().isCreated());

        // Validate the Janela in the database
        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeCreate + 1);
        Janela testJanela = janelaList.get(janelaList.size() - 1);
        assertThat(testJanela.getLargura()).isEqualByComparingTo(DEFAULT_LARGURA);
        assertThat(testJanela.getAltura()).isEqualByComparingTo(DEFAULT_ALTURA);
    }

    @Test
    @Transactional
    void createJanelaWithExistingId() throws Exception {
        // Create the Janela with an existing ID
        janela.setId(1L);

        int databaseSizeBeforeCreate = janelaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJanelaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(janela)))
            .andExpect(status().isBadRequest());

        // Validate the Janela in the database
        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLarguraIsRequired() throws Exception {
        int databaseSizeBeforeTest = janelaRepository.findAll().size();
        // set the field null
        janela.setLargura(null);

        // Create the Janela, which fails.

        restJanelaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(janela)))
            .andExpect(status().isBadRequest());

        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = janelaRepository.findAll().size();
        // set the field null
        janela.setAltura(null);

        // Create the Janela, which fails.

        restJanelaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(janela)))
            .andExpect(status().isBadRequest());

        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJanelas() throws Exception {
        // Initialize the database
        janelaRepository.saveAndFlush(janela);

        // Get all the janelaList
        restJanelaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(janela.getId().intValue())))
            .andExpect(jsonPath("$.[*].largura").value(hasItem(sameNumber(DEFAULT_LARGURA))))
            .andExpect(jsonPath("$.[*].altura").value(hasItem(sameNumber(DEFAULT_ALTURA))));
    }

    @Test
    @Transactional
    void getJanela() throws Exception {
        // Initialize the database
        janelaRepository.saveAndFlush(janela);

        // Get the janela
        restJanelaMockMvc
            .perform(get(ENTITY_API_URL_ID, janela.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(janela.getId().intValue()))
            .andExpect(jsonPath("$.largura").value(sameNumber(DEFAULT_LARGURA)))
            .andExpect(jsonPath("$.altura").value(sameNumber(DEFAULT_ALTURA)));
    }

    @Test
    @Transactional
    void getNonExistingJanela() throws Exception {
        // Get the janela
        restJanelaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJanela() throws Exception {
        // Initialize the database
        janelaRepository.saveAndFlush(janela);

        int databaseSizeBeforeUpdate = janelaRepository.findAll().size();

        // Update the janela
        Janela updatedJanela = janelaRepository.findById(janela.getId()).get();
        // Disconnect from session so that the updates on updatedJanela are not directly saved in db
        em.detach(updatedJanela);
        updatedJanela.largura(UPDATED_LARGURA).altura(UPDATED_ALTURA);

        restJanelaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJanela.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJanela))
            )
            .andExpect(status().isOk());

        // Validate the Janela in the database
        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeUpdate);
        Janela testJanela = janelaList.get(janelaList.size() - 1);
        assertThat(testJanela.getLargura()).isEqualByComparingTo(UPDATED_LARGURA);
        assertThat(testJanela.getAltura()).isEqualByComparingTo(UPDATED_ALTURA);
    }

    @Test
    @Transactional
    void putNonExistingJanela() throws Exception {
        int databaseSizeBeforeUpdate = janelaRepository.findAll().size();
        janela.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJanelaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, janela.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(janela))
            )
            .andExpect(status().isBadRequest());

        // Validate the Janela in the database
        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJanela() throws Exception {
        int databaseSizeBeforeUpdate = janelaRepository.findAll().size();
        janela.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJanelaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(janela))
            )
            .andExpect(status().isBadRequest());

        // Validate the Janela in the database
        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJanela() throws Exception {
        int databaseSizeBeforeUpdate = janelaRepository.findAll().size();
        janela.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJanelaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(janela)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Janela in the database
        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJanelaWithPatch() throws Exception {
        // Initialize the database
        janelaRepository.saveAndFlush(janela);

        int databaseSizeBeforeUpdate = janelaRepository.findAll().size();

        // Update the janela using partial update
        Janela partialUpdatedJanela = new Janela();
        partialUpdatedJanela.setId(janela.getId());

        partialUpdatedJanela.largura(UPDATED_LARGURA).altura(UPDATED_ALTURA);

        restJanelaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJanela.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJanela))
            )
            .andExpect(status().isOk());

        // Validate the Janela in the database
        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeUpdate);
        Janela testJanela = janelaList.get(janelaList.size() - 1);
        assertThat(testJanela.getLargura()).isEqualByComparingTo(UPDATED_LARGURA);
        assertThat(testJanela.getAltura()).isEqualByComparingTo(UPDATED_ALTURA);
    }

    @Test
    @Transactional
    void fullUpdateJanelaWithPatch() throws Exception {
        // Initialize the database
        janelaRepository.saveAndFlush(janela);

        int databaseSizeBeforeUpdate = janelaRepository.findAll().size();

        // Update the janela using partial update
        Janela partialUpdatedJanela = new Janela();
        partialUpdatedJanela.setId(janela.getId());

        partialUpdatedJanela.largura(UPDATED_LARGURA).altura(UPDATED_ALTURA);

        restJanelaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJanela.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJanela))
            )
            .andExpect(status().isOk());

        // Validate the Janela in the database
        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeUpdate);
        Janela testJanela = janelaList.get(janelaList.size() - 1);
        assertThat(testJanela.getLargura()).isEqualByComparingTo(UPDATED_LARGURA);
        assertThat(testJanela.getAltura()).isEqualByComparingTo(UPDATED_ALTURA);
    }

    @Test
    @Transactional
    void patchNonExistingJanela() throws Exception {
        int databaseSizeBeforeUpdate = janelaRepository.findAll().size();
        janela.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJanelaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, janela.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(janela))
            )
            .andExpect(status().isBadRequest());

        // Validate the Janela in the database
        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJanela() throws Exception {
        int databaseSizeBeforeUpdate = janelaRepository.findAll().size();
        janela.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJanelaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(janela))
            )
            .andExpect(status().isBadRequest());

        // Validate the Janela in the database
        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJanela() throws Exception {
        int databaseSizeBeforeUpdate = janelaRepository.findAll().size();
        janela.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJanelaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(janela)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Janela in the database
        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJanela() throws Exception {
        // Initialize the database
        janelaRepository.saveAndFlush(janela);

        int databaseSizeBeforeDelete = janelaRepository.findAll().size();

        // Delete the janela
        restJanelaMockMvc
            .perform(delete(ENTITY_API_URL_ID, janela.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Janela> janelaList = janelaRepository.findAll();
        assertThat(janelaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
