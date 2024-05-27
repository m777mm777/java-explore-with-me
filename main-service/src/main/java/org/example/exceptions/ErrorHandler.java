package org.example.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(final ValidationException e) {
        log.debug("Получен статус 400 Bad request {}", e.getMessage(), e);

        List<String> errors = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(errors, e.getMessage(), "Валидация не пройдена",
                        HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 Bad request {}", e.getMessage(), e);

        List<String> errors = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(errors, e.getMessage(), "Incorrectly made request.",
                        HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(final ResourceNotFoundException e) {
        log.debug("Получен статус 404 Not found {}", e.getStackTrace(), e);

        List<String> errors = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());

                return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                        .body(new ApiError(errors, e.getMessage(), "Incorrectly made request.",
                                HttpStatus.NOT_FOUND, LocalDateTime.now()));

    }



    @ExceptionHandler(ResourceServerError.class)
    public ResponseEntity<ApiError> handleServerError(final ResourceServerError e) {
        log.debug("Получен статус 500 Server error {}", e.getMessage(), e);

        List<String> errors = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(errors, e.getMessage(), "INTERNAL_SERVER_1ERROR",
                        HttpStatus.NOT_FOUND, LocalDateTime.now()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> unknownException(final Throwable e) {
        log.debug("Получен статус 400 BAD REQUEST {}", e.getMessage(), e);

        List<String> errors = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(errors, e.getMessage(), "Incorrectly made request.",
                        HttpStatus.BAD_REQUEST, LocalDateTime.now()));

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleBadRequest(final ConstraintViolationException e) {
        log.debug("Получен статус 400 Bad request {}", e.getMessage(), e);

        List<String> errors = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(errors, e.getMessage(), "BAD_REQUEST2",
                        HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now()));
    }

    @ExceptionHandler(ConflictServerError.class)
    public ResponseEntity<ApiError> handleConflictServerError(final ConflictServerError e) {
        log.debug("Получен статус 409 CONFLICT {}", e.getMessage(), e);

        List<String> errors = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError(errors, e.getMessage(), "CONFLICT",
                        HttpStatus.CONFLICT, LocalDateTime.now()));

    }
}