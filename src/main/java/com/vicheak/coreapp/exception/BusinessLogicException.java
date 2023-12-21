package com.vicheak.coreapp.exception;

import com.vicheak.coreapp.base.BaseError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class BusinessLogicException {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex) {
        var baseError = BaseError.builder()
                .message("Something went wrong!")
                .localDateTime(LocalDateTime.now())
                .status(false)
                .errors(ex.getMessage())
                .build();

        return new ResponseEntity<>(baseError, ex.getStatusCode());
    }

}
