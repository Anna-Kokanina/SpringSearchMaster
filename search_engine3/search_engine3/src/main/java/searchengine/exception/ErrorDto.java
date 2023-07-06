package searchengine.exception;

import org.springframework.http.HttpStatus;

// ErrorDto is a Data Transfer Object that encapsulates an error message and an HTTP status code.
public class ErrorDto {
    // The HTTP status code associated with the error.
    private HttpStatus status;

    // The error message.
    private String message;

    // Constructor that initializes the status and message.
    public ErrorDto(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    // Returns a string representation of the ErrorDto object.
    @Override
    public String toString() {
        return "ErrorDto{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }

    // Returns the HTTP status code.
    public HttpStatus getStatus() {
        return status;
    }

    // Sets the HTTP status code.
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    // Returns the error message.
    public String getMessage() {
        return message;
    }

    // Sets the error message.
    public void setMessage(String message) {
        this.message = message;
    }
}
