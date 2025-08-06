package com.techchallenge.estoque.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EstoqueRequest(
        @NotNull Long produtoId,
        @Min(0) Integer quantidade
) {}
