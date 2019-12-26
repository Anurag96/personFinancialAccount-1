package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonFinancialAccountNotFoundException extends RuntimeException {
    public PersonFinancialAccountNotFoundException(String id) {
        super("Could not find " + id);
    }
}
