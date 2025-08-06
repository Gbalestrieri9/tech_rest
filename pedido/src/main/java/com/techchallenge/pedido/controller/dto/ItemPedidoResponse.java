package com.techchallenge.pedido.controller.dto;

import java.math.BigDecimal;
public record ItemPedidoResponse(
        Long produtoId,
        Integer quantidade,
        BigDecimal precoUnitario
) {}