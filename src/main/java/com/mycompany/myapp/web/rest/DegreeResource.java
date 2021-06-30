package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Degree;
import com.mycompany.myapp.repository.DegreeRepository;
import com.mycompany.myapp.service.DegreeService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Degree}.
 */
@RestController
@RequestMapping("/api")
public class DegreeResource {

    private final Logger log = LoggerFactory.getLogger(DegreeResource.class);

    private static final String ENTITY_NAME = "degree";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DegreeService degreeService;

    private final DegreeRepository degreeRepository;

    public DegreeResource(DegreeService degreeService, DegreeRepository degreeRepository) {
        this.degreeService = degreeService;
        this.degreeRepository = degreeRepository;
    }

    /**
     * {@code POST  /degrees} : Create a new degree.
     *
     * @param degree the degree to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new degree, or with status {@code 400 (Bad Request)} if the degree has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/degrees")
    public ResponseEntity<Degree> createDegree(@RequestBody Degree degree) throws URISyntaxException {
        log.debug("REST request to save Degree : {}", degree);
        if (degree.getId() != null) {
            throw new BadRequestAlertException("A new degree cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Degree result = degreeService.save(degree);
        return ResponseEntity
            .created(new URI("/api/degrees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /degrees/:id} : Updates an existing degree.
     *
     * @param id the id of the degree to save.
     * @param degree the degree to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated degree,
     * or with status {@code 400 (Bad Request)} if the degree is not valid,
     * or with status {@code 500 (Internal Server Error)} if the degree couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/degrees/{id}")
    public ResponseEntity<Degree> updateDegree(@PathVariable(value = "id", required = false) final Long id, @RequestBody Degree degree)
        throws URISyntaxException {
        log.debug("REST request to update Degree : {}, {}", id, degree);
        if (degree.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, degree.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!degreeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Degree result = degreeService.save(degree);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, degree.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /degrees/:id} : Partial updates given fields of an existing degree, field will ignore if it is null
     *
     * @param id the id of the degree to save.
     * @param degree the degree to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated degree,
     * or with status {@code 400 (Bad Request)} if the degree is not valid,
     * or with status {@code 404 (Not Found)} if the degree is not found,
     * or with status {@code 500 (Internal Server Error)} if the degree couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/degrees/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Degree> partialUpdateDegree(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Degree degree
    ) throws URISyntaxException {
        log.debug("REST request to partial update Degree partially : {}, {}", id, degree);
        if (degree.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, degree.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!degreeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Degree> result = degreeService.partialUpdate(degree);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, degree.getId().toString())
        );
    }

    /**
     * {@code GET  /degrees} : get all the degrees.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of degrees in body.
     */
    @GetMapping("/degrees")
    public List<Degree> getAllDegrees() {
        log.debug("REST request to get all Degrees");
        return degreeService.findAll();
    }

    /**
     * {@code GET  /degrees/:id} : get the "id" degree.
     *
     * @param id the id of the degree to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the degree, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/degrees/{id}")
    public ResponseEntity<Degree> getDegree(@PathVariable Long id) {
        log.debug("REST request to get Degree : {}", id);
        Optional<Degree> degree = degreeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(degree);
    }

    /**
     * {@code DELETE  /degrees/:id} : delete the "id" degree.
     *
     * @param id the id of the degree to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/degrees/{id}")
    public ResponseEntity<Void> deleteDegree(@PathVariable Long id) {
        log.debug("REST request to delete Degree : {}", id);
        degreeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
