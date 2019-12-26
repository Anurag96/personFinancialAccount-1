package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DuplicateCheckException extends RuntimeException {

    public DuplicateCheckException(String message) {
        super(message);
    }
}
