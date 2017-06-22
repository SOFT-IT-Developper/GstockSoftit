package bj.softit.gssft.web.rest;

import bj.softit.gssft.GStockSoftitApp;

import bj.softit.gssft.domain.OutStock;
import bj.softit.gssft.repository.OutStockRepository;
import bj.softit.gssft.service.OutStockService;
import bj.softit.gssft.repository.search.OutStockSearchRepository;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OutStockResource REST controller.
 *
 * @see OutStockResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GStockSoftitApp.class)
public class OutStockResourceIntTest {

    private static final BigDecimal DEFAULT_QUANTITE = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITE = new BigDecimal(2);

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CAUSE = "AAAAAAAAAA";
    private static final String UPDATED_CAUSE = "BBBBBBBBBB";

    @Autowired
    private OutStockRepository outStockRepository;

    @Autowired
    private OutStockService outStockService;

    @Autowired
    private OutStockSearchRepository outStockSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOutStockMockMvc;

    private OutStock outStock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OutStockResource outStockResource = new OutStockResource(outStockService);
        this.restOutStockMockMvc = MockMvcBuilders.standaloneSetup(outStockResource)
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
    public static OutStock createEntity(EntityManager em) {
        OutStock outStock = new OutStock()
            .quantite(DEFAULT_QUANTITE)
            .date(DEFAULT_DATE)
            .cause(DEFAULT_CAUSE);
        return outStock;
    }

    @Before
    public void initTest() {
        outStockSearchRepository.deleteAll();
        outStock = createEntity(em);
    }

    @Test
    @Transactional
    public void createOutStock() throws Exception {
        int databaseSizeBeforeCreate = outStockRepository.findAll().size();

        // Create the OutStock
        restOutStockMockMvc.perform(post("/api/out-stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outStock)))
            .andExpect(status().isCreated());

        // Validate the OutStock in the database
        List<OutStock> outStockList = outStockRepository.findAll();
        assertThat(outStockList).hasSize(databaseSizeBeforeCreate + 1);
        OutStock testOutStock = outStockList.get(outStockList.size() - 1);
        assertThat(testOutStock.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testOutStock.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOutStock.getCause()).isEqualTo(DEFAULT_CAUSE);

        // Validate the OutStock in Elasticsearch
        OutStock outStockEs = outStockSearchRepository.findOne(testOutStock.getId());
        assertThat(outStockEs).isEqualToComparingFieldByField(testOutStock);
    }

    @Test
    @Transactional
    public void createOutStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = outStockRepository.findAll().size();

        // Create the OutStock with an existing ID
        outStock.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutStockMockMvc.perform(post("/api/out-stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outStock)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<OutStock> outStockList = outStockRepository.findAll();
        assertThat(outStockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOutStocks() throws Exception {
        // Initialize the database
        outStockRepository.saveAndFlush(outStock);

        // Get all the outStockList
        restOutStockMockMvc.perform(get("/api/out-stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].cause").value(hasItem(DEFAULT_CAUSE.toString())));
    }

    @Test
    @Transactional
    public void getOutStock() throws Exception {
        // Initialize the database
        outStockRepository.saveAndFlush(outStock);

        // Get the outStock
        restOutStockMockMvc.perform(get("/api/out-stocks/{id}", outStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(outStock.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.cause").value(DEFAULT_CAUSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOutStock() throws Exception {
        // Get the outStock
        restOutStockMockMvc.perform(get("/api/out-stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutStock() throws Exception {
        // Initialize the database
        outStockService.save(outStock);

        int databaseSizeBeforeUpdate = outStockRepository.findAll().size();

        // Update the outStock
        OutStock updatedOutStock = outStockRepository.findOne(outStock.getId());
        updatedOutStock
            .quantite(UPDATED_QUANTITE)
            .date(UPDATED_DATE)
            .cause(UPDATED_CAUSE);

        restOutStockMockMvc.perform(put("/api/out-stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOutStock)))
            .andExpect(status().isOk());

        // Validate the OutStock in the database
        List<OutStock> outStockList = outStockRepository.findAll();
        assertThat(outStockList).hasSize(databaseSizeBeforeUpdate);
        OutStock testOutStock = outStockList.get(outStockList.size() - 1);
        assertThat(testOutStock.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testOutStock.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOutStock.getCause()).isEqualTo(UPDATED_CAUSE);

        // Validate the OutStock in Elasticsearch
        OutStock outStockEs = outStockSearchRepository.findOne(testOutStock.getId());
        assertThat(outStockEs).isEqualToComparingFieldByField(testOutStock);
    }

    @Test
    @Transactional
    public void updateNonExistingOutStock() throws Exception {
        int databaseSizeBeforeUpdate = outStockRepository.findAll().size();

        // Create the OutStock

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOutStockMockMvc.perform(put("/api/out-stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outStock)))
            .andExpect(status().isCreated());

        // Validate the OutStock in the database
        List<OutStock> outStockList = outStockRepository.findAll();
        assertThat(outStockList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOutStock() throws Exception {
        // Initialize the database
        outStockService.save(outStock);

        int databaseSizeBeforeDelete = outStockRepository.findAll().size();

        // Get the outStock
        restOutStockMockMvc.perform(delete("/api/out-stocks/{id}", outStock.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean outStockExistsInEs = outStockSearchRepository.exists(outStock.getId());
        assertThat(outStockExistsInEs).isFalse();

        // Validate the database is empty
        List<OutStock> outStockList = outStockRepository.findAll();
        assertThat(outStockList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOutStock() throws Exception {
        // Initialize the database
        outStockService.save(outStock);

        // Search the outStock
        restOutStockMockMvc.perform(get("/api/_search/out-stocks?query=id:" + outStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].cause").value(hasItem(DEFAULT_CAUSE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutStock.class);
        OutStock outStock1 = new OutStock();
        outStock1.setId(1L);
        OutStock outStock2 = new OutStock();
        outStock2.setId(outStock1.getId());
        assertThat(outStock1).isEqualTo(outStock2);
        outStock2.setId(2L);
        assertThat(outStock1).isNotEqualTo(outStock2);
        outStock1.setId(null);
        assertThat(outStock1).isNotEqualTo(outStock2);
    }
}
