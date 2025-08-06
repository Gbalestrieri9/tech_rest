package com.techchallenge.pagamento.controller.dto;

import java.time.Instant;

public record PaymentResponse(
        Long id,
        Long pedidoId,
        String status,
        Instant processedAt
) {}
