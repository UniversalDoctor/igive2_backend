package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.Igive2App;
import com.universaldoctor.igive2.domain.ParticipantInstitution;
import com.universaldoctor.igive2.repository.ParticipantInstitutionRepository;
import com.universaldoctor.igive2.service.ParticipantInstitutionService;
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


import java.util.List;

import static com.universaldoctor.igive2.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ParticipantInstitutionResource} REST controller.
 */
@SpringBootTest(classes = Igive2App.class)
public class ParticipantInstitutionResourceIT {

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    @Autowired
    private ParticipantInstitutionRepository participantInstitutionRepository;

    @Autowired
    private ParticipantInstitutionService participantInstitutionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    StudyService studyService;

    @Autowired
    private Validator validator;

    private MockMvc restParticipantInstitutionMockMvc;

    private ParticipantInstitution participantInstitution;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParticipantInstitutionResource participantInstitutionResource = new ParticipantInstitutionResource(participantInstitutionService, studyService);
        this.restParticipantInstitutionMockMvc = MockMvcBuilders.standaloneSetup(participantInstitutionResource)
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
    public static ParticipantInstitution createEntity() {
        ParticipantInstitution participantInstitution = new ParticipantInstitution()
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        return participantInstitution;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParticipantInstitution createUpdatedEntity() {
        ParticipantInstitution participantInstitution = new ParticipantInstitution()
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);
        return participantInstitution;
    }

    @BeforeEach
    public void initTest() {
        participantInstitutionRepository.deleteAll();
        participantInstitution = createEntity();
    }

    @Test
    public void createParticipantInstitution() throws Exception {
        int databaseSizeBeforeCreate = participantInstitutionRepository.findAll().size();

        // Create the ParticipantInstitution
        restParticipantInstitutionMockMvc.perform(post("/api/participant-institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participantInstitution)))
            .andExpect(status().isCreated());

        // Validate the ParticipantInstitution in the database
        List<ParticipantInstitution> participantInstitutionList = participantInstitutionRepository.findAll();
        assertThat(participantInstitutionList).hasSize(databaseSizeBeforeCreate + 1);
        ParticipantInstitution testParticipantInstitution = participantInstitutionList.get(participantInstitutionList.size() - 1);
        assertThat(testParticipantInstitution.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testParticipantInstitution.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    public void createParticipantInstitutionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = participantInstitutionRepository.findAll().size();

        // Create the ParticipantInstitution with an existing ID
        participantInstitution.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipantInstitutionMockMvc.perform(post("/api/participant-institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participantInstitution)))
            .andExpect(status().isBadRequest());

        // Validate the ParticipantInstitution in the database
        List<ParticipantInstitution> participantInstitutionList = participantInstitutionRepository.findAll();
        assertThat(participantInstitutionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllParticipantInstitutions() throws Exception {
        // Initialize the database
        participantInstitutionRepository.save(participantInstitution);

        // Get all the participantInstitutionList
        restParticipantInstitutionMockMvc.perform(get("/api/participant-institutions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participantInstitution.getId())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));
    }

    @Test
    public void getParticipantInstitution() throws Exception {
        // Initialize the database
        participantInstitutionRepository.save(participantInstitution);

        // Get the participantInstitution
        restParticipantInstitutionMockMvc.perform(get("/api/participant-institutions/{id}", participantInstitution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(participantInstitution.getId()))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)));
    }

    @Test
    public void getNonExistingParticipantInstitution() throws Exception {
        // Get the participantInstitution
        restParticipantInstitutionMockMvc.perform(get("/api/participant-institutions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateParticipantInstitution() throws Exception {
        // Initialize the database
        participantInstitutionService.save(participantInstitution);

        int databaseSizeBeforeUpdate = participantInstitutionRepository.findAll().size();

        // Update the participantInstitution
        ParticipantInstitution updatedParticipantInstitution = participantInstitutionRepository.findById(participantInstitution.getId()).get();
        updatedParticipantInstitution
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restParticipantInstitutionMockMvc.perform(put("/api/participant-institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParticipantInstitution)))
            .andExpect(status().isOk());

        // Validate the ParticipantInstitution in the database
        List<ParticipantInstitution> participantInstitutionList = participantInstitutionRepository.findAll();
        assertThat(participantInstitutionList).hasSize(databaseSizeBeforeUpdate);
        ParticipantInstitution testParticipantInstitution = participantInstitutionList.get(participantInstitutionList.size() - 1);
        assertThat(testParticipantInstitution.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testParticipantInstitution.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    public void updateNonExistingParticipantInstitution() throws Exception {
        int databaseSizeBeforeUpdate = participantInstitutionRepository.findAll().size();

        // Create the ParticipantInstitution

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipantInstitutionMockMvc.perform(put("/api/participant-institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participantInstitution)))
            .andExpect(status().isBadRequest());

        // Validate the ParticipantInstitution in the database
        List<ParticipantInstitution> participantInstitutionList = participantInstitutionRepository.findAll();
        assertThat(participantInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteParticipantInstitution() throws Exception {
        // Initialize the database
        participantInstitutionService.save(participantInstitution);

        int databaseSizeBeforeDelete = participantInstitutionRepository.findAll().size();

        // Delete the participantInstitution
        restParticipantInstitutionMockMvc.perform(delete("/api/participant-institutions/{id}", participantInstitution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParticipantInstitution> participantInstitutionList = participantInstitutionRepository.findAll();
        assertThat(participantInstitutionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
