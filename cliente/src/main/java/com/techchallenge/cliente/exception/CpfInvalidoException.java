package com.techchallenge.cliente.exception;

public class CpfInvalidoException extends RuntimeException {
    public CpfInvalidoException(String cpf) {
        super("CPF inválido: " + cpf);
    }
}
