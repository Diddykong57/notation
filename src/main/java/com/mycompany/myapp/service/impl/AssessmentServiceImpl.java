package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Assessment;
import com.mycompany.myapp.repository.AssessmentRepository;
import com.mycompany.myapp.service.AssessmentService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Assessment}.
 */
@Service
@Transactional
public class AssessmentServiceImpl implements AssessmentService {

    private final Logger log = LoggerFactory.getLogger(AssessmentServiceImpl.class);

    private final AssessmentRepository assessmentRepository;

    public AssessmentServiceImpl(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    public Assessment save(Assessment assessment) {
        log.debug("Request to save Assessment : {}", assessment);
        return assessmentRepository.save(assessment);
    }

    @Override
    public Optional<Assessment> partialUpdate(Assessment assessment) {
        log.debug("Request to partially update Assessment : {}", assessment);

        return assessmentRepository
            .findById(assessment.getId())
            .map(
                existingAssessment -> {
                    if (assessment.getDate() != null) {
                        existingAssessment.setDate(assessment.getDate());
                    }
                    if (assessment.getCoefCont() != null) {
                        existingAssessment.setCoefCont(assessment.getCoefCont());
                    }
                    if (assessment.getType() != null) {
                        existingAssessment.setType(assessment.getType());
                    }

                    return existingAssessment;
                }
            )
            .map(assessmentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Assessment> findAll(Pageable pageable) {
        log.debug("Request to get all Assessments");
        return assessmentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Assessment> findOne(Long id) {
        log.debug("Request to get Assessment : {}", id);
        return assessmentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Assessment : {}", id);
        assessmentRepository.deleteById(id);
    }
}
