package com.techchallenge.pedido.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
public record PedidoRequest(
        @NotNull Long clienteId,
        @NotNull List<ItemPedidoRequest> itens
) {}