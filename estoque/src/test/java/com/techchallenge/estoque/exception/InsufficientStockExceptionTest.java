package com.techchallenge.estoque.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsufficientStockExceptionTest {

    @Test
    void deveCriarExcecaoComMensagem() {
        String mensagem = "Estoque insuficiente para produto 1";

        InsufficientStockException exception = new InsufficientStockException(mensagem);

        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void deveCriarExcecaoComMensagemNula() {
        InsufficientStockException exception = new InsufficientStockException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void deveCriarExcecaoComMensagemVazia() {
        String mensagem = "";

        InsufficientStockException exception = new InsufficientStockException(mensagem);

        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    void deveSerSubclasseDeRuntimeException() {
        InsufficientStockException exception = new InsufficientStockException("Teste");

        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }
}