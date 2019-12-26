package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RetryProcessException extends RuntimeException {

    public RetryProcessException(String message) {
        super(message);
    }
}
