package com.techchallenge.pagamento.listener;

import com.techchallenge.pagamento.domain.model.Payment;
import com.techchallenge.pagamento.domain.repository.PaymentRepository;
import com.techchallenge.pagamento.event.PedidoCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoCreatedListenerTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PedidoCreatedListener pedidoCreatedListener;

    private PedidoCreatedEvent event;

    @BeforeEach
    void setUp() {
        event = new PedidoCreatedEvent(1L, 100L, new BigDecimal("50.00"));
    }

    @Test
    void onPedidoCreated_ShouldProcessEventAndSavePayment() {
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);

        pedidoCreatedListener.onPedidoCreated(event);

        verify(paymentRepository, times(1)).save(paymentCaptor.capture());

        Payment savedPayment = paymentCaptor.getValue();
        assertNotNull(savedPayment);
        assertEquals(event.pedidoId(), savedPayment.getPedidoId());
        assertNotNull(savedPayment.getStatus());
        assertTrue(savedPayment.getStatus() == Payment.Status.APPROVED ||
                savedPayment.getStatus() == Payment.Status.REJECTED);
        assertNotNull(savedPayment.getProcessedAt());
        assertTrue(savedPayment.getProcessedAt().isBefore(Instant.now().plusSeconds(1)));
        assertTrue(savedPayment.getProcessedAt().isAfter(Instant.now().minusSeconds(5)));
    }

    @Test
    void onPedidoCreated_ShouldGenerateRandomApprovalStatus() {
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);

        for (int i = 0; i < 10; i++) {
            PedidoCreatedEvent testEvent = new PedidoCreatedEvent((long) i, 100L, new BigDecimal("50.00"));
            pedidoCreatedListener.onPedidoCreated(testEvent);
        }

        verify(paymentRepository, times(10)).save(paymentCaptor.capture());

        paymentCaptor.getAllValues().forEach(payment -> {
            assertNotNull(payment.getStatus());
            assertTrue(payment.getStatus() == Payment.Status.APPROVED ||
                    payment.getStatus() == Payment.Status.REJECTED);
        });
    }

    @Test
    void onPedidoCreated_WithDifferentPedidoId_ShouldMapCorrectly() {
        Long specificPedidoId = 999L;
        PedidoCreatedEvent specificEvent = new PedidoCreatedEvent(specificPedidoId, 200L, new BigDecimal("100.00"));
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);

        pedidoCreatedListener.onPedidoCreated(specificEvent);

        verify(paymentRepository, times(1)).save(paymentCaptor.capture());

        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(specificPedidoId, savedPayment.getPedidoId());
    }
}