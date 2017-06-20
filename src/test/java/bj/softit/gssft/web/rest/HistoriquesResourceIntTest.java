package bj.softit.gssft.web.rest;

import bj.softit.gssft.GStockSoftitApp;

import bj.softit.gssft.domain.Historiques;
import bj.softit.gssft.repository.HistoriquesRepository;
import bj.softit.gssft.service.HistoriquesService;
import bj.softit.gssft.repository.search.HistoriquesSearchRepository;
import bj.softit.gssft.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static bj.softit.gssft.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HistoriquesResource REST controller.
 *
 * @see HistoriquesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GStockSoftitApp.class)
public class HistoriquesResourceIntTest {

    private static final String DEFAULT_OPERATION = "AAAAAAAAAA";
    private static final String UPDATED_OPERATION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private HistoriquesRepository historiquesRepository;

    @Autowired
    private HistoriquesService historiquesService;

    @Autowired
    private HistoriquesSearchRepository historiquesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHistoriquesMockMvc;

    private Historiques historiques;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HistoriquesResource historiquesResource = new HistoriquesResource(historiquesService);
        this.restHistoriquesMockMvc = MockMvcBuilders.standaloneSetup(historiquesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historiques createEntity(EntityManager em) {
        Historiques historiques = new Historiques()
            .operation(DEFAULT_OPERATION)
            .date(DEFAULT_DATE);
        return historiques;
    }

    @Before
    public void initTest() {
        historiquesSearchRepository.deleteAll();
        historiques = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistoriques() throws Exception {
        int databaseSizeBeforeCreate = historiquesRepository.findAll().size();

        // Create the Historiques
        restHistoriquesMockMvc.perform(post("/api/historiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historiques)))
            .andExpect(status().isCreated());

        // Validate the Historiques in the database
        List<Historiques> historiquesList = historiquesRepository.findAll();
        assertThat(historiquesList).hasSize(databaseSizeBeforeCreate + 1);
        Historiques testHistoriques = historiquesList.get(historiquesList.size() - 1);
        assertThat(testHistoriques.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testHistoriques.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the Historiques in Elasticsearch
        Historiques historiquesEs = historiquesSearchRepository.findOne(testHistoriques.getId());
        assertThat(historiquesEs).isEqualToComparingFieldByField(testHistoriques);
    }

    @Test
    @Transactional
    public void createHistoriquesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historiquesRepository.findAll().size();

        // Create the Historiques with an existing ID
        historiques.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriquesMockMvc.perform(post("/api/historiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historiques)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Historiques> historiquesList = historiquesRepository.findAll();
        assertThat(historiquesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHistoriques() throws Exception {
        // Initialize the database
        historiquesRepository.saveAndFlush(historiques);

        // Get all the historiquesList
        restHistoriquesMockMvc.perform(get("/api/historiques?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiques.getId().intValue())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    public void getHistoriques() throws Exception {
        // Initialize the database
        historiquesRepository.saveAndFlush(historiques);

        // Get the historiques
        restHistoriquesMockMvc.perform(get("/api/historiques/{id}", historiques.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(historiques.getId().intValue()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingHistoriques() throws Exception {
        // Get the historiques
        restHistoriquesMockMvc.perform(get("/api/historiques/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistoriques() throws Exception {
        // Initialize the database
        historiquesService.save(historiques);

        int databaseSizeBeforeUpdate = historiquesRepository.findAll().size();

        // Update the historiques
        Historiques updatedHistoriques = historiquesRepository.findOne(historiques.getId());
        updatedHistoriques
            .operation(UPDATED_OPERATION)
            .date(UPDATED_DATE);

        restHistoriquesMockMvc.perform(put("/api/historiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHistoriques)))
            .andExpect(status().isOk());

        // Validate the Historiques in the database
        List<Historiques> historiquesList = historiquesRepository.findAll();
        assertThat(historiquesList).hasSize(databaseSizeBeforeUpdate);
        Historiques testHistoriques = historiquesList.get(historiquesList.size() - 1);
        assertThat(testHistoriques.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testHistoriques.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the Historiques in Elasticsearch
        Historiques historiquesEs = historiquesSearchRepository.findOne(testHistoriques.getId());
        assertThat(historiquesEs).isEqualToComparingFieldByField(testHistoriques);
    }

    @Test
    @Transactional
    public void updateNonExistingHistoriques() throws Exception {
        int databaseSizeBeforeUpdate = historiquesRepository.findAll().size();

        // Create the Historiques

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHistoriquesMockMvc.perform(put("/api/historiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historiques)))
            .andExpect(status().isCreated());

        // Validate the Historiques in the database
        List<Historiques> historiquesList = historiquesRepository.findAll();
        assertThat(historiquesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHistoriques() throws Exception {
        // Initialize the database
        historiquesService.save(historiques);

        int databaseSizeBeforeDelete = historiquesRepository.findAll().size();

        // Get the historiques
        restHistoriquesMockMvc.perform(delete("/api/historiques/{id}", historiques.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean historiquesExistsInEs = historiquesSearchRepository.exists(historiques.getId());
        assertThat(historiquesExistsInEs).isFalse();

        // Validate the database is empty
        List<Historiques> historiquesList = historiquesRepository.findAll();
        assertThat(historiquesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHistoriques() throws Exception {
        // Initialize the database
        historiquesService.save(historiques);

        // Search the historiques
        restHistoriquesMockMvc.perform(get("/api/_search/historiques?query=id:" + historiques.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiques.getId().intValue())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Historiques.class);
        Historiques historiques1 = new Historiques();
        historiques1.setId(1L);
        Historiques historiques2 = new Historiques();
        historiques2.setId(historiques1.getId());
        assertThat(historiques1).isEqualTo(historiques2);
        historiques2.setId(2L);
        assertThat(historiques1).isNotEqualTo(historiques2);
        historiques1.setId(null);
        assertThat(historiques1).isNotEqualTo(historiques2);
    }
}
