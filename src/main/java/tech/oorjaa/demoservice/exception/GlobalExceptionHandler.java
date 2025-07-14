package tech.oorjaa.demoservice.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import tech.oorjaa.demoservice.dto.ErrorResponse;
import tech.oorjaa.demoservice.dto.StandardResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler to provide consistent error responses across the application
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles validation errors from @Valid annotations
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardResponse<Map<String, Object>>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        StandardResponse<Map<String, Object>> response = StandardResponse.<Map<String, Object>>builder()
                .success(false)
                .message("Validation error")
                .data(errors)
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles entity not found exceptions
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardResponse<Void>> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        log.error("Entity not found", ex);
        StandardResponse<Void> response = StandardResponse.<Void>builder()
                .success(false)
                .message(ex.getMessage())
                .error("Entity not found")
                .path(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles access denied exceptions
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<StandardResponse<Void>> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        log.error("Access denied", ex);
        StandardResponse<Void> response = StandardResponse.<Void>builder()
                .success(false)
                .message("Access denied")
                .error(ex.getMessage())
                .path(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Handles no handler found exceptions (404)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardResponse<Map<String, Object>>> handleNoHandlerFound(NoHandlerFoundException ex, WebRequest request) {
        StandardResponse<Map<String, Object>> response = StandardResponse.<Map<String, Object>>builder()
                .success(false)
                .message("Resource not found")
                .error("No handler found for request")
                .data(Map.of("path", ex.getRequestURL()))
                .path(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles constraint violation exceptions
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardResponse<Map<String, Object>>> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        });

        StandardResponse<Map<String, Object>> response = StandardResponse.<Map<String, Object>>builder()
                .success(false)
                .message("Constraint violation")
                .error("Validation constraints violated")
                .data(errors)
                .path(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Fallback exception handler for any unhandled exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<StandardResponse<Void>> handleAllUncaughtException(Exception ex, WebRequest request) {
        log.error("Unexpected error", ex);
        StandardResponse<Void> response = StandardResponse.<Void>builder()
                .success(false)
                .message("An unexpected error occurred")
                .error("Internal server error")
                .path(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
