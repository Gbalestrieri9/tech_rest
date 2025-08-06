package com.techchallenge.pagamento.listener;

import com.techchallenge.pagamento.config.RabbitConfig;
import com.techchallenge.pagamento.domain.model.Payment;
import com.techchallenge.pagamento.domain.repository.PaymentRepository;
import com.techchallenge.pagamento.event.PedidoCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class PedidoCreatedListener {
    private final PaymentRepository repo;
    private final Random rnd = new Random();

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void onPedidoCreated(PedidoCreatedEvent evt) {
        boolean aprovou = rnd.nextBoolean();

        Payment payment = Payment.builder()
                .pedidoId(evt.pedidoId())
                .status(aprovou ? Payment.Status.APPROVED : Payment.Status.REJECTED)
                .processedAt(Instant.now())
                .build();

        repo.save(payment);
    }
}
