package com.techchallenge.pagamento.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void payment_ShouldCreateWithAllArgs() {
        Long id = 1L;
        Long pedidoId = 100L;
        Payment.Status status = Payment.Status.APPROVED;
        Instant processedAt = Instant.now();

        Payment payment = new Payment(id, pedidoId, status, processedAt);

        assertEquals(id, payment.getId());
        assertEquals(pedidoId, payment.getPedidoId());
        assertEquals(status, payment.getStatus());
        assertEquals(processedAt, payment.getProcessedAt());
    }

    @Test
    void payment_ShouldCreateWithNoArgs() {
        Payment payment = new Payment();

        assertNotNull(payment);
        assertNull(payment.getId());
        assertNull(payment.getPedidoId());
        assertNull(payment.getStatus());
        assertNull(payment.getProcessedAt());
    }

    @Test
    void payment_ShouldBuildWithBuilder() {
        Long id = 1L;
        Long pedidoId = 100L;
        Payment.Status status = Payment.Status.REJECTED;
        Instant processedAt = Instant.now();

        Payment payment = Payment.builder()
                .id(id)
                .pedidoId(pedidoId)
                .status(status)
                .processedAt(processedAt)
                .build();

        assertEquals(id, payment.getId());
        assertEquals(pedidoId, payment.getPedidoId());
        assertEquals(status, payment.getStatus());
        assertEquals(processedAt, payment.getProcessedAt());
    }

    @Test
    void payment_ShouldSetAndGetFields() {
        Payment payment = new Payment();
        Long id = 1L;
        Long pedidoId = 100L;
        Payment.Status status = Payment.Status.APPROVED;
        Instant processedAt = Instant.now();

        payment.setId(id);
        payment.setPedidoId(pedidoId);
        payment.setStatus(status);
        payment.setProcessedAt(processedAt);

        assertEquals(id, payment.getId());
        assertEquals(pedidoId, payment.getPedidoId());
        assertEquals(status, payment.getStatus());
        assertEquals(processedAt, payment.getProcessedAt());
    }

    @Test
    void paymentStatus_ShouldHaveCorrectValues() {
        assertEquals("APPROVED", Payment.Status.APPROVED.name());
        assertEquals("REJECTED", Payment.Status.REJECTED.name());
        assertEquals(2, Payment.Status.values().length);
    }

    @Test
    void payment_ShouldBuildPartiallyWithBuilder() {
        Payment payment = Payment.builder()
                .pedidoId(123L)
                .status(Payment.Status.APPROVED)
                .build();

        // Then
        assertNull(payment.getId());
        assertEquals(123L, payment.getPedidoId());
        assertEquals(Payment.Status.APPROVED, payment.getStatus());
        assertNull(payment.getProcessedAt());
    }
}