package com.techchallenge.pedido.controller.dto;

import com.techchallenge.pedido.domain.model.*;

import java.util.stream.Collectors;

public class PedidoMapper {
    public static PedidoResponse toResponse(Pedido p) {
        var itens = p.getItens().stream().map(i ->
                new ItemPedidoResponse(i.getProdutoId(), i.getQuantidade(), i.getPrecoUnitario())
        ).collect(Collectors.toList());
        return new PedidoResponse(p.getId(), p.getClienteId(), p.getTotal(),
                p.getDataCriacao(), itens);
    }
}
