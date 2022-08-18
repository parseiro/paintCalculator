package com.vilelapinheiro.paintcalculator.service.mapper;

import com.vilelapinheiro.paintcalculator.domain.Sala;
import com.vilelapinheiro.paintcalculator.service.dto.SalaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sala} and its DTO {@link SalaDTO}.
 */
@Mapper(componentModel = "spring")
public interface SalaMapper extends EntityMapper<SalaDTO, Sala> {}
