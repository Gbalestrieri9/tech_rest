package com.techchallenge.pagamento.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiExceptionHandlerSimplifiedTest {

    private final ApiExceptionHandler apiExceptionHandler = new ApiExceptionHandler();

    @Test
    void handleValidation_ShouldReturnBadRequestWithFieldErrors() {
        MethodArgumentNotValidException mockException = mock(MethodArgumentNotValidException.class);
        BindingResult mockBindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("object", "field1", "Error message 1");
        FieldError fieldError2 = new FieldError("object", "field2", "Error message 2");
        List<FieldError> fieldErrors = List.of(fieldError1, fieldError2);

        when(mockException.getBindingResult()).thenReturn(mockBindingResult);
        when(mockBindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<?> response = apiExceptionHandler.handleValidation(mockException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(400, responseBody.get("status"));
        assertEquals("VALIDATION_FAILED", responseBody.get("error"));
        assertNotNull(responseBody.get("timestamp"));
        assertNotNull(responseBody.get("fields"));

        @SuppressWarnings("unchecked")
        List<Map<String, String>> fields = (List<Map<String, String>>) responseBody.get("fields");
        assertEquals(2, fields.size());
    }

    @Test
    void handleNotFound_ShouldReturnNotFoundWithMessage() {
        String errorMessage = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        ResponseEntity<?> response = apiExceptionHandler.handleNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(404, responseBody.get("status"));
        assertEquals(errorMessage, responseBody.get("error"));
        assertNotNull(responseBody.get("timestamp"));
    }

    @Test
    void handleAll_ShouldReturnInternalServerError() {
        Exception exception = new RuntimeException("Unexpected error");

        ResponseEntity<?> response = apiExceptionHandler.handleAll(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(500, responseBody.get("status"));
        assertEquals("Erro interno", responseBody.get("error"));
        assertNotNull(responseBody.get("timestamp"));
    }

    @Test
    void handleValidation_WithEmptyFieldErrors_ShouldReturnEmptyFieldsList() {
        MethodArgumentNotValidException mockException = mock(MethodArgumentNotValidException.class);
        BindingResult mockBindingResult = mock(BindingResult.class);

        when(mockException.getBindingResult()).thenReturn(mockBindingResult);
        when(mockBindingResult.getFieldErrors()).thenReturn(List.of());

        ResponseEntity<?> response = apiExceptionHandler.handleValidation(mockException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        @SuppressWarnings("unchecked")
        List<Map<String, String>> fields = (List<Map<String, String>>) responseBody.get("fields");
        assertTrue(fields.isEmpty());
    }

    @Test
    void handleAll_WithNullPointerException_ShouldReturnInternalServerError() {
        NullPointerException exception = new NullPointerException("Null pointer");

        ResponseEntity<?> response = apiExceptionHandler.handleAll(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(500, responseBody.get("status"));
        assertEquals("Erro interno", responseBody.get("error"));
    }
}