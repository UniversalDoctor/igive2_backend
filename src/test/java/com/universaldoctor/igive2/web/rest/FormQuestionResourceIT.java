package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.Igive2App;
import com.universaldoctor.igive2.domain.FormQuestion;
import com.universaldoctor.igive2.repository.FormQuestionRepository;
import com.universaldoctor.igive2.service.FormQuestionService;
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

import com.universaldoctor.igive2.domain.enumeration.QuestionType;
/**
 * Integration tests for the {@link FormQuestionResource} REST controller.
 */
@SpringBootTest(classes = Igive2App.class)
public class FormQuestionResourceIT {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MANDATORY = false;
    private static final Boolean UPDATED_IS_MANDATORY = true;

    private static final QuestionType DEFAULT_TYPE = QuestionType.FREEANSWER;
    private static final QuestionType UPDATED_TYPE = QuestionType.FREELONGANSWER;

    private static final String DEFAULT_OPTIONS = "AAAAAAAAAA";
    private static final String UPDATED_OPTIONS = "BBBBBBBBBB";

    @Autowired
    private FormQuestionRepository formQuestionRepository;

    @Autowired
    private FormQuestionService formQuestionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restFormQuestionMockMvc;

    private FormQuestion formQuestion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormQuestionResource formQuestionResource = new FormQuestionResource(formQuestionService);
        this.restFormQuestionMockMvc = MockMvcBuilders.standaloneSetup(formQuestionResource)
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
    public static FormQuestion createEntity() {
        FormQuestion formQuestion = new FormQuestion()
            .question(DEFAULT_QUESTION)
            .isMandatory(DEFAULT_IS_MANDATORY)
            .type(DEFAULT_TYPE)
            .options(DEFAULT_OPTIONS);
        return formQuestion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormQuestion createUpdatedEntity() {
        FormQuestion formQuestion = new FormQuestion()
            .question(UPDATED_QUESTION)
            .isMandatory(UPDATED_IS_MANDATORY)
            .type(UPDATED_TYPE)
            .options(UPDATED_OPTIONS);
        return formQuestion;
    }

    @BeforeEach
    public void initTest() {
        formQuestionRepository.deleteAll();
        formQuestion = createEntity();
    }

    @Test
    public void createFormQuestion() throws Exception {
        int databaseSizeBeforeCreate = formQuestionRepository.findAll().size();

        // Create the FormQuestion
        restFormQuestionMockMvc.perform(post("/api/form-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formQuestion)))
            .andExpect(status().isCreated());

        // Validate the FormQuestion in the database
        List<FormQuestion> formQuestionList = formQuestionRepository.findAll();
        assertThat(formQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        FormQuestion testFormQuestion = formQuestionList.get(formQuestionList.size() - 1);
        assertThat(testFormQuestion.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testFormQuestion.isIsMandatory()).isEqualTo(DEFAULT_IS_MANDATORY);
        assertThat(testFormQuestion.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFormQuestion.getOptions()).isEqualTo(DEFAULT_OPTIONS);
    }

    @Test
    public void createFormQuestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formQuestionRepository.findAll().size();

        // Create the FormQuestion with an existing ID
        formQuestion.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormQuestionMockMvc.perform(post("/api/form-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formQuestion)))
            .andExpect(status().isBadRequest());

        // Validate the FormQuestion in the database
        List<FormQuestion> formQuestionList = formQuestionRepository.findAll();
        assertThat(formQuestionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkQuestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = formQuestionRepository.findAll().size();
        // set the field null
        formQuestion.setQuestion(null);

        // Create the FormQuestion, which fails.

        restFormQuestionMockMvc.perform(post("/api/form-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formQuestion)))
            .andExpect(status().isBadRequest());

        List<FormQuestion> formQuestionList = formQuestionRepository.findAll();
        assertThat(formQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllFormQuestions() throws Exception {
        // Initialize the database
        formQuestionRepository.save(formQuestion);

        // Get all the formQuestionList
        restFormQuestionMockMvc.perform(get("/api/form-questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formQuestion.getId())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].isMandatory").value(hasItem(DEFAULT_IS_MANDATORY.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)));
    }
    
    @Test
    public void getFormQuestion() throws Exception {
        // Initialize the database
        formQuestionRepository.save(formQuestion);

        // Get the formQuestion
        restFormQuestionMockMvc.perform(get("/api/form-questions/{id}", formQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formQuestion.getId()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION))
            .andExpect(jsonPath("$.isMandatory").value(DEFAULT_IS_MANDATORY.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.options").value(DEFAULT_OPTIONS));
    }

    @Test
    public void getNonExistingFormQuestion() throws Exception {
        // Get the formQuestion
        restFormQuestionMockMvc.perform(get("/api/form-questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateFormQuestion() throws Exception {
        // Initialize the database
        formQuestionService.save(formQuestion);

        int databaseSizeBeforeUpdate = formQuestionRepository.findAll().size();

        // Update the formQuestion
        FormQuestion updatedFormQuestion = formQuestionRepository.findById(formQuestion.getId()).get();
        updatedFormQuestion
            .question(UPDATED_QUESTION)
            .isMandatory(UPDATED_IS_MANDATORY)
            .type(UPDATED_TYPE)
            .options(UPDATED_OPTIONS);

        restFormQuestionMockMvc.perform(put("/api/form-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormQuestion)))
            .andExpect(status().isOk());

        // Validate the FormQuestion in the database
        List<FormQuestion> formQuestionList = formQuestionRepository.findAll();
        assertThat(formQuestionList).hasSize(databaseSizeBeforeUpdate);
        FormQuestion testFormQuestion = formQuestionList.get(formQuestionList.size() - 1);
        assertThat(testFormQuestion.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testFormQuestion.isIsMandatory()).isEqualTo(UPDATED_IS_MANDATORY);
        assertThat(testFormQuestion.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFormQuestion.getOptions()).isEqualTo(UPDATED_OPTIONS);
    }

    @Test
    public void updateNonExistingFormQuestion() throws Exception {
        int databaseSizeBeforeUpdate = formQuestionRepository.findAll().size();

        // Create the FormQuestion

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormQuestionMockMvc.perform(put("/api/form-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formQuestion)))
            .andExpect(status().isBadRequest());

        // Validate the FormQuestion in the database
        List<FormQuestion> formQuestionList = formQuestionRepository.findAll();
        assertThat(formQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteFormQuestion() throws Exception {
        // Initialize the database
        formQuestionService.save(formQuestion);

        int databaseSizeBeforeDelete = formQuestionRepository.findAll().size();

        // Delete the formQuestion
        restFormQuestionMockMvc.perform(delete("/api/form-questions/{id}", formQuestion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormQuestion> formQuestionList = formQuestionRepository.findAll();
        assertThat(formQuestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
