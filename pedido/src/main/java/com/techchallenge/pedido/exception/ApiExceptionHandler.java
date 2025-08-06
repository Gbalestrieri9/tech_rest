package com.techchallenge.pedido.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<?> handleNotFound(RecursoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(FalhaIntegracaoException.class)
    public ResponseEntity<?> handleIntegration(FalhaIntegracaoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(error(HttpStatus.BAD_GATEWAY, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        var campos = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> Map.of("campo", f.getField(), "erro", f.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", Instant.now().toString(),
                "status", 400,
                "error", "VALIDATION_FAILED",
                "fields", campos
        ));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleHttpClient(HttpClientErrorException ex) {
        HttpStatus status = ex.getStatusCode().is4xxClientError()
                ? (HttpStatus) ex.getStatusCode()
                : HttpStatus.BAD_GATEWAY;
        return ResponseEntity.status(status)
                .body(error(status, ex.getStatusText()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno"));
    }

    private Map<String,Object> error(HttpStatus status, String msg) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", msg
        );
    }
}
