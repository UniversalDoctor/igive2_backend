package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.Igive2App;
import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.repository.MobileUserRepository;
import com.universaldoctor.igive2.service.MobileUserService;
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


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.universaldoctor.igive2.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.universaldoctor.igive2.domain.enumeration.GenderType;
import com.universaldoctor.igive2.domain.enumeration.Diseases;
/**
 * Integration tests for the {@link MobileUserResource} REST controller.
 */
@SpringBootTest(classes = Igive2App.class)
public class MobileUserResourceResourceIT {

    private static final GenderType DEFAULT_GENDER = GenderType.FEMALE;
    private static final GenderType UPDATED_GENDER = GenderType.MALE;

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDATE = LocalDate.now(ZoneId.systemDefault());

    private static final Diseases DEFAULT_DISEASES = Diseases.HYPERTENSION;
    private static final Diseases UPDATED_DISEASES = Diseases.NONE;
    private static final String DEFAULT_USER_ID = "user_id";

    @Autowired
    private MobileUserRepository mobileUserRepository;

    @Autowired
    private MobileUserService mobileUserService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restMobileUserMockMvc;

    private MobileUser mobileUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MobileUserResource mobileUserResource = new MobileUserResource(mobileUserService);
        this.restMobileUserMockMvc = MockMvcBuilders.standaloneSetup(mobileUserResource)
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
    public static MobileUser createEntity() {
        MobileUser mobileUser = new MobileUser()
            .gender(DEFAULT_GENDER)
            .birthdate(DEFAULT_BIRTHDATE)
            .diseases(DEFAULT_DISEASES).userId(DEFAULT_USER_ID);
        return mobileUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MobileUser createUpdatedEntity() {
        MobileUser mobileUser = new MobileUser()
            .gender(UPDATED_GENDER)
            .birthdate(UPDATED_BIRTHDATE)
            .diseases(UPDATED_DISEASES);
        return mobileUser;
    }

    @BeforeEach
    public void initTest() {
        mobileUserRepository.deleteAll();
        mobileUser = createEntity();
    }

    @Test
    public void createMobileUser() throws Exception {
        int databaseSizeBeforeCreate = mobileUserRepository.findAll().size();

        // Create the MobileUser
        restMobileUserMockMvc.perform(post("/api/mobile-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mobileUser)))
            .andExpect(status().isCreated());

        // Validate the MobileUser in the database
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeCreate + 1);
        MobileUser testMobileUser = mobileUserList.get(mobileUserList.size() - 1);
        assertThat(testMobileUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testMobileUser.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testMobileUser.getDiseases()).isEqualTo(DEFAULT_DISEASES);
    }

    @Test
    public void createMobileUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mobileUserRepository.findAll().size();

        // Create the MobileUser with an existing ID
        mobileUser.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restMobileUserMockMvc.perform(post("/api/mobile-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mobileUser)))
            .andExpect(status().isBadRequest());

        // Validate the MobileUser in the database
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileUserRepository.findAll().size();
        // set the field null
        mobileUser.setGender(null);

        // Create the MobileUser, which fails.

        restMobileUserMockMvc.perform(post("/api/mobile-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mobileUser)))
            .andExpect(status().isBadRequest());

        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDiseasesIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileUserRepository.findAll().size();
        // set the field null
        mobileUser.setDiseases(null);

        // Create the MobileUser, which fails.

        restMobileUserMockMvc.perform(post("/api/mobile-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mobileUser)))
            .andExpect(status().isBadRequest());

        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllMobileUsers() throws Exception {
        // Initialize the database
        mobileUserRepository.save(mobileUser);

        // Get all the mobileUserList
        restMobileUserMockMvc.perform(get("/api/mobile-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mobileUser.getId())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].diseases").value(hasItem(DEFAULT_DISEASES.toString())));
    }

    @Test
    public void getMobileUser() throws Exception {
        // Initialize the database
        mobileUserRepository.save(mobileUser);

        // Get the mobileUser
        restMobileUserMockMvc.perform(get("/api/mobile-users/{id}", mobileUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mobileUser.getId()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()))
            .andExpect(jsonPath("$.diseases").value(DEFAULT_DISEASES.toString()));
    }

    @Test
    public void getNonExistingMobileUser() throws Exception {
        // Get the mobileUser
        restMobileUserMockMvc.perform(get("/api/mobile-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateMobileUser() throws Exception {
        // Initialize the database
        mobileUserService.save(mobileUser);

        int databaseSizeBeforeUpdate = mobileUserRepository.findAll().size();

        // Update the mobileUser
        MobileUser updatedMobileUser = mobileUserRepository.findById(mobileUser.getId()).get();
        updatedMobileUser
            .gender(UPDATED_GENDER)
            .birthdate(UPDATED_BIRTHDATE)
            .diseases(UPDATED_DISEASES);

        restMobileUserMockMvc.perform(put("/api/mobile-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMobileUser)))
            .andExpect(status().isOk());

        // Validate the MobileUser in the database
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeUpdate);
        MobileUser testMobileUser = mobileUserList.get(mobileUserList.size() - 1);
        assertThat(testMobileUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testMobileUser.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testMobileUser.getDiseases()).isEqualTo(UPDATED_DISEASES);
    }

    @Test
    public void updateNonExistingMobileUser() throws Exception {
        int databaseSizeBeforeUpdate = mobileUserRepository.findAll().size();

        // Create the MobileUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMobileUserMockMvc.perform(put("/api/mobile-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mobileUser)))
            .andExpect(status().isBadRequest());

        // Validate the MobileUser in the database
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteMobileUser() throws Exception {
        // Initialize the database
        mobileUserService.save(mobileUser);

        int databaseSizeBeforeDelete = mobileUserRepository.findAll().size();

        // Delete the mobileUser
        restMobileUserMockMvc.perform(delete("/api/mobile-users/{id}", mobileUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
