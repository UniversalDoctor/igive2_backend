package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.Igive2App;
import com.universaldoctor.igive2.domain.Participant;
import com.universaldoctor.igive2.repository.ParticipantRepository;
import com.universaldoctor.igive2.service.MobileUserService;
import com.universaldoctor.igive2.service.ParticipantService;
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


import javax.validation.constraints.NotNull;
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

/**
 * Integration tests for the {@link ParticipantResource} REST controller.
 */
@SpringBootTest(classes = Igive2App.class)
public class ParticipantResourceIT {

    private static final @NotNull Instant DEFAULT_ENTRY_DATE = Instant.ofEpochMilli(0L);
    private static final @NotNull Instant UPDATED_ENTRY_DATE = Instant.now();

    private static final String DEFAULT_ANONYMOUS_ID = "AAAAAAAAAA";
    private static final String UPDATED_ANONYMOUS_ID = "BBBBBBBBBB";

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    @Autowired
    MobileUserService mobileUserService;

    private MockMvc restParticipantMockMvc;

    private Participant participant;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParticipantResource participantResource = new ParticipantResource(participantService, mobileUserService);
        this.restParticipantMockMvc = MockMvcBuilders.standaloneSetup(participantResource)
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
    public static Participant createEntity() {
        Participant participant = new Participant()
            .entryDate(DEFAULT_ENTRY_DATE)
            .anonymousId(DEFAULT_ANONYMOUS_ID);
        return participant;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participant createUpdatedEntity() {
        Participant participant = new Participant()
            .entryDate(UPDATED_ENTRY_DATE)
            .anonymousId(UPDATED_ANONYMOUS_ID);
        return participant;
    }

    @BeforeEach
    public void initTest() {
        participantRepository.deleteAll();
        participant = createEntity();
    }

    @Test
    public void createParticipant() throws Exception {
        int databaseSizeBeforeCreate = participantRepository.findAll().size();

        // Create the Participant
        restParticipantMockMvc.perform(post("/api/participants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participant)))
            .andExpect(status().isCreated());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeCreate + 1);
        Participant testParticipant = participantList.get(participantList.size() - 1);
        assertThat(testParticipant.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testParticipant.getAnonymousId()).isEqualTo(DEFAULT_ANONYMOUS_ID);
    }

    @Test
    public void createParticipantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = participantRepository.findAll().size();

        // Create the Participant with an existing ID
        participant.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipantMockMvc.perform(post("/api/participants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participant)))
            .andExpect(status().isBadRequest());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkEntryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = participantRepository.findAll().size();
        // set the field null
        participant.setEntryDate(null);

        // Create the Participant, which fails.

        restParticipantMockMvc.perform(post("/api/participants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participant)))
            .andExpect(status().isBadRequest());

        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkAnonymousIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = participantRepository.findAll().size();
        // set the field null
        participant.setAnonymousId(null);

        // Create the Participant, which fails.

        restParticipantMockMvc.perform(post("/api/participants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participant)))
            .andExpect(status().isBadRequest());

        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllParticipants() throws Exception {
        // Initialize the database
        participantRepository.save(participant);

        // Get all the participantList
        restParticipantMockMvc.perform(get("/api/participants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participant.getId())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].anonymousId").value(hasItem(DEFAULT_ANONYMOUS_ID)));
    }

    @Test
    public void getParticipant() throws Exception {
        // Initialize the database
        participantRepository.save(participant);

        // Get the participant
        restParticipantMockMvc.perform(get("/api/participants/{id}", participant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(participant.getId()))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()))
            .andExpect(jsonPath("$.anonymousId").value(DEFAULT_ANONYMOUS_ID));
    }

    @Test
    public void getNonExistingParticipant() throws Exception {
        // Get the participant
        restParticipantMockMvc.perform(get("/api/participants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateParticipant() throws Exception {
        // Initialize the database
        participantService.save(participant);

        int databaseSizeBeforeUpdate = participantRepository.findAll().size();

        // Update the participant
        Participant updatedParticipant = participantRepository.findById(participant.getId()).get();
        updatedParticipant
            .entryDate(UPDATED_ENTRY_DATE)
            .anonymousId(UPDATED_ANONYMOUS_ID);

        restParticipantMockMvc.perform(put("/api/participants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParticipant)))
            .andExpect(status().isOk());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeUpdate);
        Participant testParticipant = participantList.get(participantList.size() - 1);
        assertThat(testParticipant.getAnonymousId()).isEqualTo(UPDATED_ANONYMOUS_ID);
    }

    @Test
    public void updateNonExistingParticipant() throws Exception {
        int databaseSizeBeforeUpdate = participantRepository.findAll().size();

        // Create the Participant

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipantMockMvc.perform(put("/api/participants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participant)))
            .andExpect(status().isBadRequest());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteParticipant() throws Exception {
        // Initialize the database
        participantService.save(participant);

        int databaseSizeBeforeDelete = participantRepository.findAll().size();

        // Delete the participant
        restParticipantMockMvc.perform(delete("/api/participants/{id}", participant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
