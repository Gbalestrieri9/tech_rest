package com.techchallenge.cliente.exception;

public class ClienteJaExisteException extends RuntimeException {
    public ClienteJaExisteException(String cpf) {
        super("Cliente com CPF " + cpf + " já existe");
    }
}
