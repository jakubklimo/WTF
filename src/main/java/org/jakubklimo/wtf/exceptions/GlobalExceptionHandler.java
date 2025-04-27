package org.jakubklimo.wtf.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Entity Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                Instant.now().toString()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Internal Server Error",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Instant.now().toString()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    public static class ErrorResponse {
        private String error;
        private String message;
        private int status;
        private String timestamp;

        public ErrorResponse(String error, String message, int status, String timestamp) {
            this.error = error;
            this.message = message;
            this.status = status;
            this.timestamp = timestamp;
        }

        public String getError() { return error; }
        public String getMessage() { return message; }
        public int getStatus() { return status; }
        public String getTimestamp() { return timestamp; }
    }
}
