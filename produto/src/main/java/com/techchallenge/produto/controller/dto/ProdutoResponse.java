package com.techchallenge.produto.controller.dto;

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
