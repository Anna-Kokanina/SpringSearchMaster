package searchengine.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// ErrorMessage is a class that encapsulates an error message.
@Setter
@Getter
@AllArgsConstructor
public class ErrorMessage {
    // The error message associated with the exception.
    private String errorMessage;
}
