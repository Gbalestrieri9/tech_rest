package com.techchallenge.cliente.domain.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class Endereco {
    private final String logradouro;
    private final String numero;
    private final String complemento;
    private final String cep;
    private final String cidade;
    private final String uf;
}
