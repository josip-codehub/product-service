package com.ingemark.demo.products.exception.handler;

import com.ingemark.demo.products.exception.BaseException;
import com.ingemark.demo.products.exception.model.ValidationErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> handleException(final BaseException ex) {
        log.error(
                ex.getMessage(),
                ex
        );
        return ResponseEntity
                .status(ex.getStatus())
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(final MethodArgumentNotValidException ex) {
        final var errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        log.error(
                "Validation error: {}\nwith errors: {}",
                ex.getMessage(),
                String.join("\n", errors),
                ex
        );

        final ValidationErrorResponse validationError = new ValidationErrorResponse(
                "Validation failed",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(validationError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(final Exception ex) {
        log.error(
                ex.getMessage(),
                ex
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }
}
