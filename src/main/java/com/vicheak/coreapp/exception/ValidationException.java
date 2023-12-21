package com.vicheak.coreapp.exception;

import com.vicheak.coreapp.base.BaseError;
import com.vicheak.coreapp.base.FieldError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> errors = new ArrayList<>();

        ex.getFieldErrors().forEach(fieldError -> errors.add(
                FieldError.builder()
                        .fieldName(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build()
        ));

        BaseError<Object> baseError = BaseError.builder()
                .message("Something went wrong!")
                .localDateTime(LocalDateTime.now())
                .status(false)
                .errors(errors)
                .build();

        return new ResponseEntity<>(baseError, HttpStatus.BAD_REQUEST);
    }

}
