package com.techchallenge.cliente.domain.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
public class Cliente {
    private final Long id;
    private final String nome;
    private final String cpf;
    private final LocalDate dataNascimento;
    private final List<Endereco> enderecos;

    public Cliente withId(Long id){
        return Cliente.builder()
                .id(id)
                .nome(nome)
                .cpf(cpf)
                .dataNascimento(dataNascimento)
                .enderecos(enderecos)
                .build();
    }
}
