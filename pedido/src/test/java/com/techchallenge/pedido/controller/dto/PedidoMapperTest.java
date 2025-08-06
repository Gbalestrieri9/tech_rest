package com.techchallenge.pedido.controller.dto;

import com.techchallenge.pedido.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoMapperTest {

    private Pedido pedido;
    private PedidoItem pedidoItem1;
    private PedidoItem pedidoItem2;

    @BeforeEach
    void setUp() {
        pedidoItem1 = PedidoItem.builder()
                .id(1L)
                .produtoId(1L)
                .quantidade(2)
                .precoUnitario(new BigDecimal("10.00"))
                .build();

        pedidoItem2 = PedidoItem.builder()
                .id(2L)
                .produtoId(2L)
                .quantidade(3)
                .precoUnitario(new BigDecimal("15.00"))
                .build();

        pedido = Pedido.builder()
                .id(1L)
                .clienteId(1L)
                .total(new BigDecimal("65.00"))
                .dataCriacao(Instant.parse("2024-01-01T10:00:00Z"))
                .itens(List.of(pedidoItem1, pedidoItem2))
                .build();
    }

    @Test
    void toResponse_DeveConverterCorretamente() {
        PedidoResponse response = PedidoMapper.toResponse(pedido);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(1L, response.clienteId());
        assertEquals(new BigDecimal("65.00"), response.total());
        assertEquals(Instant.parse("2024-01-01T10:00:00Z"), response.dataCriacao());

        assertNotNull(response.itens());
        assertEquals(2, response.itens().size());

        ItemPedidoResponse item1 = response.itens().get(0);
        assertEquals(1L, item1.produtoId());
        assertEquals(2, item1.quantidade());
        assertEquals(new BigDecimal("10.00"), item1.precoUnitario());

        ItemPedidoResponse item2 = response.itens().get(1);
        assertEquals(2L, item2.produtoId());
        assertEquals(3, item2.quantidade());
        assertEquals(new BigDecimal("15.00"), item2.precoUnitario());
    }

    @Test
    void toResponse_DeveConverterPedidoSemItens() {
        Pedido pedidoSemItens = Pedido.builder()
                .id(2L)
                .clienteId(2L)
                .total(BigDecimal.ZERO)
                .dataCriacao(Instant.now())
                .itens(List.of())
                .build();

        PedidoResponse response = PedidoMapper.toResponse(pedidoSemItens);

        assertNotNull(response);
        assertEquals(2L, response.id());
        assertEquals(2L, response.clienteId());
        assertEquals(BigDecimal.ZERO, response.total());
        assertNotNull(response.itens());
        assertTrue(response.itens().isEmpty());
    }
}