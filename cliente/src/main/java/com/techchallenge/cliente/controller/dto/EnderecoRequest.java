package com.techchallenge.cliente.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record EnderecoRequest(
        @NotBlank String logradouro,
        @NotBlank String numero,
        String complemento,
        @NotBlank String cep,
        @NotBlank String cidade,
        @NotBlank String uf
) {}
