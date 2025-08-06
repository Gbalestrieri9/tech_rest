package com.techchallenge.pedido.controller.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DTOTest {

    @Test
    void itemPedidoRequest_DeveArmazenarDadosCorretamente() {
        ItemPedidoRequest request = new ItemPedidoRequest(1L, 5);

        assertEquals(1L, request.produtoId());
        assertEquals(5, request.quantidade());
    }

    @Test
    void itemPedidoResponse_DeveArmazenarDadosCorretamente() {
        BigDecimal preco = new BigDecimal("25.50");

        ItemPedidoResponse response = new ItemPedidoResponse(1L, 3, preco);

        assertEquals(1L, response.produtoId());
        assertEquals(3, response.quantidade());
        assertEquals(preco, response.precoUnitario());
    }

    @Test
    void pedidoRequest_DeveArmazenarDadosCorretamente() {
        List<ItemPedidoRequest> itens = List.of(new ItemPedidoRequest(1L, 2));

        PedidoRequest request = new PedidoRequest(1L, itens);

        assertEquals(1L, request.clienteId());
        assertEquals(itens, request.itens());
    }

    @Test
    void pedidoResponse_DeveArmazenarDadosCorretamente() {
        Instant dataCriacao = Instant.now();
        BigDecimal total = new BigDecimal("100.00");
        List<ItemPedidoResponse> itens = List.of(
                new ItemPedidoResponse(1L, 2, new BigDecimal("50.00"))
        );

        PedidoResponse response = new PedidoResponse(1L, 2L, total, dataCriacao, itens);

        assertEquals(1L, response.id());
        assertEquals(2L, response.clienteId());
        assertEquals(total, response.total());
        assertEquals(dataCriacao, response.dataCriacao());
        assertEquals(itens, response.itens());
    }

    @Test
    void produtoResponse_DeveArmazenarDadosCorretamente() {
        BigDecimal preco = new BigDecimal("15.75");

        ProdutoResponse response = new ProdutoResponse(
                1L, "Produto Teste", "Descrição", preco, "Categoria", true, "SKU123"
        );

        assertEquals(1L, response.id());
        assertEquals("Produto Teste", response.nome());
        assertEquals("Descrição", response.descricao());
        assertEquals(preco, response.preco());
        assertEquals("Categoria", response.categoria());
        assertTrue(response.ativo());
        assertEquals("SKU123", response.sku());
    }
}