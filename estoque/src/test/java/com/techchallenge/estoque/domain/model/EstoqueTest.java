package com.techchallenge.estoque.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstoqueTest {

    @Test
    void deveCriarEstoqueComBuilder() {
        Estoque estoque = Estoque.builder()
                .produtoId(1L)
                .quantidade(100)
                .build();

        assertNotNull(estoque);
        assertEquals(1L, estoque.getProdutoId());
        assertEquals(100, estoque.getQuantidade());
    }

    @Test
    void deveCriarEstoqueComConstructorCompleto() {
        Estoque estoque = new Estoque(1L, 100);

        assertNotNull(estoque);
        assertEquals(1L, estoque.getProdutoId());
        assertEquals(100, estoque.getQuantidade());
    }

    @Test
    void deveCriarEstoqueComConstructorVazio() {
        Estoque estoque = new Estoque();

        assertNotNull(estoque);
        assertNull(estoque.getProdutoId());
        assertNull(estoque.getQuantidade());
    }

    @Test
    void deveDefinirEObterProdutoId() {
        Estoque estoque = new Estoque();
        Long produtoId = 1L;

        estoque.setProdutoId(produtoId);

        assertEquals(produtoId, estoque.getProdutoId());
    }

    @Test
    void deveDefinirEObterQuantidade() {
        Estoque estoque = new Estoque();
        Integer quantidade = 50;

        estoque.setQuantidade(quantidade);

        assertEquals(quantidade, estoque.getQuantidade());
    }

    @Test
    void devePermitirQuantidadeZero() {
        Estoque estoque = Estoque.builder()
                .produtoId(1L)
                .quantidade(0)
                .build();

        assertNotNull(estoque);
        assertEquals(1L, estoque.getProdutoId());
        assertEquals(0, estoque.getQuantidade());
    }

    @Test
    void devePermitirValoresNulos() {
        Estoque estoque = Estoque.builder()
                .produtoId(null)
                .quantidade(null)
                .build();

        assertNotNull(estoque);
        assertNull(estoque.getProdutoId());
        assertNull(estoque.getQuantidade());
    }
}