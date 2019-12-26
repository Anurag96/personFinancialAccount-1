package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class SecurityConfigurationException extends RuntimeException {

    public SecurityConfigurationException() {
        super("Security Information mismatch for consumerKey. Please notify the Business Entity Service");
    }
}
