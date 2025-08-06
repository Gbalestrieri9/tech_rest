package com.techchallenge.produto.controller.dto;

import com.techchallenge.produto.domain.model.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoMapperTest {

    @Test
    void toResponse_DeveConverterProdutoParaResponse() {
        Produto produto = Produto.builder()
                .id(1L)
                .nome("Produto Teste")
                .descricao("Descrição do produto")
                .preco(new BigDecimal("99.99"))
                .categoria("Categoria1")
                .ativo(true)
                .sku("SKU123")
                .build();

        ProdutoResponse response = ProdutoMapper.toResponse(produto);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Produto Teste", response.nome());
        assertEquals("Descrição do produto", response.descricao());
        assertEquals(new BigDecimal("99.99"), response.preco());
        assertEquals("Categoria1", response.categoria());
        assertEquals(true, response.ativo());
        assertEquals("SKU123", response.sku());
    }

    @Test
    void toResponse_DeveConverterProdutoComValoresNulos() {
        Produto produto = Produto.builder()
                .id(1L)
                .nome("Produto Teste")
                .preco(new BigDecimal("50.00"))
                .ativo(true)
                .sku("SKU123")
                .build();

        ProdutoResponse response = ProdutoMapper.toResponse(produto);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Produto Teste", response.nome());
        assertNull(response.descricao());
        assertNull(response.categoria());
        assertEquals(new BigDecimal("50.00"), response.preco());
        assertEquals(true, response.ativo());
        assertEquals("SKU123", response.sku());
    }
}