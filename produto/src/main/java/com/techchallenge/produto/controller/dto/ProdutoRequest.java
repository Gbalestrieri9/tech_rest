package com.techchallenge.produto.controller.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank String nome,
        String descricao,
        @NotNull @DecimalMin(value = "0.00") BigDecimal preco,
        String categoria,
        Boolean ativo,
        String sku
) {}