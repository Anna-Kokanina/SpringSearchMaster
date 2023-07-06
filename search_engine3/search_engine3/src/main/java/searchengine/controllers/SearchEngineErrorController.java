package searchengine.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import searchengine.exception.ErrorMessage;

@RestControllerAdvice
public class SearchEngineErrorController {

    // Exception handler for NullPointerException
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> nullPointerException(NullPointerException exception) {
        // Create and return a custom error message
        return createErrorMessage(HttpStatus.NOT_FOUND, "Search info is not in database" + exception.getMessage());
    }

    private ResponseEntity<ErrorMessage> createErrorMessage(HttpStatus status, String message) {
        // Create a ResponseEntity with the provided HTTP status and error message
        return ResponseEntity.status(status).body(new ErrorMessage(message));
    }
}
