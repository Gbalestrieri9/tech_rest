package com.techchallenge.cliente.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.List;

public record ClienteRequest(
        @NotBlank String nome,
        @NotBlank String cpf,
        @NotNull @Past LocalDate dataNascimento,
        @NotEmpty List<@Valid EnderecoRequest> enderecos
) {}
