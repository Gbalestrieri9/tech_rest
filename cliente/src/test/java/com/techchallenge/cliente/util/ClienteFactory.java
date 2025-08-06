package com.techchallenge.cliente.util;

import com.techchallenge.cliente.domain.entity.Cliente;
import com.techchallenge.cliente.domain.entity.Endereco;

import java.time.LocalDate;
import java.util.List;

public final class ClienteFactory {
    private ClienteFactory(){}

    public static Cliente clienteValidoSemId(){
        return Cliente.builder()
                .id(null)
                .nome("Fulano de Tal")
                .cpf("390.533.447-05") // CPF válido
                .dataNascimento(LocalDate.of(1990,1,1))
                .enderecos(List.of(endereco()))
                .build();
    }

    public static Endereco endereco(){
        return Endereco.builder()
                .logradouro("Rua A")
                .numero("10")
                .complemento("Ap 1")
                .cep("01001000")
                .cidade("São Paulo")
                .uf("SP")
                .build();
    }
}
