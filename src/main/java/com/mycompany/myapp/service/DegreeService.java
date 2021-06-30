package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Degree;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Degree}.
 */
public interface DegreeService {
    /**
     * Save a degree.
     *
     * @param degree the entity to save.
     * @return the persisted entity.
     */
    Degree save(Degree degree);

    /**
     * Partially updates a degree.
     *
     * @param degree the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Degree> partialUpdate(Degree degree);

    /**
     * Get all the degrees.
     *
     * @return the list of entities.
     */
    List<Degree> findAll();

    /**
     * Get the "id" degree.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Degree> findOne(Long id);

    /**
     * Delete the "id" degree.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
