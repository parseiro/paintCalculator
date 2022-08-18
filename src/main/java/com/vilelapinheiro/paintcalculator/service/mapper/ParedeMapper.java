package com.vilelapinheiro.paintcalculator.service.mapper;

import com.vilelapinheiro.paintcalculator.domain.Parede;
import com.vilelapinheiro.paintcalculator.domain.Sala;
import com.vilelapinheiro.paintcalculator.service.dto.ParedeDTO;
import com.vilelapinheiro.paintcalculator.service.dto.SalaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Parede} and its DTO {@link ParedeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParedeMapper extends EntityMapper<ParedeDTO, Parede> {
    @Mapping(target = "sala", source = "sala", qualifiedByName = "salaNome")
    ParedeDTO toDto(Parede s);

    @Named("salaNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    SalaDTO toDtoSalaNome(Sala sala);
}
