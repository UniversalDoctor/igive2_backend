package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.Igive2App;
import com.universaldoctor.igive2.domain.IGive2User;
import com.universaldoctor.igive2.repository.IGive2UserRepository;
import com.universaldoctor.igive2.service.IGive2UserService;
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
 * Integration tests for the {@link IGive2UserResource} REST controller.
 */
@SpringBootTest(classes = Igive2App.class)
public class IGive2UserResourceIT {

    private static final Boolean DEFAULT_NEWSLETTER = false;
    private static final Boolean UPDATED_NEWSLETTER = true;

    private static final Boolean DEFAULT_TERMS_ACCEPTED = false;
    private static final Boolean UPDATED_TERMS_ACCEPTED = true;

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    @Autowired
    private IGive2UserRepository iGive2UserRepository;

    @Autowired
    private IGive2UserService iGive2UserService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restIGive2UserMockMvc;

    private IGive2User iGive2User;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IGive2UserResource iGive2UserResource = new IGive2UserResource(iGive2UserService);
        this.restIGive2UserMockMvc = MockMvcBuilders.standaloneSetup(iGive2UserResource)
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
    public static IGive2User createEntity() {
        IGive2User iGive2User = new IGive2User()
            .newsletter(DEFAULT_NEWSLETTER)
            .termsAccepted(DEFAULT_TERMS_ACCEPTED)
            .country(DEFAULT_COUNTRY);
        return iGive2User;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IGive2User createUpdatedEntity() {
        IGive2User iGive2User = new IGive2User()
            .newsletter(UPDATED_NEWSLETTER)
            .termsAccepted(UPDATED_TERMS_ACCEPTED)
            .country(UPDATED_COUNTRY);
        return iGive2User;
    }

    @BeforeEach
    public void initTest() {
        iGive2UserRepository.deleteAll();
        iGive2User = createEntity();
    }

    @Test
    public void createIGive2User() throws Exception {
        int databaseSizeBeforeCreate = iGive2UserRepository.findAll().size();

        // Create the IGive2User
        restIGive2UserMockMvc.perform(post("/api/i-give-2-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iGive2User)))
            .andExpect(status().isCreated());

        // Validate the IGive2User in the database
        List<IGive2User> iGive2UserList = iGive2UserRepository.findAll();
        assertThat(iGive2UserList).hasSize(databaseSizeBeforeCreate + 1);
        IGive2User testIGive2User = iGive2UserList.get(iGive2UserList.size() - 1);
        assertThat(testIGive2User.isNewsletter()).isEqualTo(DEFAULT_NEWSLETTER);
        assertThat(testIGive2User.isTermsAccepted()).isEqualTo(DEFAULT_TERMS_ACCEPTED);
        assertThat(testIGive2User.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    public void createIGive2UserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = iGive2UserRepository.findAll().size();

        // Create the IGive2User with an existing ID
        iGive2User.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restIGive2UserMockMvc.perform(post("/api/i-give-2-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iGive2User)))
            .andExpect(status().isBadRequest());

        // Validate the IGive2User in the database
        List<IGive2User> iGive2UserList = iGive2UserRepository.findAll();
        assertThat(iGive2UserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNewsletterIsRequired() throws Exception {
        int databaseSizeBeforeTest = iGive2UserRepository.findAll().size();
        // set the field null
        iGive2User.setNewsletter(null);

        // Create the IGive2User, which fails.

        restIGive2UserMockMvc.perform(post("/api/i-give-2-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iGive2User)))
            .andExpect(status().isBadRequest());

        List<IGive2User> iGive2UserList = iGive2UserRepository.findAll();
        assertThat(iGive2UserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTermsAcceptedIsRequired() throws Exception {
        int databaseSizeBeforeTest = iGive2UserRepository.findAll().size();
        // set the field null
        iGive2User.setTermsAccepted(null);

        // Create the IGive2User, which fails.

        restIGive2UserMockMvc.perform(post("/api/i-give-2-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iGive2User)))
            .andExpect(status().isBadRequest());

        List<IGive2User> iGive2UserList = iGive2UserRepository.findAll();
        assertThat(iGive2UserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = iGive2UserRepository.findAll().size();
        // set the field null
        iGive2User.setCountry(null);

        // Create the IGive2User, which fails.

        restIGive2UserMockMvc.perform(post("/api/i-give-2-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iGive2User)))
            .andExpect(status().isBadRequest());

        List<IGive2User> iGive2UserList = iGive2UserRepository.findAll();
        assertThat(iGive2UserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllIGive2Users() throws Exception {
        // Initialize the database
        iGive2UserRepository.save(iGive2User);

        // Get all the iGive2UserList
        restIGive2UserMockMvc.perform(get("/api/i-give-2-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iGive2User.getId())))
            .andExpect(jsonPath("$.[*].newsletter").value(hasItem(DEFAULT_NEWSLETTER.booleanValue())))
            .andExpect(jsonPath("$.[*].termsAccepted").value(hasItem(DEFAULT_TERMS_ACCEPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));
    }
    
    @Test
    public void getIGive2User() throws Exception {
        // Initialize the database
        iGive2UserRepository.save(iGive2User);

        // Get the iGive2User
        restIGive2UserMockMvc.perform(get("/api/i-give-2-users/{id}", iGive2User.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(iGive2User.getId()))
            .andExpect(jsonPath("$.newsletter").value(DEFAULT_NEWSLETTER.booleanValue()))
            .andExpect(jsonPath("$.termsAccepted").value(DEFAULT_TERMS_ACCEPTED.booleanValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    public void getNonExistingIGive2User() throws Exception {
        // Get the iGive2User
        restIGive2UserMockMvc.perform(get("/api/i-give-2-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateIGive2User() throws Exception {
        // Initialize the database
        iGive2UserService.save(iGive2User);

        int databaseSizeBeforeUpdate = iGive2UserRepository.findAll().size();

        // Update the iGive2User
        IGive2User updatedIGive2User = iGive2UserRepository.findById(iGive2User.getId()).get();
        updatedIGive2User
            .newsletter(UPDATED_NEWSLETTER)
            .termsAccepted(UPDATED_TERMS_ACCEPTED)
            .country(UPDATED_COUNTRY);

        restIGive2UserMockMvc.perform(put("/api/i-give-2-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIGive2User)))
            .andExpect(status().isOk());

        // Validate the IGive2User in the database
        List<IGive2User> iGive2UserList = iGive2UserRepository.findAll();
        assertThat(iGive2UserList).hasSize(databaseSizeBeforeUpdate);
        IGive2User testIGive2User = iGive2UserList.get(iGive2UserList.size() - 1);
        assertThat(testIGive2User.isNewsletter()).isEqualTo(UPDATED_NEWSLETTER);
        assertThat(testIGive2User.isTermsAccepted()).isEqualTo(UPDATED_TERMS_ACCEPTED);
        assertThat(testIGive2User.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    public void updateNonExistingIGive2User() throws Exception {
        int databaseSizeBeforeUpdate = iGive2UserRepository.findAll().size();

        // Create the IGive2User

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIGive2UserMockMvc.perform(put("/api/i-give-2-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iGive2User)))
            .andExpect(status().isBadRequest());

        // Validate the IGive2User in the database
        List<IGive2User> iGive2UserList = iGive2UserRepository.findAll();
        assertThat(iGive2UserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteIGive2User() throws Exception {
        // Initialize the database
        iGive2UserService.save(iGive2User);

        int databaseSizeBeforeDelete = iGive2UserRepository.findAll().size();

        // Delete the iGive2User
        restIGive2UserMockMvc.perform(delete("/api/i-give-2-users/{id}", iGive2User.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IGive2User> iGive2UserList = iGive2UserRepository.findAll();
        assertThat(iGive2UserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
