package com.techchallenge.estoque.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReservaRequest(
        @NotNull @Min(1) Integer quantidade
) {}
