package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Assessment;
import com.mycompany.myapp.domain.Subject;
import com.mycompany.myapp.domain.enumeration.TypeAssessment;
import com.mycompany.myapp.repository.AssessmentRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AssessmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssessmentResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_COEF_CONT = 1;
    private static final Integer UPDATED_COEF_CONT = 2;

    private static final TypeAssessment DEFAULT_TYPE = TypeAssessment.CE;
    private static final TypeAssessment UPDATED_TYPE = TypeAssessment.CO;

    private static final String ENTITY_API_URL = "/api/assessments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssessmentMockMvc;

    private Assessment assessment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assessment createEntity(EntityManager em) {
        Assessment assessment = new Assessment().date(DEFAULT_DATE).coefCont(DEFAULT_COEF_CONT).type(DEFAULT_TYPE);
        // Add required entity
        Subject subject;
        if (TestUtil.findAll(em, Subject.class).isEmpty()) {
            subject = SubjectResourceIT.createEntity(em);
            em.persist(subject);
            em.flush();
        } else {
            subject = TestUtil.findAll(em, Subject.class).get(0);
        }
        assessment.setSubject(subject);
        return assessment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assessment createUpdatedEntity(EntityManager em) {
        Assessment assessment = new Assessment().date(UPDATED_DATE).coefCont(UPDATED_COEF_CONT).type(UPDATED_TYPE);
        // Add required entity
        Subject subject;
        if (TestUtil.findAll(em, Subject.class).isEmpty()) {
            subject = SubjectResourceIT.createUpdatedEntity(em);
            em.persist(subject);
            em.flush();
        } else {
            subject = TestUtil.findAll(em, Subject.class).get(0);
        }
        assessment.setSubject(subject);
        return assessment;
    }

    @BeforeEach
    public void initTest() {
        assessment = createEntity(em);
    }

    @Test
    @Transactional
    void createAssessment() throws Exception {
        int databaseSizeBeforeCreate = assessmentRepository.findAll().size();
        // Create the Assessment
        restAssessmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assessment)))
            .andExpect(status().isCreated());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeCreate + 1);
        Assessment testAssessment = assessmentList.get(assessmentList.size() - 1);
        assertThat(testAssessment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAssessment.getCoefCont()).isEqualTo(DEFAULT_COEF_CONT);
        assertThat(testAssessment.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createAssessmentWithExistingId() throws Exception {
        // Create the Assessment with an existing ID
        assessment.setId(1L);

        int databaseSizeBeforeCreate = assessmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssessmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assessment)))
            .andExpect(status().isBadRequest());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAssessments() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        // Get all the assessmentList
        restAssessmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assessment.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].coefCont").value(hasItem(DEFAULT_COEF_CONT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getAssessment() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        // Get the assessment
        restAssessmentMockMvc
            .perform(get(ENTITY_API_URL_ID, assessment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assessment.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.coefCont").value(DEFAULT_COEF_CONT))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAssessment() throws Exception {
        // Get the assessment
        restAssessmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssessment() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();

        // Update the assessment
        Assessment updatedAssessment = assessmentRepository.findById(assessment.getId()).get();
        // Disconnect from session so that the updates on updatedAssessment are not directly saved in db
        em.detach(updatedAssessment);
        updatedAssessment.date(UPDATED_DATE).coefCont(UPDATED_COEF_CONT).type(UPDATED_TYPE);

        restAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAssessment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAssessment))
            )
            .andExpect(status().isOk());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
        Assessment testAssessment = assessmentList.get(assessmentList.size() - 1);
        assertThat(testAssessment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAssessment.getCoefCont()).isEqualTo(UPDATED_COEF_CONT);
        assertThat(testAssessment.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();
        assessment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assessment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();
        assessment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();
        assessment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assessment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssessmentWithPatch() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();

        // Update the assessment using partial update
        Assessment partialUpdatedAssessment = new Assessment();
        partialUpdatedAssessment.setId(assessment.getId());

        partialUpdatedAssessment.date(UPDATED_DATE).coefCont(UPDATED_COEF_CONT).type(UPDATED_TYPE);

        restAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssessment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssessment))
            )
            .andExpect(status().isOk());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
        Assessment testAssessment = assessmentList.get(assessmentList.size() - 1);
        assertThat(testAssessment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAssessment.getCoefCont()).isEqualTo(UPDATED_COEF_CONT);
        assertThat(testAssessment.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAssessmentWithPatch() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();

        // Update the assessment using partial update
        Assessment partialUpdatedAssessment = new Assessment();
        partialUpdatedAssessment.setId(assessment.getId());

        partialUpdatedAssessment.date(UPDATED_DATE).coefCont(UPDATED_COEF_CONT).type(UPDATED_TYPE);

        restAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssessment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssessment))
            )
            .andExpect(status().isOk());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
        Assessment testAssessment = assessmentList.get(assessmentList.size() - 1);
        assertThat(testAssessment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAssessment.getCoefCont()).isEqualTo(UPDATED_COEF_CONT);
        assertThat(testAssessment.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();
        assessment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assessment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();
        assessment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();
        assessment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assessment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssessment() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        int databaseSizeBeforeDelete = assessmentRepository.findAll().size();

        // Delete the assessment
        restAssessmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, assessment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
