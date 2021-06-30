package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Get;
import com.mycompany.myapp.repository.GetRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Get}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GetResource {

    private final Logger log = LoggerFactory.getLogger(GetResource.class);

    private static final String ENTITY_NAME = "get";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GetRepository getRepository;

    public GetResource(GetRepository getRepository) {
        this.getRepository = getRepository;
    }

    /**
     * {@code POST  /gets} : Create a new get.
     *
     * @param get the get to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new get, or with status {@code 400 (Bad Request)} if the get has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gets")
    public ResponseEntity<Get> createGet(@Valid @RequestBody Get get) throws URISyntaxException {
        log.debug("REST request to save Get : {}", get);
        if (get.getId() != null) {
            throw new BadRequestAlertException("A new get cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Get result = getRepository.save(get);
        return ResponseEntity
            .created(new URI("/api/gets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gets/:id} : Updates an existing get.
     *
     * @param id the id of the get to save.
     * @param get the get to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated get,
     * or with status {@code 400 (Bad Request)} if the get is not valid,
     * or with status {@code 500 (Internal Server Error)} if the get couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gets/{id}")
    public ResponseEntity<Get> updateGet(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Get get)
        throws URISyntaxException {
        log.debug("REST request to update Get : {}, {}", id, get);
        if (get.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, get.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!getRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Get result = getRepository.save(get);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, get.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gets/:id} : Partial updates given fields of an existing get, field will ignore if it is null
     *
     * @param id the id of the get to save.
     * @param get the get to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated get,
     * or with status {@code 400 (Bad Request)} if the get is not valid,
     * or with status {@code 404 (Not Found)} if the get is not found,
     * or with status {@code 500 (Internal Server Error)} if the get couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Get> partialUpdateGet(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Get get)
        throws URISyntaxException {
        log.debug("REST request to partial update Get partially : {}, {}", id, get);
        if (get.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, get.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!getRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Get> result = getRepository
            .findById(get.getId())
            .map(
                existingGet -> {
                    if (get.getNote() != null) {
                        existingGet.setNote(get.getNote());
                    }

                    return existingGet;
                }
            )
            .map(getRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, get.getId().toString())
        );
    }

    /**
     * {@code GET  /gets} : get all the gets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gets in body.
     */
    @GetMapping("/gets")
    public List<Get> getAllGets() {
        log.debug("REST request to get all Gets");
        return getRepository.findAll();
    }

    /**
     * {@code GET  /gets/:id} : get the "id" get.
     *
     * @param id the id of the get to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the get, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gets/{id}")
    public ResponseEntity<Get> getGet(@PathVariable Long id) {
        log.debug("REST request to get Get : {}", id);
        Optional<Get> get = getRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(get);
    }

    /**
     * {@code DELETE  /gets/:id} : delete the "id" get.
     *
     * @param id the id of the get to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gets/{id}")
    public ResponseEntity<Void> deleteGet(@PathVariable Long id) {
        log.debug("REST request to delete Get : {}", id);
        getRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
