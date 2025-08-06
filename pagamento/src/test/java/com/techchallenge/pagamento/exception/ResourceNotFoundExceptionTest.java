package com.techchallenge.pagamento.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void resourceNotFoundException_ShouldCreateWithMessage() {
        String message = "Resource not found";

        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void resourceNotFoundException_ShouldCreateWithNullMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException(null);

        assertNull(exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void resourceNotFoundException_ShouldCreateWithEmptyMessage() {
        String message = "";

        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertTrue(exception.getMessage().isEmpty());
    }

    @Test
    void resourceNotFoundException_ShouldBeThrowable() {
        String message = "Test exception";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        assertThrows(ResourceNotFoundException.class, () -> {
            throw exception;
        });
    }

    @Test
    void resourceNotFoundException_ShouldInheritFromRuntimeException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("test");

        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }
}