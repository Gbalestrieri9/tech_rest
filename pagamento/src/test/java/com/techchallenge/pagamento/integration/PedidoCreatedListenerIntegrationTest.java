package com.techchallenge.pagamento.integration;

import com.techchallenge.pagamento.domain.model.Payment;
import com.techchallenge.pagamento.domain.repository.PaymentRepository;
import com.techchallenge.pagamento.event.PedidoCreatedEvent;
import com.techchallenge.pagamento.listener.PedidoCreatedListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class PedidoCreatedListenerIntegrationTest {

    @MockBean
    private PaymentRepository paymentRepository;

    @Autowired
    private PedidoCreatedListener pedidoCreatedListener;

    @Test
    void onPedidoCreated_ShouldProcessMessageAndSavePayment() {
        PedidoCreatedEvent event = new PedidoCreatedEvent(123L, 456L, new BigDecimal("150.00"));

        pedidoCreatedListener.onPedidoCreated(event);

        verify(paymentRepository).save(argThat(payment ->
                payment.getPedidoId().equals(123L) &&
                        (payment.getStatus() == Payment.Status.APPROVED || payment.getStatus() == Payment.Status.REJECTED) &&
                        payment.getProcessedAt() != null
        ));
    }
}