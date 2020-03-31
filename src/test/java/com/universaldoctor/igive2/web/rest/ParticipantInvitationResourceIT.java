package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.Igive2App;
import com.universaldoctor.igive2.domain.ParticipantInvitation;
import com.universaldoctor.igive2.repository.ParticipantInvitationRepository;
import com.universaldoctor.igive2.service.ParticipantInvitationService;
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
 * Integration tests for the {@link ParticipantInvitationResource} REST controller.
 */
@SpringBootTest(classes = Igive2App.class)
public class ParticipantInvitationResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATE = false;
    private static final Boolean UPDATED_STATE = true;

    private static final String DEFAULT_PARTICIPANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PARTICIPANT_ID = "BBBBBBBBBB";

    @Autowired
    private ParticipantInvitationRepository participantInvitationRepository;

    @Autowired
    private ParticipantInvitationService participantInvitationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restParticipantInvitationMockMvc;

    private ParticipantInvitation participantInvitation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParticipantInvitationResource participantInvitationResource = new ParticipantInvitationResource(participantInvitationService);
        this.restParticipantInvitationMockMvc = MockMvcBuilders.standaloneSetup(participantInvitationResource)
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
    public static ParticipantInvitation createEntity() {
        ParticipantInvitation participantInvitation = new ParticipantInvitation()
            .email(DEFAULT_EMAIL)
            .state(DEFAULT_STATE)
            .participantId(DEFAULT_PARTICIPANT_ID);
        return participantInvitation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParticipantInvitation createUpdatedEntity() {
        ParticipantInvitation participantInvitation = new ParticipantInvitation()
            .email(UPDATED_EMAIL)
            .state(UPDATED_STATE)
            .participantId(UPDATED_PARTICIPANT_ID);
        return participantInvitation;
    }

    @BeforeEach
    public void initTest() {
        participantInvitationRepository.deleteAll();
        participantInvitation = createEntity();
    }

    @Test
    public void createParticipantInvitation() throws Exception {
        int databaseSizeBeforeCreate = participantInvitationRepository.findAll().size();

        // Create the ParticipantInvitation
        restParticipantInvitationMockMvc.perform(post("/api/participant-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participantInvitation)))
            .andExpect(status().isCreated());

        // Validate the ParticipantInvitation in the database
        List<ParticipantInvitation> participantInvitationList = participantInvitationRepository.findAll();
        assertThat(participantInvitationList).hasSize(databaseSizeBeforeCreate + 1);
        ParticipantInvitation testParticipantInvitation = participantInvitationList.get(participantInvitationList.size() - 1);
        assertThat(testParticipantInvitation.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testParticipantInvitation.isState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    public void createParticipantInvitationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = participantInvitationRepository.findAll().size();

        // Create the ParticipantInvitation with an existing ID
        participantInvitation.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipantInvitationMockMvc.perform(post("/api/participant-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participantInvitation)))
            .andExpect(status().isBadRequest());

        // Validate the ParticipantInvitation in the database
        List<ParticipantInvitation> participantInvitationList = participantInvitationRepository.findAll();
        assertThat(participantInvitationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = participantInvitationRepository.findAll().size();
        // set the field null
        participantInvitation.setEmail(null);

        // Create the ParticipantInvitation, which fails.

        restParticipantInvitationMockMvc.perform(post("/api/participant-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participantInvitation)))
            .andExpect(status().isBadRequest());

        List<ParticipantInvitation> participantInvitationList = participantInvitationRepository.findAll();
        assertThat(participantInvitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllParticipantInvitations() throws Exception {
        // Initialize the database
        participantInvitationRepository.save(participantInvitation);

        // Get all the participantInvitationList
        restParticipantInvitationMockMvc.perform(get("/api/participant-invitations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participantInvitation.getId())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.booleanValue())));
    }

    @Test
    public void getParticipantInvitation() throws Exception {
        // Initialize the database
        participantInvitationRepository.save(participantInvitation);

        // Get the participantInvitation
        restParticipantInvitationMockMvc.perform(get("/api/participant-invitations/{id}", participantInvitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(participantInvitation.getId()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.booleanValue()));
    }

    @Test
    public void getNonExistingParticipantInvitation() throws Exception {
        // Get the participantInvitation
        restParticipantInvitationMockMvc.perform(get("/api/participant-invitations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateParticipantInvitation() throws Exception {
        // Initialize the database
        participantInvitationService.save(participantInvitation);

        int databaseSizeBeforeUpdate = participantInvitationRepository.findAll().size();

        // Update the participantInvitation
        ParticipantInvitation updatedParticipantInvitation = participantInvitationRepository.findById(participantInvitation.getId()).get();
        updatedParticipantInvitation
            .email(UPDATED_EMAIL)
            .state(UPDATED_STATE)
            .participantId(UPDATED_PARTICIPANT_ID);

        restParticipantInvitationMockMvc.perform(put("/api/participant-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParticipantInvitation)))
            .andExpect(status().isOk());

        // Validate the ParticipantInvitation in the database
        List<ParticipantInvitation> participantInvitationList = participantInvitationRepository.findAll();
        assertThat(participantInvitationList).hasSize(databaseSizeBeforeUpdate);
        ParticipantInvitation testParticipantInvitation = participantInvitationList.get(participantInvitationList.size() - 1);
        assertThat(testParticipantInvitation.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testParticipantInvitation.isState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    public void updateNonExistingParticipantInvitation() throws Exception {
        int databaseSizeBeforeUpdate = participantInvitationRepository.findAll().size();

        // Create the ParticipantInvitation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipantInvitationMockMvc.perform(put("/api/participant-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participantInvitation)))
            .andExpect(status().isBadRequest());

        // Validate the ParticipantInvitation in the database
        List<ParticipantInvitation> participantInvitationList = participantInvitationRepository.findAll();
        assertThat(participantInvitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteParticipantInvitation() throws Exception {
        // Initialize the database
        participantInvitationService.save(participantInvitation);

        int databaseSizeBeforeDelete = participantInvitationRepository.findAll().size();

        // Delete the participantInvitation
        restParticipantInvitationMockMvc.perform(delete("/api/participant-invitations/{id}", participantInvitation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParticipantInvitation> participantInvitationList = participantInvitationRepository.findAll();
        assertThat(participantInvitationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
