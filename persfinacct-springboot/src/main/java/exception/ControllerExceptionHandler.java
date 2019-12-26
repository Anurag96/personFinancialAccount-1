package exception;

import com.esrx.services.personfinancialaccounts.models.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse response = ErrorResponse
                .builder()
                .status(status.value())
                .error(status.name())
                .title("Malformed JSON request")
                .message("Invalid Request: could not parse.")
                .build();
        return new ResponseEntity<>(response, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        StringBuilder sb = new StringBuilder();
        String missingFields = ex.getBindingResult()
                .getFieldErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).
                        collect(Collectors.joining(", ")).toString();
        sb.append(missingFields);
        sb.append(ex.getBindingResult()
                .getFieldErrors().size() > 1 ? " are " : " is ");
        sb.append("missing or invalid.");
        ErrorResponse response = ErrorResponse
                .builder()
                .status(status.value())
                .error(status.name())
                .title("Mandatory values missing")
                .message(sb.toString())
                .build();
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(ParametersRequiredException.class)
    protected ResponseEntity<Object> handleParametersRequiredException(ParametersRequiredException ex) {
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .title("Mandatory values missing")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataValidationException.class)
    protected ResponseEntity<Object> handleDataValidationException(DataValidationException ex) {
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .title("Data validation failed")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateCheckException.class)
    protected ResponseEntity<Object> handleDuplicateCheckException(DuplicateCheckException ex) {
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.name())
                .title("Duplicate request")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.name())
                .title("Resource not found")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonFinancialAccountNotFoundException.class)
    protected ResponseEntity<Object> handlePersonFinancialAccountNotFoundException(PersonFinancialAccountNotFoundException ex) {
        logger.debug("Exception encountered:", ex);
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.name())
                .title("Resource not found")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SecurityConfigurationException.class)
    protected ResponseEntity<Object> handleSecurityConfigurationException(SecurityConfigurationException ex) {
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.name())
                .title("Authentication failed")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultExceptionHandler(Exception ex) {
        logger.error("Exception encountered:", ex);
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .title("An error occurred in the application")
                .message("An error occurred in the application")
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}