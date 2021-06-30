package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Subject;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Subject}.
 */
public interface SubjectService {
    /**
     * Save a subject.
     *
     * @param subject the entity to save.
     * @return the persisted entity.
     */
    Subject save(Subject subject);

    /**
     * Partially updates a subject.
     *
     * @param subject the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Subject> partialUpdate(Subject subject);

    /**
     * Get all the subjects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Subject> findAll(Pageable pageable);

    /**
     * Get the "id" subject.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Subject> findOne(Long id);

    /**
     * Delete the "id" subject.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
