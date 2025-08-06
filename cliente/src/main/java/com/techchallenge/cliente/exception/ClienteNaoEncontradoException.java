package com.techchallenge.cliente.exception;

public class ClienteNaoEncontradoException extends RuntimeException {
    public ClienteNaoEncontradoException(Long id) {
        super("Cliente id " + id + " não encontrado");
    }
}
