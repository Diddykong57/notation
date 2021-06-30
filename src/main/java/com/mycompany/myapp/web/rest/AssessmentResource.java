package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Assessment;
import com.mycompany.myapp.repository.AssessmentRepository;
import com.mycompany.myapp.service.AssessmentService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Assessment}.
 */
@RestController
@RequestMapping("/api")
public class AssessmentResource {

    private final Logger log = LoggerFactory.getLogger(AssessmentResource.class);

    private static final String ENTITY_NAME = "assessment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssessmentService assessmentService;

    private final AssessmentRepository assessmentRepository;

    public AssessmentResource(AssessmentService assessmentService, AssessmentRepository assessmentRepository) {
        this.assessmentService = assessmentService;
        this.assessmentRepository = assessmentRepository;
    }

    /**
     * {@code POST  /assessments} : Create a new assessment.
     *
     * @param assessment the assessment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assessment, or with status {@code 400 (Bad Request)} if the assessment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assessments")
    public ResponseEntity<Assessment> createAssessment(@Valid @RequestBody Assessment assessment) throws URISyntaxException {
        log.debug("REST request to save Assessment : {}", assessment);
        if (assessment.getId() != null) {
            throw new BadRequestAlertException("A new assessment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Assessment result = assessmentService.save(assessment);
        return ResponseEntity
            .created(new URI("/api/assessments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assessments/:id} : Updates an existing assessment.
     *
     * @param id the id of the assessment to save.
     * @param assessment the assessment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assessment,
     * or with status {@code 400 (Bad Request)} if the assessment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assessment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assessments/{id}")
    public ResponseEntity<Assessment> updateAssessment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Assessment assessment
    ) throws URISyntaxException {
        log.debug("REST request to update Assessment : {}, {}", id, assessment);
        if (assessment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assessment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assessmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Assessment result = assessmentService.save(assessment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assessment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /assessments/:id} : Partial updates given fields of an existing assessment, field will ignore if it is null
     *
     * @param id the id of the assessment to save.
     * @param assessment the assessment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assessment,
     * or with status {@code 400 (Bad Request)} if the assessment is not valid,
     * or with status {@code 404 (Not Found)} if the assessment is not found,
     * or with status {@code 500 (Internal Server Error)} if the assessment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assessments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Assessment> partialUpdateAssessment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Assessment assessment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Assessment partially : {}, {}", id, assessment);
        if (assessment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assessment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assessmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Assessment> result = assessmentService.partialUpdate(assessment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assessment.getId().toString())
        );
    }

    /**
     * {@code GET  /assessments} : get all the assessments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assessments in body.
     */
    @GetMapping("/assessments")
    public ResponseEntity<List<Assessment>> getAllAssessments(Pageable pageable) {
        log.debug("REST request to get a page of Assessments");
        Page<Assessment> page = assessmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assessments/:id} : get the "id" assessment.
     *
     * @param id the id of the assessment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assessment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assessments/{id}")
    public ResponseEntity<Assessment> getAssessment(@PathVariable Long id) {
        log.debug("REST request to get Assessment : {}", id);
        Optional<Assessment> assessment = assessmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assessment);
    }

    /**
     * {@code DELETE  /assessments/:id} : delete the "id" assessment.
     *
     * @param id the id of the assessment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assessments/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        log.debug("REST request to delete Assessment : {}", id);
        assessmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
