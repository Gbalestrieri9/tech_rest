package com.techchallenge.estoque.controller.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstoqueResponseTest {

    @Test
    void deveCriarEstoqueResponse() {
        EstoqueResponse response = new EstoqueResponse(1L, 100);

        assertNotNull(response);
        assertEquals(1L, response.produtoId());
        assertEquals(100, response.quantidade());
    }

    @Test
    void devePermitirValoresNulos() {
        EstoqueResponse response = new EstoqueResponse(null, null);

        assertNotNull(response);
        assertNull(response.produtoId());
        assertNull(response.quantidade());
    }

    @Test
    void devePermitirQuantidadeZero() {
        EstoqueResponse response = new EstoqueResponse(1L, 0);

        assertNotNull(response);
        assertEquals(1L, response.produtoId());
        assertEquals(0, response.quantidade());
    }
}
