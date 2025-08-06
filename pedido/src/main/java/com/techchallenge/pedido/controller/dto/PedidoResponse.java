package com.techchallenge.pedido.controller.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
public record PedidoResponse(
        Long id,
        Long clienteId,
        BigDecimal total,
        Instant dataCriacao,
        List<ItemPedidoResponse> itens
) {}