package com.techchallenge.cliente.controller.dto;

import java.time.LocalDate;
import java.util.List;

public record ClienteResponse(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        List<EnderecoResponse> enderecos
) {}
