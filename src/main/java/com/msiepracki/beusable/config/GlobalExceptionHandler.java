package com.msiepracki.beusable.config;

import com.msiepracki.beusable.config.validation.ValidationError;
import com.msiepracki.beusable.config.validation.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        List<ObjectError> objectErrors = exception.getBindingResult().getAllErrors();
        List<ValidationError> errors = objectErrors.stream()
                .map(this::buildValidationErrorResponse)
                .toList();
        ValidationErrorResponse validationErrorResponse = ValidationErrorResponse.builder()
                .errors(errors)
                .build();
        return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
    }

    private ValidationError buildValidationErrorResponse(ObjectError error) {
        return ValidationError.builder()
                .field(((FieldError) error).getField())
                .message(error.getDefaultMessage())
                .build();
    }
}
