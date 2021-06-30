package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Assessment;
import com.mycompany.myapp.domain.Get;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.repository.GetRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GetResourceIT {

    private static final Float DEFAULT_NOTE = 0F;
    private static final Float UPDATED_NOTE = 1F;

    private static final String ENTITY_API_URL = "/api/gets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GetRepository getRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGetMockMvc;

    private Get get;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Get createEntity(EntityManager em) {
        Get get = new Get().note(DEFAULT_NOTE);
        // Add required entity
        Assessment assessment;
        if (TestUtil.findAll(em, Assessment.class).isEmpty()) {
            assessment = AssessmentResourceIT.createEntity(em);
            em.persist(assessment);
            em.flush();
        } else {
            assessment = TestUtil.findAll(em, Assessment.class).get(0);
        }
        get.setAssessment(assessment);
        // Add required entity
        Student student;
        if (TestUtil.findAll(em, Student.class).isEmpty()) {
            student = StudentResourceIT.createEntity(em);
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findAll(em, Student.class).get(0);
        }
        get.setStudent(student);
        return get;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Get createUpdatedEntity(EntityManager em) {
        Get get = new Get().note(UPDATED_NOTE);
        // Add required entity
        Assessment assessment;
        if (TestUtil.findAll(em, Assessment.class).isEmpty()) {
            assessment = AssessmentResourceIT.createUpdatedEntity(em);
            em.persist(assessment);
            em.flush();
        } else {
            assessment = TestUtil.findAll(em, Assessment.class).get(0);
        }
        get.setAssessment(assessment);
        // Add required entity
        Student student;
        if (TestUtil.findAll(em, Student.class).isEmpty()) {
            student = StudentResourceIT.createUpdatedEntity(em);
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findAll(em, Student.class).get(0);
        }
        get.setStudent(student);
        return get;
    }

    @BeforeEach
    public void initTest() {
        get = createEntity(em);
    }

    @Test
    @Transactional
    void createGet() throws Exception {
        int databaseSizeBeforeCreate = getRepository.findAll().size();
        // Create the Get
        restGetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(get)))
            .andExpect(status().isCreated());

        // Validate the Get in the database
        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeCreate + 1);
        Get testGet = getList.get(getList.size() - 1);
        assertThat(testGet.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createGetWithExistingId() throws Exception {
        // Create the Get with an existing ID
        get.setId(1L);

        int databaseSizeBeforeCreate = getRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(get)))
            .andExpect(status().isBadRequest());

        // Validate the Get in the database
        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = getRepository.findAll().size();
        // set the field null
        get.setNote(null);

        // Create the Get, which fails.

        restGetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(get)))
            .andExpect(status().isBadRequest());

        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGets() throws Exception {
        // Initialize the database
        getRepository.saveAndFlush(get);

        // Get all the getList
        restGetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(get.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())));
    }

    @Test
    @Transactional
    void getGet() throws Exception {
        // Initialize the database
        getRepository.saveAndFlush(get);

        // Get the get
        restGetMockMvc
            .perform(get(ENTITY_API_URL_ID, get.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(get.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingGet() throws Exception {
        // Get the get
        restGetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGet() throws Exception {
        // Initialize the database
        getRepository.saveAndFlush(get);

        int databaseSizeBeforeUpdate = getRepository.findAll().size();

        // Update the get
        Get updatedGet = getRepository.findById(get.getId()).get();
        // Disconnect from session so that the updates on updatedGet are not directly saved in db
        em.detach(updatedGet);
        updatedGet.note(UPDATED_NOTE);

        restGetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGet))
            )
            .andExpect(status().isOk());

        // Validate the Get in the database
        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeUpdate);
        Get testGet = getList.get(getList.size() - 1);
        assertThat(testGet.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingGet() throws Exception {
        int databaseSizeBeforeUpdate = getRepository.findAll().size();
        get.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, get.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(get))
            )
            .andExpect(status().isBadRequest());

        // Validate the Get in the database
        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGet() throws Exception {
        int databaseSizeBeforeUpdate = getRepository.findAll().size();
        get.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(get))
            )
            .andExpect(status().isBadRequest());

        // Validate the Get in the database
        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGet() throws Exception {
        int databaseSizeBeforeUpdate = getRepository.findAll().size();
        get.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(get)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Get in the database
        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGetWithPatch() throws Exception {
        // Initialize the database
        getRepository.saveAndFlush(get);

        int databaseSizeBeforeUpdate = getRepository.findAll().size();

        // Update the get using partial update
        Get partialUpdatedGet = new Get();
        partialUpdatedGet.setId(get.getId());

        partialUpdatedGet.note(UPDATED_NOTE);

        restGetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGet))
            )
            .andExpect(status().isOk());

        // Validate the Get in the database
        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeUpdate);
        Get testGet = getList.get(getList.size() - 1);
        assertThat(testGet.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateGetWithPatch() throws Exception {
        // Initialize the database
        getRepository.saveAndFlush(get);

        int databaseSizeBeforeUpdate = getRepository.findAll().size();

        // Update the get using partial update
        Get partialUpdatedGet = new Get();
        partialUpdatedGet.setId(get.getId());

        partialUpdatedGet.note(UPDATED_NOTE);

        restGetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGet))
            )
            .andExpect(status().isOk());

        // Validate the Get in the database
        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeUpdate);
        Get testGet = getList.get(getList.size() - 1);
        assertThat(testGet.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingGet() throws Exception {
        int databaseSizeBeforeUpdate = getRepository.findAll().size();
        get.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, get.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(get))
            )
            .andExpect(status().isBadRequest());

        // Validate the Get in the database
        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGet() throws Exception {
        int databaseSizeBeforeUpdate = getRepository.findAll().size();
        get.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(get))
            )
            .andExpect(status().isBadRequest());

        // Validate the Get in the database
        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGet() throws Exception {
        int databaseSizeBeforeUpdate = getRepository.findAll().size();
        get.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(get)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Get in the database
        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGet() throws Exception {
        // Initialize the database
        getRepository.saveAndFlush(get);

        int databaseSizeBeforeDelete = getRepository.findAll().size();

        // Delete the get
        restGetMockMvc.perform(delete(ENTITY_API_URL_ID, get.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Get> getList = getRepository.findAll();
        assertThat(getList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
