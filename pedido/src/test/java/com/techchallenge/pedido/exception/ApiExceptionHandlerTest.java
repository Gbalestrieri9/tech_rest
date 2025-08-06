package com.techchallenge.pedido.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiExceptionHandlerTest {

    private ApiExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ApiExceptionHandler();
    }

    @Test
    void handleNotFound_DeveRetornarNotFound() {
        RecursoNaoEncontradoException exception = new RecursoNaoEncontradoException("Recurso não encontrado");

        ResponseEntity<?> response = exceptionHandler.handleNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(404, body.get("status"));
        assertEquals("Recurso não encontrado", body.get("error"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleIntegration_DeveRetornarBadGateway() {
        FalhaIntegracaoException exception = new FalhaIntegracaoException("Falha na integração");

        ResponseEntity<?> response = exceptionHandler.handleIntegration(exception);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(502, body.get("status"));
        assertEquals("Falha na integração", body.get("error"));
    }

    @Test
    void handleValidation_DeveRetornarBadRequest() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objeto", "campo", "mensagem de erro");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<?> response = exceptionHandler.handleValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(400, body.get("status"));
        assertEquals("VALIDATION_FAILED", body.get("error"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> fields = (List<Map<String, Object>>) body.get("fields");
        assertEquals(1, fields.size());
        assertEquals("campo", fields.get(0).get("campo"));
        assertEquals("mensagem de erro", fields.get(0).get("erro"));
    }

    @Test
    void handleHttpClient_DeveRetornarStatusCorretoParaErro4xx() {
        HttpClientErrorException exception = HttpClientErrorException.BadRequest.create(
                HttpStatus.BAD_REQUEST, "Bad Request", null, null, null);

        ResponseEntity<?> response = exceptionHandler.handleHttpClient(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(400, body.get("status"));
    }

    @Test
    void handleHttpClient_DeveRetornarBadGatewayParaErro5xx() {
        HttpClientErrorException exception = HttpClientErrorException.BadRequest.create(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null, null, null);

        ResponseEntity<?> response = exceptionHandler.handleHttpClient(exception);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(502, body.get("status"));
    }

    @Test
    void handleAll_DeveRetornarInternalServerError() {
        Exception exception = new RuntimeException("Erro genérico");

        ResponseEntity<?> response = exceptionHandler.handleAll(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(500, body.get("status"));
        assertEquals("Erro interno", body.get("error"));
    }
}