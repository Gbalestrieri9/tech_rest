package com.techchallenge.pedido.event;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void pedidoCreatedEvent_DeveArmazenarDadosCorretamente() {
        Long pedidoId = 1L;
        Long clienteId = 2L;
        BigDecimal total = new BigDecimal("150.75");

        PedidoCreatedEvent event = new PedidoCreatedEvent(pedidoId, clienteId, total);

        assertEquals(pedidoId, event.pedidoId());
        assertEquals(clienteId, event.clienteId());
        assertEquals(total, event.total());
    }
}
