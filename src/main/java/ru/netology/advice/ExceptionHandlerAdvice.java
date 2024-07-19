package ru.netology.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.exceptions.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    private static final AtomicInteger errorId = new AtomicInteger(1);

    @ExceptionHandler(value = {BindException.class, BadCredentialsException.class, IOException.class})
    public ResponseEntity<ErrorInResponse> handleBindException(Exception e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorInResponse(e.getMessage(), errorId.getAndIncrement()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorInResponse> handleAuthorizationException(AuthorizationException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorInResponse(e.getMessage(), errorId.getAndIncrement()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorInResponse> handlerException(FileUploadException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorInResponse(e.getMessage(), errorId.getAndIncrement()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeleteFileException.class)
    public ResponseEntity<ErrorInResponse> handlerException(DeleteFileException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorInResponse(e.getMessage(), errorId.getAndIncrement()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RenameFileException.class)
    public ResponseEntity<ErrorInResponse> handlerException(RenameFileException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorInResponse(e.getMessage(), errorId.getAndIncrement()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorInResponse> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorInResponse(e.getMessage(), errorId.getAndIncrement()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
