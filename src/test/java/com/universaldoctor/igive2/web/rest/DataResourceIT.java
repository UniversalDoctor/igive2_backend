package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.Igive2App;
import com.universaldoctor.igive2.domain.Data;
import com.universaldoctor.igive2.repository.DataRepository;
import com.universaldoctor.igive2.service.DataService;
import com.universaldoctor.igive2.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.universaldoctor.igive2.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.universaldoctor.igive2.domain.enumeration.DataType;
/**
 * Integration tests for the {@link DataResource} REST controller.
 */
@SpringBootTest(classes = Igive2App.class)
public class DataResourceIT {

    private static final DataType DEFAULT_DATA = DataType.HEIGHT;
    private static final DataType UPDATED_DATA = DataType.WEIGHT;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.now();
    private static final Instant UPDATED_DATE = Instant.now();

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private DataService dataService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restDataMockMvc;

    private Data data;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataResource dataResource = new DataResource(dataService);
        this.restDataMockMvc = MockMvcBuilders.standaloneSetup(dataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Data createEntity() {
        Data data = new Data()
            .data(DEFAULT_DATA)
            .notes(DEFAULT_NOTES)
            .date(DEFAULT_DATE)
            .value(DEFAULT_VALUE);
        return data;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Data createUpdatedEntity() {
        Data data = new Data()
            .data(UPDATED_DATA)
            .notes(UPDATED_NOTES)
            .date(UPDATED_DATE)
            .value(UPDATED_VALUE);
        return data;
    }

    @BeforeEach
    public void initTest() {
        dataRepository.deleteAll();
        data = createEntity();
    }

    @Test
    public void createData() throws Exception {
        int databaseSizeBeforeCreate = dataRepository.findAll().size();

        // Create the Data
        restDataMockMvc.perform(post("/api/data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(data)))
            .andExpect(status().isCreated());

        // Validate the Data in the database
        List<Data> dataList = dataRepository.findAll();
        assertThat(dataList).hasSize(databaseSizeBeforeCreate + 1);
        Data testData = dataList.get(dataList.size() - 1);
        assertThat(testData.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testData.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testData.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    public void createDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataRepository.findAll().size();

        // Create the Data with an existing ID
        data.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataMockMvc.perform(post("/api/data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(data)))
            .andExpect(status().isBadRequest());

        // Validate the Data in the database
        List<Data> dataList = dataRepository.findAll();
        assertThat(dataList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataRepository.findAll().size();
        // set the field null
        data.setData(null);

        // Create the Data, which fails.

        restDataMockMvc.perform(post("/api/data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(data)))
            .andExpect(status().isBadRequest());

        List<Data> dataList = dataRepository.findAll();
        assertThat(dataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataRepository.findAll().size();
        // set the field null
        data.setValue(null);

        // Create the Data, which fails.

        restDataMockMvc.perform(post("/api/data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(data)))
            .andExpect(status().isBadRequest());

        List<Data> dataList = dataRepository.findAll();
        assertThat(dataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllData() throws Exception {
        // Initialize the database
        dataRepository.save(data);

        // Get all the dataList
        restDataMockMvc.perform(get("/api/data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(data.getId())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    public void getData() throws Exception {
        // Initialize the database
        dataRepository.save(data);

        // Get the data
        restDataMockMvc.perform(get("/api/data/{id}", data.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(data.getId()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    public void getNonExistingData() throws Exception {
        // Get the data
        restDataMockMvc.perform(get("/api/data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateData() throws Exception {
        // Initialize the database
        dataService.save(data);

        int databaseSizeBeforeUpdate = dataRepository.findAll().size();

        // Update the data
        Data updatedData = dataRepository.findById(data.getId()).get();
        updatedData
            .data(UPDATED_DATA)
            .notes(UPDATED_NOTES)
            .date(UPDATED_DATE)
            .value(UPDATED_VALUE);

        restDataMockMvc.perform(put("/api/data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedData)))
            .andExpect(status().isOk());

        // Validate the Data in the database
        List<Data> dataList = dataRepository.findAll();
        assertThat(dataList).hasSize(databaseSizeBeforeUpdate);
        Data testData = dataList.get(dataList.size() - 1);
        assertThat(testData.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testData.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testData.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    public void updateNonExistingData() throws Exception {
        int databaseSizeBeforeUpdate = dataRepository.findAll().size();

        // Create the Data

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataMockMvc.perform(put("/api/data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(data)))
            .andExpect(status().isBadRequest());

        // Validate the Data in the database
        List<Data> dataList = dataRepository.findAll();
        assertThat(dataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteData() throws Exception {
        // Initialize the database
        dataService.save(data);

        int databaseSizeBeforeDelete = dataRepository.findAll().size();

        // Delete the data
        restDataMockMvc.perform(delete("/api/data/{id}", data.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Data> dataList = dataRepository.findAll();
        assertThat(dataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
