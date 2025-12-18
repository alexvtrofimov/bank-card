package com.example.bankcards.controller;

import com.example.bankcards.exception.BalanceException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserAlreadyExist;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;


@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ResponseApiError {

        private String message;

        private String debugMessage;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<String> errors;

        public ResponseApiError(String message, String debugMessage){
            this.message=message;
            this.debugMessage=debugMessage;
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
        ObjectError firstError = allErrors.getFirst();
        ResponseApiError responseError = new ResponseApiError(firstError.getDefaultMessage(), firstError.toString());
        responseError.setErrors(allErrors.stream().map(e -> e.getDefaultMessage()).toList());
        return ResponseEntity
                .status(exception.getBody().getStatus())
                .body(responseError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseApiError> handlerNNumberFormatException(MethodArgumentTypeMismatchException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseApiError(exception.getMessage(), ""));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseApiError> handleBadCredentialsException(BadCredentialsException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseApiError(exception.getMessage(), ""));
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ResponseApiError> handleCardNotFoundException(CardNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseApiError(exception.getMessage(), exception.getDebugMessage()));
    }

    @ExceptionHandler(BalanceException.class)
    public ResponseEntity<ResponseApiError> handleBalanceException(BalanceException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseApiError(exception.getMessage(), exception.getDebugMessage()));
    }

    @ExceptionHandler(UserAlreadyExist.class)
    public ResponseEntity<ResponseApiError> handleUserAlreadyExistException(UserAlreadyExist exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ResponseApiError(exception.getMessage(), exception.getDebugMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseApiError> handleUnknownError(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseApiError(exception.getMessage(), ""));
    }
}
