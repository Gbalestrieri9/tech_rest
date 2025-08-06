package com.techchallenge.estoque.controller.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EstoqueRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deveCriarEstoqueRequestValido() {
        EstoqueRequest request = new EstoqueRequest(1L, 100);

        assertNotNull(request);
        assertEquals(1L, request.produtoId());
        assertEquals(100, request.quantidade());

        Set<ConstraintViolation<EstoqueRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void deveCriarEstoqueRequestComQuantidadeZero() {
        EstoqueRequest request = new EstoqueRequest(1L, 0);

        assertNotNull(request);
        assertEquals(1L, request.produtoId());
        assertEquals(0, request.quantidade());

        Set<ConstraintViolation<EstoqueRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void deveValidarProdutoIdNaoNulo() {
        EstoqueRequest request = new EstoqueRequest(null, 100);

        Set<ConstraintViolation<EstoqueRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("não deve ser nulo") || v.getMessage().contains("must not be null")));
    }

    @Test
    void deveValidarQuantidadeNaoNegativa() {
        EstoqueRequest request = new EstoqueRequest(1L, -1);

        Set<ConstraintViolation<EstoqueRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> "quantidade".equals(v.getPropertyPath().toString())));
    }

    @Test
    void devePermitirQuantidadeNula() {
        EstoqueRequest request = new EstoqueRequest(1L, null);

        assertNotNull(request);
        assertEquals(1L, request.produtoId());
        assertNull(request.quantidade());

        Set<ConstraintViolation<EstoqueRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }
}
