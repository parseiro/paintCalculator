package com.vilelapinheiro.paintcalculator.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalaMapperTest {

    private SalaMapper salaMapper;

    @BeforeEach
    public void setUp() {
        salaMapper = new SalaMapperImpl();
    }
}
