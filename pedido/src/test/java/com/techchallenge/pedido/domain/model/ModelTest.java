package com.techchallenge.pedido.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void pedido_DeveSerCriadoComBuilder() {
        Instant agora = Instant.now();
        List<PedidoItem> itens = new ArrayList<>();

        Pedido pedido = Pedido.builder()
                .id(1L)
                .clienteId(2L)
                .total(new BigDecimal("100.50"))
                .dataCriacao(agora)
                .itens(itens)
                .build();

        assertEquals(1L, pedido.getId());
        assertEquals(2L, pedido.getClienteId());
        assertEquals(new BigDecimal("100.50"), pedido.getTotal());
        assertEquals(agora, pedido.getDataCriacao());
        assertEquals(itens, pedido.getItens());
    }

    @Test
    void pedido_DevePermitirSettersEGetters() {
        Pedido pedido = new Pedido();
        Instant agora = Instant.now();

        pedido.setId(1L);
        pedido.setClienteId(2L);
        pedido.setTotal(new BigDecimal("200.75"));
        pedido.setDataCriacao(agora);

        assertEquals(1L, pedido.getId());
        assertEquals(2L, pedido.getClienteId());
        assertEquals(new BigDecimal("200.75"), pedido.getTotal());
        assertEquals(agora, pedido.getDataCriacao());
    }

    @Test
    void pedidoItem_DeveSerCriadoComBuilder() {
        Pedido pedido = new Pedido();

        PedidoItem item = PedidoItem.builder()
                .id(1L)
                .pedido(pedido)
                .produtoId(3L)
                .quantidade(5)
                .precoUnitario(new BigDecimal("25.00"))
                .build();

        assertEquals(1L, item.getId());
        assertEquals(pedido, item.getPedido());
        assertEquals(3L, item.getProdutoId());
        assertEquals(5, item.getQuantidade());
        assertEquals(new BigDecimal("25.00"), item.getPrecoUnitario());
    }

    @Test
    void pedidoItem_DevePermitirSettersEGetters() {
        PedidoItem item = new PedidoItem();
        Pedido pedido = new Pedido();

        item.setId(1L);
        item.setPedido(pedido);
        item.setProdutoId(3L);
        item.setQuantidade(2);
        item.setPrecoUnitario(new BigDecimal("15.50"));

        assertEquals(1L, item.getId());
        assertEquals(pedido, item.getPedido());
        assertEquals(3L, item.getProdutoId());
        assertEquals(2, item.getQuantidade());
        assertEquals(new BigDecimal("15.50"), item.getPrecoUnitario());
    }

    @Test
    void pedido_DeveAceitarConstrutorComTodosArgumentos() {
        Instant agora = Instant.now();
        List<PedidoItem> itens = new ArrayList<>();

        Pedido pedido = new Pedido(1L, 2L, new BigDecimal("300.00"), agora, itens);

        assertEquals(1L, pedido.getId());
        assertEquals(2L, pedido.getClienteId());
        assertEquals(new BigDecimal("300.00"), pedido.getTotal());
        assertEquals(agora, pedido.getDataCriacao());
        assertEquals(itens, pedido.getItens());
    }

    @Test
    void pedidoItem_DeveAceitarConstrutorComTodosArgumentos() {
        Pedido pedido = new Pedido();

        PedidoItem item = new PedidoItem(1L, pedido, 3L, 4, new BigDecimal("12.50"));

        assertEquals(1L, item.getId());
        assertEquals(pedido, item.getPedido());
        assertEquals(3L, item.getProdutoId());
        assertEquals(4, item.getQuantidade());
        assertEquals(new BigDecimal("12.50"), item.getPrecoUnitario());
    }
}