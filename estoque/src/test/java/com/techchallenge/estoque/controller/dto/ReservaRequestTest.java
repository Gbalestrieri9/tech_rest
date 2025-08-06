package com.techchallenge.estoque.controller.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class ReservaRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deveCriarReservaRequestValida() {
        ReservaRequest request = new ReservaRequest(10);

        assertNotNull(request);
        assertEquals(10, request.quantidade());

        Set<ConstraintViolation<ReservaRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void deveValidarQuantidadeNaoNula() {
        ReservaRequest request = new ReservaRequest(null);

        Set<ConstraintViolation<ReservaRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("não deve ser nulo") || v.getMessage().contains("must not be null")));
    }

    @Test
    void deveValidarQuantidadeMaiorQueZero() {
        ReservaRequest request = new ReservaRequest(0);

        Set<ConstraintViolation<ReservaRequest>> violations = validator.validate(request);

        assertTrue(violations.size() >= 1);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> "quantidade".equals(v.getPropertyPath().toString())));
    }

    @Test
    void deveValidarQuantidadeNegativa() {
        ReservaRequest request = new ReservaRequest(-5);

        Set<ConstraintViolation<ReservaRequest>> violations = validator.validate(request);

        assertTrue(violations.size() >= 1);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> "quantidade".equals(v.getPropertyPath().toString())));
    }
}
