package com.vilelapinheiro.paintcalculator.service;

import com.vilelapinheiro.paintcalculator.domain.Sala;
import com.vilelapinheiro.paintcalculator.repository.SalaRepository;
import com.vilelapinheiro.paintcalculator.service.dto.SalaDTO;
import com.vilelapinheiro.paintcalculator.service.mapper.SalaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sala}.
 */
@Service
@Transactional
public class SalaService {

    private final Logger log = LoggerFactory.getLogger(SalaService.class);

    private final SalaRepository salaRepository;

    private final SalaMapper salaMapper;

    public SalaService(SalaRepository salaRepository, SalaMapper salaMapper) {
        this.salaRepository = salaRepository;
        this.salaMapper = salaMapper;
    }

    /**
     * Save a sala.
     *
     * @param salaDTO the entity to save.
     * @return the persisted entity.
     */
    public SalaDTO save(SalaDTO salaDTO) {
        log.debug("Request to save Sala : {}", salaDTO);
        Sala sala = salaMapper.toEntity(salaDTO);
        sala = salaRepository.save(sala);
        return salaMapper.toDto(sala);
    }

    /**
     * Update a sala.
     *
     * @param salaDTO the entity to save.
     * @return the persisted entity.
     */
    public SalaDTO update(SalaDTO salaDTO) {
        log.debug("Request to save Sala : {}", salaDTO);
        Sala sala = salaMapper.toEntity(salaDTO);
        sala = salaRepository.save(sala);
        return salaMapper.toDto(sala);
    }

    /**
     * Partially update a sala.
     *
     * @param salaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SalaDTO> partialUpdate(SalaDTO salaDTO) {
        log.debug("Request to partially update Sala : {}", salaDTO);

        return salaRepository
            .findById(salaDTO.getId())
            .map(existingSala -> {
                salaMapper.partialUpdate(existingSala, salaDTO);

                return existingSala;
            })
            .map(salaRepository::save)
            .map(salaMapper::toDto);
    }

    /**
     * Get all the salas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SalaDTO> findAll() {
        log.debug("Request to get all Salas");
        return salaRepository.findAll().stream().map(salaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one sala by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SalaDTO> findOne(Long id) {
        log.debug("Request to get Sala : {}", id);
        return salaRepository.findById(id).map(salaMapper::toDto);
    }

    /**
     * Delete the sala by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Sala : {}", id);
        salaRepository.deleteById(id);
    }
}
