package searchengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiErrorController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleInternalServerError(Exception exception) {
        // Log the exception or perform any necessary error handling steps

        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred.");
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException(NullPointerException exception) {
        // Log the exception or perform any necessary error handling steps

        return createErrorResponse(HttpStatus.NOT_FOUND, "The requested resource was not found.");
    }

    // Add more exception handler methods for specific exceptions as needed

    private ResponseEntity<ErrorDto> createErrorResponse(HttpStatus status, String message) {
        // Create an ErrorDto with the provided HTTP status and error message
        ErrorDto errorDto = new ErrorDto(status, message);
        return ResponseEntity.status(status).body(errorDto);
    }
}
