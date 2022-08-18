package com.vilelapinheiro.paintcalculator.service;

import com.vilelapinheiro.paintcalculator.domain.Parede;
import com.vilelapinheiro.paintcalculator.repository.ParedeRepository;
import com.vilelapinheiro.paintcalculator.service.dto.ParedeDTO;
import com.vilelapinheiro.paintcalculator.service.mapper.ParedeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final ParedeMapper paredeMapper;

    public ParedeService(ParedeRepository paredeRepository, ParedeMapper paredeMapper) {
        this.paredeRepository = paredeRepository;
        this.paredeMapper = paredeMapper;
    }

    /**
     * Save a parede.
     *
     * @param paredeDTO the entity to save.
     * @return the persisted entity.
     */
    public ParedeDTO save(ParedeDTO paredeDTO) {
        log.debug("Request to save Parede : {}", paredeDTO);
        Parede parede = paredeMapper.toEntity(paredeDTO);
        parede = paredeRepository.save(parede);
        return paredeMapper.toDto(parede);
    }

    /**
     * Update a parede.
     *
     * @param paredeDTO the entity to save.
     * @return the persisted entity.
     */
    public ParedeDTO update(ParedeDTO paredeDTO) {
        log.debug("Request to save Parede : {}", paredeDTO);
        Parede parede = paredeMapper.toEntity(paredeDTO);
        parede = paredeRepository.save(parede);
        return paredeMapper.toDto(parede);
    }

    /**
     * Partially update a parede.
     *
     * @param paredeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParedeDTO> partialUpdate(ParedeDTO paredeDTO) {
        log.debug("Request to partially update Parede : {}", paredeDTO);

        return paredeRepository
            .findById(paredeDTO.getId())
            .map(existingParede -> {
                paredeMapper.partialUpdate(existingParede, paredeDTO);

                return existingParede;
            })
            .map(paredeRepository::save)
            .map(paredeMapper::toDto);
    }

    /**
     * Get all the paredes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParedeDTO> findAll() {
        log.debug("Request to get all Paredes");
        return paredeRepository.findAll().stream().map(paredeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the paredes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ParedeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return paredeRepository.findAllWithEagerRelationships(pageable).map(paredeMapper::toDto);
    }

    /**
     * Get one parede by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParedeDTO> findOne(Long id) {
        log.debug("Request to get Parede : {}", id);
        return paredeRepository.findOneWithEagerRelationships(id).map(paredeMapper::toDto);
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
