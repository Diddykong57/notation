package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Degree;
import com.mycompany.myapp.repository.DegreeRepository;
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
 * Integration tests for the {@link DegreeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DegreeResourceIT {

    private static final String DEFAULT_NAME_DIPL = "AAAAAAAAAA";
    private static final String UPDATED_NAME_DIPL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/degrees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DegreeRepository degreeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDegreeMockMvc;

    private Degree degree;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Degree createEntity(EntityManager em) {
        Degree degree = new Degree().nameDipl(DEFAULT_NAME_DIPL);
        return degree;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Degree createUpdatedEntity(EntityManager em) {
        Degree degree = new Degree().nameDipl(UPDATED_NAME_DIPL);
        return degree;
    }

    @BeforeEach
    public void initTest() {
        degree = createEntity(em);
    }

    @Test
    @Transactional
    void createDegree() throws Exception {
        int databaseSizeBeforeCreate = degreeRepository.findAll().size();
        // Create the Degree
        restDegreeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(degree)))
            .andExpect(status().isCreated());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeCreate + 1);
        Degree testDegree = degreeList.get(degreeList.size() - 1);
        assertThat(testDegree.getNameDipl()).isEqualTo(DEFAULT_NAME_DIPL);
    }

    @Test
    @Transactional
    void createDegreeWithExistingId() throws Exception {
        // Create the Degree with an existing ID
        degree.setId(1L);

        int databaseSizeBeforeCreate = degreeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDegreeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(degree)))
            .andExpect(status().isBadRequest());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDegrees() throws Exception {
        // Initialize the database
        degreeRepository.saveAndFlush(degree);

        // Get all the degreeList
        restDegreeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(degree.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameDipl").value(hasItem(DEFAULT_NAME_DIPL)));
    }

    @Test
    @Transactional
    void getDegree() throws Exception {
        // Initialize the database
        degreeRepository.saveAndFlush(degree);

        // Get the degree
        restDegreeMockMvc
            .perform(get(ENTITY_API_URL_ID, degree.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(degree.getId().intValue()))
            .andExpect(jsonPath("$.nameDipl").value(DEFAULT_NAME_DIPL));
    }

    @Test
    @Transactional
    void getNonExistingDegree() throws Exception {
        // Get the degree
        restDegreeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDegree() throws Exception {
        // Initialize the database
        degreeRepository.saveAndFlush(degree);

        int databaseSizeBeforeUpdate = degreeRepository.findAll().size();

        // Update the degree
        Degree updatedDegree = degreeRepository.findById(degree.getId()).get();
        // Disconnect from session so that the updates on updatedDegree are not directly saved in db
        em.detach(updatedDegree);
        updatedDegree.nameDipl(UPDATED_NAME_DIPL);

        restDegreeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDegree.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDegree))
            )
            .andExpect(status().isOk());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeUpdate);
        Degree testDegree = degreeList.get(degreeList.size() - 1);
        assertThat(testDegree.getNameDipl()).isEqualTo(UPDATED_NAME_DIPL);
    }

    @Test
    @Transactional
    void putNonExistingDegree() throws Exception {
        int databaseSizeBeforeUpdate = degreeRepository.findAll().size();
        degree.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDegreeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, degree.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(degree))
            )
            .andExpect(status().isBadRequest());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDegree() throws Exception {
        int databaseSizeBeforeUpdate = degreeRepository.findAll().size();
        degree.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDegreeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(degree))
            )
            .andExpect(status().isBadRequest());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDegree() throws Exception {
        int databaseSizeBeforeUpdate = degreeRepository.findAll().size();
        degree.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDegreeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(degree)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDegreeWithPatch() throws Exception {
        // Initialize the database
        degreeRepository.saveAndFlush(degree);

        int databaseSizeBeforeUpdate = degreeRepository.findAll().size();

        // Update the degree using partial update
        Degree partialUpdatedDegree = new Degree();
        partialUpdatedDegree.setId(degree.getId());

        partialUpdatedDegree.nameDipl(UPDATED_NAME_DIPL);

        restDegreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDegree.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDegree))
            )
            .andExpect(status().isOk());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeUpdate);
        Degree testDegree = degreeList.get(degreeList.size() - 1);
        assertThat(testDegree.getNameDipl()).isEqualTo(UPDATED_NAME_DIPL);
    }

    @Test
    @Transactional
    void fullUpdateDegreeWithPatch() throws Exception {
        // Initialize the database
        degreeRepository.saveAndFlush(degree);

        int databaseSizeBeforeUpdate = degreeRepository.findAll().size();

        // Update the degree using partial update
        Degree partialUpdatedDegree = new Degree();
        partialUpdatedDegree.setId(degree.getId());

        partialUpdatedDegree.nameDipl(UPDATED_NAME_DIPL);

        restDegreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDegree.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDegree))
            )
            .andExpect(status().isOk());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeUpdate);
        Degree testDegree = degreeList.get(degreeList.size() - 1);
        assertThat(testDegree.getNameDipl()).isEqualTo(UPDATED_NAME_DIPL);
    }

    @Test
    @Transactional
    void patchNonExistingDegree() throws Exception {
        int databaseSizeBeforeUpdate = degreeRepository.findAll().size();
        degree.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDegreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, degree.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(degree))
            )
            .andExpect(status().isBadRequest());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDegree() throws Exception {
        int databaseSizeBeforeUpdate = degreeRepository.findAll().size();
        degree.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDegreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(degree))
            )
            .andExpect(status().isBadRequest());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDegree() throws Exception {
        int databaseSizeBeforeUpdate = degreeRepository.findAll().size();
        degree.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDegreeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(degree)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDegree() throws Exception {
        // Initialize the database
        degreeRepository.saveAndFlush(degree);

        int databaseSizeBeforeDelete = degreeRepository.findAll().size();

        // Delete the degree
        restDegreeMockMvc
            .perform(delete(ENTITY_API_URL_ID, degree.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
