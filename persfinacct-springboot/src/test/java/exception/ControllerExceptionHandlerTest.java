package exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ControllerExceptionHandler.class)
public class ControllerExceptionHandlerTest {

    @Autowired
    private ControllerExceptionHandler controllerExceptionHandler;

    @MockBean
    private PersonFinancialAccountNotFoundException personFinancialAccountNotFoundException;

    @Test
    public void handlePersonFinancialAccountNotFoundException() throws Exception {
        ResponseEntity<Object> response = controllerExceptionHandler.handlePersonFinancialAccountNotFoundException(personFinancialAccountNotFoundException);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}