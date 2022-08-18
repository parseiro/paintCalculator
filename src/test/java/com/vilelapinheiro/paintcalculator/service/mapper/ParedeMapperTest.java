package com.vilelapinheiro.paintcalculator.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParedeMapperTest {

    private ParedeMapper paredeMapper;

    @BeforeEach
    public void setUp() {
        paredeMapper = new ParedeMapperImpl();
    }
}
