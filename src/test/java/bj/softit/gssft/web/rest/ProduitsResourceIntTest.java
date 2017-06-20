package bj.softit.gssft.web.rest;

import bj.softit.gssft.GStockSoftitApp;

import bj.softit.gssft.domain.Produits;
import bj.softit.gssft.repository.ProduitsRepository;
import bj.softit.gssft.service.ProduitsService;
import bj.softit.gssft.repository.search.ProduitsSearchRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProduitsResource REST controller.
 *
 * @see ProduitsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GStockSoftitApp.class)
public class ProduitsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLACEMENT = "AAAAAAAAAA";
    private static final String UPDATED_EMPLACEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CAPTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CAPTURE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CAPTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CAPTURE_CONTENT_TYPE = "image/png";

    @Autowired
    private ProduitsRepository produitsRepository;

    @Autowired
    private ProduitsService produitsService;

    @Autowired
    private ProduitsSearchRepository produitsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProduitsMockMvc;

    private Produits produits;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProduitsResource produitsResource = new ProduitsResource(produitsService);
        this.restProduitsMockMvc = MockMvcBuilders.standaloneSetup(produitsResource)
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
    public static Produits createEntity(EntityManager em) {
        Produits produits = new Produits()
            .name(DEFAULT_NAME)
            .emplacement(DEFAULT_EMPLACEMENT)
            .description(DEFAULT_DESCRIPTION)
            .capture(DEFAULT_CAPTURE)
            .captureContentType(DEFAULT_CAPTURE_CONTENT_TYPE);
        return produits;
    }

    @Before
    public void initTest() {
        produitsSearchRepository.deleteAll();
        produits = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduits() throws Exception {
        int databaseSizeBeforeCreate = produitsRepository.findAll().size();

        // Create the Produits
        restProduitsMockMvc.perform(post("/api/produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produits)))
            .andExpect(status().isCreated());

        // Validate the Produits in the database
        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeCreate + 1);
        Produits testProduits = produitsList.get(produitsList.size() - 1);
        assertThat(testProduits.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduits.getEmplacement()).isEqualTo(DEFAULT_EMPLACEMENT);
        assertThat(testProduits.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduits.getCapture()).isEqualTo(DEFAULT_CAPTURE);
        assertThat(testProduits.getCaptureContentType()).isEqualTo(DEFAULT_CAPTURE_CONTENT_TYPE);

        // Validate the Produits in Elasticsearch
        Produits produitsEs = produitsSearchRepository.findOne(testProduits.getId());
        assertThat(produitsEs).isEqualToComparingFieldByField(testProduits);
    }

    @Test
    @Transactional
    public void createProduitsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = produitsRepository.findAll().size();

        // Create the Produits with an existing ID
        produits.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProduitsMockMvc.perform(post("/api/produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produits)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitsRepository.findAll().size();
        // set the field null
        produits.setName(null);

        // Create the Produits, which fails.

        restProduitsMockMvc.perform(post("/api/produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produits)))
            .andExpect(status().isBadRequest());

        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProduits() throws Exception {
        // Initialize the database
        produitsRepository.saveAndFlush(produits);

        // Get all the produitsList
        restProduitsMockMvc.perform(get("/api/produits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produits.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].emplacement").value(hasItem(DEFAULT_EMPLACEMENT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].captureContentType").value(hasItem(DEFAULT_CAPTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].capture").value(hasItem(Base64Utils.encodeToString(DEFAULT_CAPTURE))));
    }

    @Test
    @Transactional
    public void getProduits() throws Exception {
        // Initialize the database
        produitsRepository.saveAndFlush(produits);

        // Get the produits
        restProduitsMockMvc.perform(get("/api/produits/{id}", produits.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(produits.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.emplacement").value(DEFAULT_EMPLACEMENT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.captureContentType").value(DEFAULT_CAPTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.capture").value(Base64Utils.encodeToString(DEFAULT_CAPTURE)));
    }

    @Test
    @Transactional
    public void getNonExistingProduits() throws Exception {
        // Get the produits
        restProduitsMockMvc.perform(get("/api/produits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduits() throws Exception {
        // Initialize the database
        produitsService.save(produits);

        int databaseSizeBeforeUpdate = produitsRepository.findAll().size();

        // Update the produits
        Produits updatedProduits = produitsRepository.findOne(produits.getId());
        updatedProduits
            .name(UPDATED_NAME)
            .emplacement(UPDATED_EMPLACEMENT)
            .description(UPDATED_DESCRIPTION)
            .capture(UPDATED_CAPTURE)
            .captureContentType(UPDATED_CAPTURE_CONTENT_TYPE);

        restProduitsMockMvc.perform(put("/api/produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProduits)))
            .andExpect(status().isOk());

        // Validate the Produits in the database
        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeUpdate);
        Produits testProduits = produitsList.get(produitsList.size() - 1);
        assertThat(testProduits.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduits.getEmplacement()).isEqualTo(UPDATED_EMPLACEMENT);
        assertThat(testProduits.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduits.getCapture()).isEqualTo(UPDATED_CAPTURE);
        assertThat(testProduits.getCaptureContentType()).isEqualTo(UPDATED_CAPTURE_CONTENT_TYPE);

        // Validate the Produits in Elasticsearch
        Produits produitsEs = produitsSearchRepository.findOne(testProduits.getId());
        assertThat(produitsEs).isEqualToComparingFieldByField(testProduits);
    }

    @Test
    @Transactional
    public void updateNonExistingProduits() throws Exception {
        int databaseSizeBeforeUpdate = produitsRepository.findAll().size();

        // Create the Produits

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProduitsMockMvc.perform(put("/api/produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produits)))
            .andExpect(status().isCreated());

        // Validate the Produits in the database
        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProduits() throws Exception {
        // Initialize the database
        produitsService.save(produits);

        int databaseSizeBeforeDelete = produitsRepository.findAll().size();

        // Get the produits
        restProduitsMockMvc.perform(delete("/api/produits/{id}", produits.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean produitsExistsInEs = produitsSearchRepository.exists(produits.getId());
        assertThat(produitsExistsInEs).isFalse();

        // Validate the database is empty
        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProduits() throws Exception {
        // Initialize the database
        produitsService.save(produits);

        // Search the produits
        restProduitsMockMvc.perform(get("/api/_search/produits?query=id:" + produits.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produits.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].emplacement").value(hasItem(DEFAULT_EMPLACEMENT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].captureContentType").value(hasItem(DEFAULT_CAPTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].capture").value(hasItem(Base64Utils.encodeToString(DEFAULT_CAPTURE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Produits.class);
        Produits produits1 = new Produits();
        produits1.setId(1L);
        Produits produits2 = new Produits();
        produits2.setId(produits1.getId());
        assertThat(produits1).isEqualTo(produits2);
        produits2.setId(2L);
        assertThat(produits1).isNotEqualTo(produits2);
        produits1.setId(null);
        assertThat(produits1).isNotEqualTo(produits2);
    }
}
