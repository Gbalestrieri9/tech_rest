package com.techchallenge.pagamento.event;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PedidoCreatedEventTest {

    @Test
    void pedidoCreatedEvent_ShouldCreateWithAllFields() {
        Long pedidoId = 1L;
        Long clienteId = 100L;
        BigDecimal total = new BigDecimal("99.99");

        PedidoCreatedEvent event = new PedidoCreatedEvent(pedidoId, clienteId, total);

        assertEquals(pedidoId, event.pedidoId());
        assertEquals(clienteId, event.clienteId());
        assertEquals(total, event.total());
    }

    @Test
    void pedidoCreatedEvent_ShouldHandleNullValues() {
        PedidoCreatedEvent event = new PedidoCreatedEvent(null, null, null);

        assertNull(event.pedidoId());
        assertNull(event.clienteId());
        assertNull(event.total());
    }

    @Test
    void pedidoCreatedEvent_ShouldBeEqualWithSameValues() {
        Long pedidoId = 1L;
        Long clienteId = 100L;
        BigDecimal total = new BigDecimal("99.99");

        PedidoCreatedEvent event1 = new PedidoCreatedEvent(pedidoId, clienteId, total);
        PedidoCreatedEvent event2 = new PedidoCreatedEvent(pedidoId, clienteId, total);

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void pedidoCreatedEvent_ShouldNotBeEqualWithDifferentValues() {
        PedidoCreatedEvent event1 = new PedidoCreatedEvent(1L, 100L, new BigDecimal("99.99"));
        PedidoCreatedEvent event2 = new PedidoCreatedEvent(2L, 100L, new BigDecimal("99.99"));

        assertNotEquals(event1, event2);
    }

    @Test
    void pedidoCreatedEvent_ShouldHaveCorrectToString() {
        Long pedidoId = 1L;
        Long clienteId = 100L;
        BigDecimal total = new BigDecimal("99.99");

        PedidoCreatedEvent event = new PedidoCreatedEvent(pedidoId, clienteId, total);

        String toString = event.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("100"));
        assertTrue(toString.contains("99.99"));
    }

    @Test
    void pedidoCreatedEvent_ShouldHandleLargeValues() {
        Long pedidoId = Long.MAX_VALUE;
        Long clienteId = Long.MAX_VALUE;
        BigDecimal total = new BigDecimal("999999999.99");

        PedidoCreatedEvent event = new PedidoCreatedEvent(pedidoId, clienteId, total);

        assertEquals(pedidoId, event.pedidoId());
        assertEquals(clienteId, event.clienteId());
        assertEquals(total, event.total());
    }
}