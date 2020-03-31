package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.Igive2App;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.repository.StudyRepository;
import com.universaldoctor.igive2.service.StudyService;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.universaldoctor.igive2.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.universaldoctor.igive2.domain.enumeration.State;
/**
 * Integration tests for the {@link StudyResource} REST controller.
 */
@SpringBootTest(classes = Igive2App.class)
public class StudyResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ICON = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ICON = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ICON_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ICON_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MORE_INFO = "AAAAAAAAAA";
    private static final String UPDATED_MORE_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final Instant  DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant  UPDATED_START_DATE = Instant.now();

    private static final Instant  DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant  UPDATED_END_DATE = Instant.now();

    private static final State DEFAULT_STATE = State.DRAFT;
    private static final State UPDATED_STATE = State.PUBLISHED;

    private static final Boolean DEFAULT_RECRUITING = false;
    private static final Boolean UPDATED_RECRUITING = true;

    private static final String DEFAULT_REQUESTED_DATA = "AAAAAAAAAA";
    private static final String UPDATED_REQUESTED_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_JUSTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_DATA_JUSTIFICATION = "BBBBBBBBBB";

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyService studyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restStudyMockMvc;

    private Study study;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudyResource studyResource = new StudyResource(studyService);
        this.restStudyMockMvc = MockMvcBuilders.standaloneSetup(studyResource)
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
    public static Study createEntity() {
        Study study = new Study()
            .code(DEFAULT_CODE)
            .icon(DEFAULT_ICON)
            .iconContentType(DEFAULT_ICON_CONTENT_TYPE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .website(DEFAULT_MORE_INFO)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .state(DEFAULT_STATE)
            .recruiting(DEFAULT_RECRUITING)
            .requestedData(DEFAULT_REQUESTED_DATA)
            .dataJustification(DEFAULT_DATA_JUSTIFICATION);
        return study;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Study createUpdatedEntity() {
        Study study = new Study()
            .code(UPDATED_CODE)
            .icon(UPDATED_ICON)
            .iconContentType(UPDATED_ICON_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .website(UPDATED_MORE_INFO)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .state(UPDATED_STATE)
            .recruiting(UPDATED_RECRUITING)
            .requestedData(UPDATED_REQUESTED_DATA)
            .dataJustification(UPDATED_DATA_JUSTIFICATION);
        return study;
    }

    @BeforeEach
    public void initTest() {
        studyRepository.deleteAll();
        study = createEntity();
    }

    @Test
    public void createStudy() throws Exception {
        int databaseSizeBeforeCreate = studyRepository.findAll().size();

        // Create the Study
        restStudyMockMvc.perform(post("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isCreated());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeCreate + 1);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testStudy.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testStudy.getIconContentType()).isEqualTo(DEFAULT_ICON_CONTENT_TYPE);
        assertThat(testStudy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStudy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStudy.getWebsite()).isEqualTo(DEFAULT_MORE_INFO);
        assertThat(testStudy.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testStudy.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testStudy.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testStudy.isRecruiting()).isEqualTo(DEFAULT_RECRUITING);
        assertThat(testStudy.getRequestedData()).isEqualTo(DEFAULT_REQUESTED_DATA);
        assertThat(testStudy.getDataJustification()).isEqualTo(DEFAULT_DATA_JUSTIFICATION);
    }

    @Test
    public void createStudyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studyRepository.findAll().size();

        // Create the Study with an existing ID
        study.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudyMockMvc.perform(post("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyRepository.findAll().size();
        // set the field null
        study.setCode(null);

        // Create the Study, which fails.

        restStudyMockMvc.perform(post("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isBadRequest());

        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyRepository.findAll().size();
        // set the field null
        study.setName(null);

        // Create the Study, which fails.

        restStudyMockMvc.perform(post("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isBadRequest());

        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyRepository.findAll().size();
        // set the field null
        study.setState(null);

        // Create the Study, which fails.

        restStudyMockMvc.perform(post("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isBadRequest());

        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllStudies() throws Exception {
        // Initialize the database
        studyRepository.save(study);

        // Get all the studyList
        restStudyMockMvc.perform(get("/api/studies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(study.getId())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].iconContentType").value(hasItem(DEFAULT_ICON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICON))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_MORE_INFO)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].recruiting").value(hasItem(DEFAULT_RECRUITING.booleanValue())))
            .andExpect(jsonPath("$.[*].requestedData").value(hasItem(DEFAULT_REQUESTED_DATA)))
            .andExpect(jsonPath("$.[*].dataJustification").value(hasItem(DEFAULT_DATA_JUSTIFICATION)));
    }

    @Test
    public void getStudy() throws Exception {
        // Initialize the database
        studyRepository.save(study);

        // Get the study
        restStudyMockMvc.perform(get("/api/studies/{id}", study.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(study.getId()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.iconContentType").value(DEFAULT_ICON_CONTENT_TYPE))
            .andExpect(jsonPath("$.icon").value(Base64Utils.encodeToString(DEFAULT_ICON)))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.website").value(DEFAULT_MORE_INFO))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.recruiting").value(DEFAULT_RECRUITING.booleanValue()))
            .andExpect(jsonPath("$.requestedData").value(DEFAULT_REQUESTED_DATA))
            .andExpect(jsonPath("$.dataJustification").value(DEFAULT_DATA_JUSTIFICATION));
    }

    @Test
    public void getNonExistingStudy() throws Exception {
        // Get the study
        restStudyMockMvc.perform(get("/api/studies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateStudy() throws Exception {
        // Initialize the database
        studyService.save(study);

        int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Update the study
        Study updatedStudy = studyRepository.findById(study.getId()).get();
        updatedStudy
            .code(UPDATED_CODE)
            .icon(UPDATED_ICON)
            .iconContentType(UPDATED_ICON_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .website(UPDATED_MORE_INFO)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .state(UPDATED_STATE)
            .recruiting(UPDATED_RECRUITING)
            .requestedData(UPDATED_REQUESTED_DATA)
            .dataJustification(UPDATED_DATA_JUSTIFICATION);

        restStudyMockMvc.perform(put("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudy)))
            .andExpect(status().isOk());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStudy.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testStudy.getIconContentType()).isEqualTo(UPDATED_ICON_CONTENT_TYPE);
        assertThat(testStudy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStudy.getWebsite()).isEqualTo(UPDATED_MORE_INFO);
        assertThat(testStudy.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testStudy.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testStudy.isRecruiting()).isEqualTo(UPDATED_RECRUITING);
        assertThat(testStudy.getRequestedData()).isEqualTo(UPDATED_REQUESTED_DATA);
        assertThat(testStudy.getDataJustification()).isEqualTo(UPDATED_DATA_JUSTIFICATION);
    }

    @Test
    public void updateNonExistingStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Create the Study

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyMockMvc.perform(put("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteStudy() throws Exception {
        // Initialize the database
        studyService.save(study);

        int databaseSizeBeforeDelete = studyRepository.findAll().size();

        // Delete the study
        restStudyMockMvc.perform(delete("/api/studies/{id}", study.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
