package com.techchallenge.pagamento.controller.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PaymentResponseTest {

    @Test
    void paymentResponse_ShouldCreateWithAllFields() {
        Long id = 1L;
        Long pedidoId = 100L;
        String status = "APPROVED";
        Instant processedAt = Instant.now();

        PaymentResponse response = new PaymentResponse(id, pedidoId, status, processedAt);

        assertEquals(id, response.id());
        assertEquals(pedidoId, response.pedidoId());
        assertEquals(status, response.status());
        assertEquals(processedAt, response.processedAt());
    }

    @Test
    void paymentResponse_ShouldHandleNullValues() {
        PaymentResponse response = new PaymentResponse(null, null, null, null);

        assertNull(response.id());
        assertNull(response.pedidoId());
        assertNull(response.status());
        assertNull(response.processedAt());
    }

    @Test
    void paymentResponse_ShouldBeEqualWithSameValues() {
        Long id = 1L;
        Long pedidoId = 100L;
        String status = "APPROVED";
        Instant processedAt = Instant.parse("2023-01-01T00:00:00Z");

        PaymentResponse response1 = new PaymentResponse(id, pedidoId, status, processedAt);
        PaymentResponse response2 = new PaymentResponse(id, pedidoId, status, processedAt);

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void paymentResponse_ShouldNotBeEqualWithDifferentValues() {
        PaymentResponse response1 = new PaymentResponse(1L, 100L, "APPROVED", Instant.now());
        PaymentResponse response2 = new PaymentResponse(2L, 100L, "APPROVED", Instant.now());

        assertNotEquals(response1, response2);
    }

    @Test
    void paymentResponse_ShouldHaveCorrectToString() {
        Long id = 1L;
        Long pedidoId = 100L;
        String status = "APPROVED";
        Instant processedAt = Instant.parse("2023-01-01T00:00:00Z");

        PaymentResponse response = new PaymentResponse(id, pedidoId, status, processedAt);

        String toString = response.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("100"));
        assertTrue(toString.contains("APPROVED"));
        assertTrue(toString.contains("2023-01-01T00:00:00Z"));
    }
}