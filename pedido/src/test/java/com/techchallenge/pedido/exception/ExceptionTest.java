package com.techchallenge.pedido.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void recursoNaoEncontradoException_DeveArmazenarMensagemCorretamente() {
        String mensagem = "Recurso não encontrado";

        RecursoNaoEncontradoException exception = new RecursoNaoEncontradoException(mensagem);

        assertEquals(mensagem, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void falhaIntegracaoException_DeveArmazenarMensagemCorretamente() {
        String mensagem = "Falha na integração";

        FalhaIntegracaoException exception = new FalhaIntegracaoException(mensagem);

        assertEquals(mensagem, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }
}