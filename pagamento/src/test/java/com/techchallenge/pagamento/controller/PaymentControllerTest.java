package com.techchallenge.pagamento.controller;

import com.techchallenge.pagamento.controller.dto.PaymentResponse;
import com.techchallenge.pagamento.domain.model.Payment;
import com.techchallenge.pagamento.domain.repository.PaymentRepository;
import com.techchallenge.pagamento.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentController paymentController;

    private Payment payment;
    private final Long pedidoId = 1L;

    @BeforeEach
    void setUp() {
        payment = Payment.builder()
                .id(1L)
                .pedidoId(pedidoId)
                .status(Payment.Status.APPROVED)
                .processedAt(Instant.now())
                .build();
    }

    @Test
    void status_WhenPaymentExists_ShouldReturnPaymentResponse() {
        when(paymentRepository.findByPedidoId(pedidoId)).thenReturn(Optional.of(payment));

        ResponseEntity<PaymentResponse> response = paymentController.status(pedidoId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        PaymentResponse paymentResponse = response.getBody();
        assertEquals(payment.getId(), paymentResponse.id());
        assertEquals(payment.getPedidoId(), paymentResponse.pedidoId());
        assertEquals(payment.getStatus().name(), paymentResponse.status());
        assertEquals(payment.getProcessedAt(), paymentResponse.processedAt());

        verify(paymentRepository, times(1)).findByPedidoId(pedidoId);
    }

    @Test
    void status_WhenPaymentNotFound_ShouldThrowResourceNotFoundException() {
        when(paymentRepository.findByPedidoId(pedidoId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> paymentController.status(pedidoId)
        );

        assertEquals("Pagamento para pedido " + pedidoId + " não encontrado", exception.getMessage());
        verify(paymentRepository, times(1)).findByPedidoId(pedidoId);
    }

    @Test
    void status_WithRejectedPayment_ShouldReturnCorrectStatus() {
        payment.setStatus(Payment.Status.REJECTED);
        when(paymentRepository.findByPedidoId(pedidoId)).thenReturn(Optional.of(payment));

        ResponseEntity<PaymentResponse> response = paymentController.status(pedidoId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("REJECTED", response.getBody().status());

        verify(paymentRepository, times(1)).findByPedidoId(pedidoId);
    }
}