package com.vilelapinheiro.paintcalculator.web.rest;

import com.vilelapinheiro.paintcalculator.repository.SalaRepository;
import com.vilelapinheiro.paintcalculator.service.SalaService;
import com.vilelapinheiro.paintcalculator.service.dto.SalaDTO;
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
 * REST controller for managing {@link com.vilelapinheiro.paintcalculator.domain.Sala}.
 */
@RestController
@RequestMapping("/api")
public class SalaResource {

    private final Logger log = LoggerFactory.getLogger(SalaResource.class);

    private static final String ENTITY_NAME = "sala";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalaService salaService;

    private final SalaRepository salaRepository;

    public SalaResource(SalaService salaService, SalaRepository salaRepository) {
        this.salaService = salaService;
        this.salaRepository = salaRepository;
    }

    /**
     * {@code POST  /salas} : Create a new sala.
     *
     * @param salaDTO the salaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salaDTO, or with status {@code 400 (Bad Request)} if the sala has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/salas")
    public ResponseEntity<SalaDTO> createSala(@Valid @RequestBody SalaDTO salaDTO) throws URISyntaxException {
        log.debug("REST request to save Sala : {}", salaDTO);
        if (salaDTO.getId() != null) {
            throw new BadRequestAlertException("A new sala cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalaDTO result = salaService.save(salaDTO);
        return ResponseEntity
            .created(new URI("/api/salas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /salas/:id} : Updates an existing sala.
     *
     * @param id the id of the salaDTO to save.
     * @param salaDTO the salaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaDTO,
     * or with status {@code 400 (Bad Request)} if the salaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/salas/{id}")
    public ResponseEntity<SalaDTO> updateSala(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SalaDTO salaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Sala : {}, {}", id, salaDTO);
        if (salaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SalaDTO result = salaService.update(salaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, salaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /salas/:id} : Partial updates given fields of an existing sala, field will ignore if it is null
     *
     * @param id the id of the salaDTO to save.
     * @param salaDTO the salaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaDTO,
     * or with status {@code 400 (Bad Request)} if the salaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the salaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the salaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/salas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SalaDTO> partialUpdateSala(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SalaDTO salaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sala partially : {}, {}", id, salaDTO);
        if (salaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SalaDTO> result = salaService.partialUpdate(salaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, salaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /salas} : get all the salas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salas in body.
     */
    @GetMapping("/salas")
    public List<SalaDTO> getAllSalas() {
        log.debug("REST request to get all Salas");
        return salaService.findAll();
    }

    /**
     * {@code GET  /salas/:id} : get the "id" sala.
     *
     * @param id the id of the salaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/salas/{id}")
    public ResponseEntity<SalaDTO> getSala(@PathVariable Long id) {
        log.debug("REST request to get Sala : {}", id);
        Optional<SalaDTO> salaDTO = salaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salaDTO);
    }

    /**
     * {@code DELETE  /salas/:id} : delete the "id" sala.
     *
     * @param id the id of the salaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/salas/{id}")
    public ResponseEntity<Void> deleteSala(@PathVariable Long id) {
        log.debug("REST request to delete Sala : {}", id);
        salaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
