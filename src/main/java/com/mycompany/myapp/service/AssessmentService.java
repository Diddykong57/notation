package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Assessment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Assessment}.
 */
public interface AssessmentService {
    /**
     * Save a assessment.
     *
     * @param assessment the entity to save.
     * @return the persisted entity.
     */
    Assessment save(Assessment assessment);

    /**
     * Partially updates a assessment.
     *
     * @param assessment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Assessment> partialUpdate(Assessment assessment);

    /**
     * Get all the assessments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Assessment> findAll(Pageable pageable);

    /**
     * Get the "id" assessment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Assessment> findOne(Long id);

    /**
     * Delete the "id" assessment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
