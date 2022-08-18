package com.vilelapinheiro.paintcalculator.web.rest;

import com.vilelapinheiro.paintcalculator.repository.ParedeRepository;
import com.vilelapinheiro.paintcalculator.service.ParedeService;
import com.vilelapinheiro.paintcalculator.service.dto.ParedeDTO;
import com.vilelapinheiro.paintcalculator.web.rest.errors.BadRequestAlertException;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.vilelapinheiro.paintcalculator.domain.Parede}.
 */
@RestController
@RequestMapping("/api")
public class ParedeResource {

    private final Logger log = LoggerFactory.getLogger(ParedeResource.class);

    private static final String ENTITY_NAME = "parede";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParedeService paredeService;

    private final ParedeRepository paredeRepository;

    public ParedeResource(ParedeService paredeService, ParedeRepository paredeRepository) {
        this.paredeService = paredeService;
        this.paredeRepository = paredeRepository;
    }

    /**
     * {@code POST  /paredes} : Create a new parede.
     *
     * @param paredeDTO the paredeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paredeDTO, or with status {@code 400 (Bad Request)} if the parede has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paredes")
    public ResponseEntity<ParedeDTO> createParede(@Valid @RequestBody ParedeDTO paredeDTO) throws URISyntaxException {
        log.debug("REST request to save Parede : {}", paredeDTO);
        if (paredeDTO.getId() != null) {
            throw new BadRequestAlertException("A new parede cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParedeDTO result = paredeService.save(paredeDTO);
        return ResponseEntity
            .created(new URI("/api/paredes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paredes/:id} : Updates an existing parede.
     *
     * @param id the id of the paredeDTO to save.
     * @param paredeDTO the paredeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paredeDTO,
     * or with status {@code 400 (Bad Request)} if the paredeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paredeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paredes/{id}")
    public ResponseEntity<ParedeDTO> updateParede(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParedeDTO paredeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Parede : {}, {}", id, paredeDTO);
        if (paredeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paredeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paredeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParedeDTO result = paredeService.update(paredeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paredeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /paredes/:id} : Partial updates given fields of an existing parede, field will ignore if it is null
     *
     * @param id the id of the paredeDTO to save.
     * @param paredeDTO the paredeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paredeDTO,
     * or with status {@code 400 (Bad Request)} if the paredeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paredeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paredeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/paredes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ParedeDTO> partialUpdateParede(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParedeDTO paredeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parede partially : {}, {}", id, paredeDTO);
        if (paredeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paredeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paredeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParedeDTO> result = paredeService.partialUpdate(paredeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paredeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paredes} : get all the paredes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paredes in body.
     */
    @GetMapping("/paredes")
    public List<ParedeDTO> getAllParedes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Paredes");
        return paredeService.findAll();
    }

    /**
     * {@code GET  /paredes/:id} : get the "id" parede.
     *
     * @param id the id of the paredeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paredeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paredes/{id}")
    public ResponseEntity<ParedeDTO> getParede(@PathVariable Long id) {
        log.debug("REST request to get Parede : {}", id);
        Optional<ParedeDTO> paredeDTO = paredeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paredeDTO);
    }

    /**
     * {@code DELETE  /paredes/:id} : delete the "id" parede.
     *
     * @param id the id of the paredeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paredes/{id}")
    public ResponseEntity<Void> deleteParede(@PathVariable Long id) {
        log.debug("REST request to delete Parede : {}", id);
        paredeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
