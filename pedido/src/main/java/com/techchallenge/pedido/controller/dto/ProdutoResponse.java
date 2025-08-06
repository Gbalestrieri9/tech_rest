package com.techchallenge.pedido.controller.dto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        String categoria,
        Boolean ativo,
        String sku
) {}
