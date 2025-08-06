package com.techchallenge.cliente.controller.dto;

public record EnderecoResponse(
        String logradouro,
        String numero,
        String complemento,
        String cep,
        String cidade,
        String uf
) {}
