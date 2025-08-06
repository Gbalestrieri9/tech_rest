package com.techchallenge.pagamento.event;

import java.math.BigDecimal;

public record PedidoCreatedEvent(
        Long pedidoId,
        Long clienteId,
        BigDecimal total
) {}
