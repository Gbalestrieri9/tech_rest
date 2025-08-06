package com.techchallenge.produto.domain.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


class ProdutoTest {

    @Test
    void prePersist_DeveGerarSkuQuandoNulo() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(new BigDecimal("50.00"));

        produto.prePersist();

        assertNotNull(produto.getSku());
        assertFalse(produto.getSku().isBlank());
        assertEquals(true, produto.getAtivo());
    }

    @Test
    void prePersist_DeveGerarSkuQuandoVazio() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(new BigDecimal("50.00"));
        produto.setSku("");

        produto.prePersist();

        assertNotNull(produto.getSku());
        assertFalse(produto.getSku().isBlank());
    }

    @Test
    void prePersist_DeveManterSkuExistente() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(new BigDecimal("50.00"));
        produto.setSku("SKU_EXISTENTE");

        produto.prePersist();

        assertEquals("SKU_EXISTENTE", produto.getSku());
    }

    @Test
    void prePersist_DeveDefinirAtivoComoTrueQuandoNulo() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(new BigDecimal("50.00"));
        produto.setAtivo(null);

        produto.prePersist();

        assertEquals(true, produto.getAtivo());
    }

    @Test
    void prePersist_DeveManterAtivoExistente() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(new BigDecimal("50.00"));
        produto.setAtivo(false);

        produto.prePersist();

        assertEquals(false, produto.getAtivo());
    }
}