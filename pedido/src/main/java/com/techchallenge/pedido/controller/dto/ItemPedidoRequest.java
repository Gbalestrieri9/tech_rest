package com.techchallenge.pedido.controller.dto;

import jakarta.validation.constraints.Min;
public record ItemPedidoRequest(
        @Min(1) Long produtoId,
        @Min(1) Integer quantidade
) {}

