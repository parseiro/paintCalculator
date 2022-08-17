package com.vilelapinheiro.paintcalculator.service;

import com.vilelapinheiro.paintcalculator.domain.Parede;
import com.vilelapinheiro.paintcalculator.repository.ParedeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Parede}.
 */
@Service
@Transactional
public class ParedeService {

    private final Logger log = LoggerFactory.getLogger(ParedeService.class);

    private final ParedeRepository paredeRepository;

    public ParedeService(ParedeRepository paredeRepository) {
        this.paredeRepository = paredeRepository;
    }

    /**
     * Save a parede.
     *
     * @param parede the entity to save.
     * @return the persisted entity.
     */
    public Parede save(Parede parede) {
        log.debug("Request to save Parede : {}", parede);
        return paredeRepository.save(parede);
    }

    /**
     * Update a parede.
     *
     * @param parede the entity to save.
     * @return the persisted entity.
     */
    public Parede update(Parede parede) {
        log.debug("Request to save Parede : {}", parede);
        return paredeRepository.save(parede);
    }

    /**
     * Partially update a parede.
     *
     * @param parede the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Parede> partialUpdate(Parede parede) {
        log.debug("Request to partially update Parede : {}", parede);

        return paredeRepository
            .findById(parede.getId())
            .map(existingParede -> {
                if (parede.getLargura() != null) {
                    existingParede.setLargura(parede.getLargura());
                }
                if (parede.getAltura() != null) {
                    existingParede.setAltura(parede.getAltura());
                }
                if (parede.getNumPortas() != null) {
                    existingParede.setNumPortas(parede.getNumPortas());
                }
                if (parede.getNumJanelas() != null) {
                    existingParede.setNumJanelas(parede.getNumJanelas());
                }

                return existingParede;
            })
            .map(paredeRepository::save);
    }

    /**
     * Get all the paredes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Parede> findAll() {
        log.debug("Request to get all Paredes");
        return paredeRepository.findAll();
    }

    /**
     * Get all the paredes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Parede> findAllWithEagerRelationships(Pageable pageable) {
        return paredeRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one parede by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Parede> findOne(Long id) {
        log.debug("Request to get Parede : {}", id);
        return paredeRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the parede by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Parede : {}", id);
        paredeRepository.deleteById(id);
    }
}
