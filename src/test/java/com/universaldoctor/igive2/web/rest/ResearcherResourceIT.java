package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.Igive2App;
import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.repository.ResearcherRepository;
import com.universaldoctor.igive2.service.ResearcherService;
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


import java.util.List;

import static com.universaldoctor.igive2.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ResearcherResource} REST controller.
 */
@SpringBootTest(classes = Igive2App.class)
public class ResearcherResourceIT {

    private static final String DEFAULT_INSTITUTION = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION = "BBBBBBBBBB";

    private static final String DEFAULT_HONORIFICS = "AAAAAAAAAA";
    private static final String UPDATED_HONORIFICS = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    @Autowired
    private ResearcherRepository researcherRepository;

    @Autowired
    private ResearcherService researcherService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restResearcherMockMvc;

    private Researcher researcher;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResearcherResource researcherResource = new ResearcherResource(researcherService);
        this.restResearcherMockMvc = MockMvcBuilders.standaloneSetup(researcherResource)
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
    public static Researcher createEntity() {
        Researcher researcher = new Researcher()
            .institution(DEFAULT_INSTITUTION)
            .honorifics(DEFAULT_HONORIFICS)
            .userId(DEFAULT_USER_ID);
        return researcher;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Researcher createUpdatedEntity() {
        Researcher researcher = new Researcher()
            .institution(UPDATED_INSTITUTION)
            .honorifics(UPDATED_HONORIFICS)
            .userId(UPDATED_USER_ID);
        return researcher;
    }

    @BeforeEach
    public void initTest() {
        researcherRepository.deleteAll();
        researcher = createEntity();
    }

    @Test
    public void createResearcher() throws Exception {
        int databaseSizeBeforeCreate = researcherRepository.findAll().size();

        // Create the Researcher
        restResearcherMockMvc.perform(post("/api/researchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(researcher)))
            .andExpect(status().isCreated());

        // Validate the Researcher in the database
        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeCreate + 1);
        Researcher testResearcher = researcherList.get(researcherList.size() - 1);
        assertThat(testResearcher.getInstitution()).isEqualTo(DEFAULT_INSTITUTION);
        assertThat(testResearcher.getHonorifics()).isEqualTo(DEFAULT_HONORIFICS);
        assertThat(testResearcher.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    public void createResearcherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = researcherRepository.findAll().size();

        // Create the Researcher with an existing ID
        researcher.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restResearcherMockMvc.perform(post("/api/researchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(researcher)))
            .andExpect(status().isBadRequest());

        // Validate the Researcher in the database
        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkInstitutionIsRequired() throws Exception {
        int databaseSizeBeforeTest = researcherRepository.findAll().size();
        // set the field null
        researcher.setInstitution(null);

        // Create the Researcher, which fails.

        restResearcherMockMvc.perform(post("/api/researchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(researcher)))
            .andExpect(status().isBadRequest());

        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkHonorificsIsRequired() throws Exception {
        int databaseSizeBeforeTest = researcherRepository.findAll().size();
        // set the field null
        researcher.setHonorifics(null);

        // Create the Researcher, which fails.

        restResearcherMockMvc.perform(post("/api/researchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(researcher)))
            .andExpect(status().isBadRequest());

        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = researcherRepository.findAll().size();
        // set the field null
        researcher.setUserId(null);

        // Create the Researcher, which fails.

        restResearcherMockMvc.perform(post("/api/researchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(researcher)))
            .andExpect(status().isBadRequest());

        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllResearchers() throws Exception {
        // Initialize the database
        researcherRepository.save(researcher);

        // Get all the researcherList
        restResearcherMockMvc.perform(get("/api/researchers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(researcher.getId())))
            .andExpect(jsonPath("$.[*].institution").value(hasItem(DEFAULT_INSTITUTION)))
            .andExpect(jsonPath("$.[*].honorifics").value(hasItem(DEFAULT_HONORIFICS)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }
    
    @Test
    public void getResearcher() throws Exception {
        // Initialize the database
        researcherRepository.save(researcher);

        // Get the researcher
        restResearcherMockMvc.perform(get("/api/researchers/{id}", researcher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(researcher.getId()))
            .andExpect(jsonPath("$.institution").value(DEFAULT_INSTITUTION))
            .andExpect(jsonPath("$.honorifics").value(DEFAULT_HONORIFICS))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID));
    }

    @Test
    public void getNonExistingResearcher() throws Exception {
        // Get the researcher
        restResearcherMockMvc.perform(get("/api/researchers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateResearcher() throws Exception {
        // Initialize the database
        researcherService.save(researcher);

        int databaseSizeBeforeUpdate = researcherRepository.findAll().size();

        // Update the researcher
        Researcher updatedResearcher = researcherRepository.findById(researcher.getId()).get();
        updatedResearcher
            .institution(UPDATED_INSTITUTION)
            .honorifics(UPDATED_HONORIFICS)
            .userId(UPDATED_USER_ID);

        restResearcherMockMvc.perform(put("/api/researchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResearcher)))
            .andExpect(status().isOk());

        // Validate the Researcher in the database
        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeUpdate);
        Researcher testResearcher = researcherList.get(researcherList.size() - 1);
        assertThat(testResearcher.getInstitution()).isEqualTo(UPDATED_INSTITUTION);
        assertThat(testResearcher.getHonorifics()).isEqualTo(UPDATED_HONORIFICS);
        assertThat(testResearcher.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    public void updateNonExistingResearcher() throws Exception {
        int databaseSizeBeforeUpdate = researcherRepository.findAll().size();

        // Create the Researcher

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResearcherMockMvc.perform(put("/api/researchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(researcher)))
            .andExpect(status().isBadRequest());

        // Validate the Researcher in the database
        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteResearcher() throws Exception {
        // Initialize the database
        researcherService.save(researcher);

        int databaseSizeBeforeDelete = researcherRepository.findAll().size();

        // Delete the researcher
        restResearcherMockMvc.perform(delete("/api/researchers/{id}", researcher.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
