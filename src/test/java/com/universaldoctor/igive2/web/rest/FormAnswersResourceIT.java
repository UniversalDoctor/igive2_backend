package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.Igive2App;
import com.universaldoctor.igive2.domain.FormAnswers;
import com.universaldoctor.igive2.repository.FormAnswersRepository;
import com.universaldoctor.igive2.service.FormAnswersService;
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


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.universaldoctor.igive2.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FormAnswersResource} REST controller.
 */
@SpringBootTest(classes = Igive2App.class)
public class FormAnswersResourceIT {

    private static final Instant DEFAULT_SAVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SAVED_DATE = Instant.now();

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    @Autowired
    private FormAnswersRepository formAnswersRepository;

    @Autowired
    private FormAnswersService formAnswersService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restFormAnswersMockMvc;

    private FormAnswers formAnswers;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormAnswersResource formAnswersResource = new FormAnswersResource(formAnswersService);
        this.restFormAnswersMockMvc = MockMvcBuilders.standaloneSetup(formAnswersResource)
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
    public static FormAnswers createEntity() {
        FormAnswers formAnswers = new FormAnswers()
            .savedDate(DEFAULT_SAVED_DATE)
            .completed(DEFAULT_COMPLETED);
        return formAnswers;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormAnswers createUpdatedEntity() {
        FormAnswers formAnswers = new FormAnswers()
            .savedDate(UPDATED_SAVED_DATE)
            .completed(UPDATED_COMPLETED);
        return formAnswers;
    }

    @BeforeEach
    public void initTest() {
        formAnswersRepository.deleteAll();
        formAnswers = createEntity();
    }

    @Test
    public void createFormAnswers() throws Exception {
        int databaseSizeBeforeCreate = formAnswersRepository.findAll().size();

        // Create the FormAnswers
        restFormAnswersMockMvc.perform(post("/api/form-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formAnswers)))
            .andExpect(status().isCreated());

        // Validate the FormAnswers in the database
        List<FormAnswers> formAnswersList = formAnswersRepository.findAll();
        assertThat(formAnswersList).hasSize(databaseSizeBeforeCreate + 1);
        FormAnswers testFormAnswers = formAnswersList.get(formAnswersList.size() - 1);
        assertThat(testFormAnswers.isCompleted()).isEqualTo(DEFAULT_COMPLETED);
    }

    @Test
    public void createFormAnswersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formAnswersRepository.findAll().size();

        // Create the FormAnswers with an existing ID
        formAnswers.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormAnswersMockMvc.perform(post("/api/form-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formAnswers)))
            .andExpect(status().isBadRequest());

        // Validate the FormAnswers in the database
        List<FormAnswers> formAnswersList = formAnswersRepository.findAll();
        assertThat(formAnswersList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllFormAnswers() throws Exception {
        // Initialize the database
        formAnswersRepository.save(formAnswers);

        // Get all the formAnswersList
        restFormAnswersMockMvc.perform(get("/api/form-answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formAnswers.getId())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }

    @Test
    public void getFormAnswers() throws Exception {
        // Initialize the database
        formAnswersRepository.save(formAnswers);

        // Get the formAnswers
        restFormAnswersMockMvc.perform(get("/api/form-answers/{id}", formAnswers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formAnswers.getId()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }

    @Test
    public void getNonExistingFormAnswers() throws Exception {
        // Get the formAnswers
        restFormAnswersMockMvc.perform(get("/api/form-answers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateFormAnswers() throws Exception {
        // Initialize the database
        formAnswersService.save(formAnswers);

        int databaseSizeBeforeUpdate = formAnswersRepository.findAll().size();

        // Update the formAnswers
        FormAnswers updatedFormAnswers = formAnswersRepository.findById(formAnswers.getId()).get();
        updatedFormAnswers
            .savedDate(UPDATED_SAVED_DATE)
            .completed(UPDATED_COMPLETED);

        restFormAnswersMockMvc.perform(put("/api/form-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormAnswers)))
            .andExpect(status().isOk());

        // Validate the FormAnswers in the database
        List<FormAnswers> formAnswersList = formAnswersRepository.findAll();
        assertThat(formAnswersList).hasSize(databaseSizeBeforeUpdate);
        FormAnswers testFormAnswers = formAnswersList.get(formAnswersList.size() - 1);
        assertThat(testFormAnswers.isCompleted()).isEqualTo(UPDATED_COMPLETED);
    }

    @Test
    public void updateNonExistingFormAnswers() throws Exception {
        int databaseSizeBeforeUpdate = formAnswersRepository.findAll().size();

        // Create the FormAnswers

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormAnswersMockMvc.perform(put("/api/form-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formAnswers)))
            .andExpect(status().isBadRequest());

        // Validate the FormAnswers in the database
        List<FormAnswers> formAnswersList = formAnswersRepository.findAll();
        assertThat(formAnswersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteFormAnswers() throws Exception {
        // Initialize the database
        formAnswersService.save(formAnswers);

        int databaseSizeBeforeDelete = formAnswersRepository.findAll().size();

        // Delete the formAnswers
        restFormAnswersMockMvc.perform(delete("/api/form-answers/{id}", formAnswers.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormAnswers> formAnswersList = formAnswersRepository.findAll();
        assertThat(formAnswersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
