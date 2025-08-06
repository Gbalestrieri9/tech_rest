package com.techchallenge.estoque.exception;

import com.techchallenge.estoque.exception.InsufficientStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void deveTratarIllegalArgumentException() {
        String mensagem = "Estoque para produto 1 já existe";
        IllegalArgumentException exception = new IllegalArgumentException(mensagem);

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals(mensagem, response.getBody().get("message"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void deveTratarInsufficientStockException() {
        String mensagem = "Estoque insuficiente para produto 1";
        InsufficientStockException exception = new InsufficientStockException(mensagem);

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleInsufficientStockException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().get("status"));
        assertEquals("Conflict", response.getBody().get("error"));
        assertEquals(mensagem, response.getBody().get("message"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void deveTratarIllegalArgumentExceptionComMensagemNula() {
        IllegalArgumentException exception = new IllegalArgumentException((String) null);

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().get("message"));
    }

    @Test
    void deveTratarInsufficientStockExceptionComMensagemNula() {
        InsufficientStockException exception = new InsufficientStockException(null);

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleInsufficientStockException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().get("message"));
    }
}
