package com.tasks.crud.Controller;

import com.tasks.crud.DTO.ApiRes;
import com.tasks.crud.Exception.ResourceNotFoundException;
import com.tasks.crud.Util.ResponseCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiRes<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        ApiRes<Void> apiRes = ApiRes.<Void>builder()
                .code(ResponseCodeUtil.USER_NOT_FOUND_ERROR_CODE)
                .title("NOT FOUND")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(apiRes, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiRes<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("Validation failed: {}", errors);

        ApiRes<Map<String, String>> apiRes = ApiRes.<Map<String, String>>builder()
                .code(ResponseCodeUtil.INPUT_VALIDATION_ERROR_CODE)
                .title("VALIDATION FAILED")
                .message("Input validation error")
                .data(errors)
                .build();
        return new ResponseEntity<>(apiRes, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiRes<Void>> handleGenericException(Exception ex) {
        log.error("Internal server error occurred", ex);
        ApiRes<Void> apiRes = ApiRes.<Void>builder()
                .code(ResponseCodeUtil.INTERNAL_SERVER_ERROR_CODE)
                .title("FAILED")
                .message("An unexpected error occurred: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(apiRes, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
