package controllers;

import com.esrx.services.personfinancialaccounts.exception.DataValidationException;
import com.esrx.services.personfinancialaccounts.exception.ParametersRequiredException;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.processors.PostProcessor;
import com.esrx.services.personfinancialaccounts.services.PersonFinancialAccountService;
import com.esrx.services.personfinancialaccounts.util.TestUtils;
import com.esrx.services.personfinancialaccounts.util.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = PersonFinancialAccountController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class}, secure = false)
@ActiveProfiles(profiles = "local")
public class PersonFinancialAccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PersonFinancialAccountService personFinancialAccountService;

    @MockBean
    private Utils utils;

    @Mock
    private PersonFinancialAccountController controller;

    @MockBean
    private PostProcessor postProcessor;

    private UUID resourceId = UUID.randomUUID();

    @Test
    public void createfinacc_API() throws Exception {
        PersonFinancialAccount finacc = TestUtils.createFinAccObject();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/personFinancialAccounts")
                .content(TestUtils.asJsonString(finacc))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test(expected = NumberFormatException.class)
    public void createfinaccWhenInvalidResourceId() throws Exception {
        PersonFinancialAccount finacc = TestUtils.createFinAccObject();
        finacc.getAccountHolder().setPersonResourceId(UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9wxyz"));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/personFinancialAccounts")
                .content(TestUtils.asJsonString(finacc))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createfinaccWhenEmptyResourceId() throws Exception {
        PersonFinancialAccount finacc = TestUtils.createFinAccObject();
        finacc.getAccountHolder().setPersonResourceId(null);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/personFinancialAccounts")
                .content(TestUtils.asJsonString(finacc))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findPersonFinancialAccountByIndividualAGN() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("individualAGN", "04566666667");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findPersonFinancialAccountByCustomerNumber() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("customerNumber", "U344444449");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findPersonFinancialAccountByMailGroup() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("mailGroup", "PD2");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findPersonFinancialAccountBySubGroup() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("subGroup", "KHXB");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findPersonFinancialAccountWithMailGroupAndSubGroup() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("mailGroup", "PD2");
        lStringStringMultiValueMap.add("subGroup", "KHXB");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findPersonFinancialAccountWithMailGroupAndSubGroupBlankCustomerNumber() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("customerNumber", " ");
        lStringStringMultiValueMap.add("mailGroup", "PD2");
        lStringStringMultiValueMap.add("subGroup", "KHXB");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findPersonFinancialAccountWithoutWithCustomerNumberAndMailGroupAndSubGroup() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("customerNumber", "U344444449");
        lStringStringMultiValueMap.add("mailGroup", "PD2");
        lStringStringMultiValueMap.add("subGroup", "KHXB");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findPersonFinancialAccountWithoutAnySearchCriteria() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findPersonFinancialAccountWithAllParams() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("customerNumber", "U344444449");
        lStringStringMultiValueMap.add("mailGroup", "PD2");
        lStringStringMultiValueMap.add("subGroup", "KHXB");
        lStringStringMultiValueMap.add("individualAGN", "04566666667");
        lStringStringMultiValueMap.add("membershipId", "045669234345");
        lStringStringMultiValueMap.add("personResourceId", "d16324e4-08c0-4ccd-ac23-7aa2add97011");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findPersonFinancialAccountWithPersonResourceId() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("personResourceId", "d16324e4-08c0-4ccd-ac23-7aa2add97011");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findPersonFinancialAccountWithInvalidpersonResourceId() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("personResourceId", "k16324e4-08c0-4ccd-ac23-7aa2add97011");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findPersonFinancialAccountWithMembershipId() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("membershipId", "045669234345");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findPersonFinancialAccountWithInvalidMembershipId() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("membershipId", " ");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findPersonFinancialAccountWithBlankValues() throws Exception {
        MultiValueMap<String, String> lStringStringMultiValueMap = new LinkedMultiValueMap<>();
        lStringStringMultiValueMap.add("customerNumber", " ");
        lStringStringMultiValueMap.add("mailGroup", " ");
        lStringStringMultiValueMap.add("subGroup", " ");
        lStringStringMultiValueMap.add("individualAGN", " ");
        lStringStringMultiValueMap.add("membershipId", " ");
        lStringStringMultiValueMap.add("personResourceId", " ");

        mockMvc.perform(get("/v1/personFinancialAccounts/")
                .params(lStringStringMultiValueMap)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatefinacc_API() throws Exception {
        PersonFinancialAccount finacc = TestUtils.createFinAccObject();
        finacc.setResourceId(resourceId);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/personFinancialAccounts/" + finacc.getResourceId())
                .content(TestUtils.asJsonString(finacc))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test(expected = NumberFormatException.class)
    public void updatefinaccWhenInvalidPersonResourceId() throws Exception {
        PersonFinancialAccount finacc = TestUtils.createFinAccObject();
        finacc.setResourceId(resourceId);
        finacc.getAccountHolder().setPersonResourceId(UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9wxyz"));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/personFinancialAccounts/" + finacc.getResourceId())
                .content(TestUtils.asJsonString(finacc))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    public void updatefinaccWhenEmptyPersonResourceId() throws Exception {
        PersonFinancialAccount finacc = TestUtils.createFinAccObject();
        finacc.setResourceId(resourceId);
        finacc.getAccountHolder().setPersonResourceId(null);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/personFinancialAccounts/" + finacc.getResourceId())
                .content(TestUtils.asJsonString(finacc))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatefinaccWhenNullResourceId() throws Exception {
        PersonFinancialAccount finacc = TestUtils.createFinAccObject();
        finacc.setResourceId(null);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/personFinancialAccounts/" + finacc.getResourceId())
                .content(TestUtils.asJsonString(finacc))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/v1/personFinancialAccounts/" + resourceId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteWithInvalidResourceId() throws Exception {
        doThrow(new ParametersRequiredException("Please provide a valid Resource Id.")).when(personFinancialAccountService).delete(any(), any(), any());

        mockMvc.perform(delete("/v1/personFinancialAccounts/kk")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Please provide a valid Resource Id.")));
    }

    @Test
    public void testDeleteWithDatabaseException() throws Exception {
        doThrow(new RuntimeException()).when(personFinancialAccountService).delete(any(), any(), any());

        mockMvc.perform(delete("/v1/personFinancialAccounts/" + resourceId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("An error occurred in the application")));
    }

    @Test
    public void deleteNotValidResourceId() throws Exception {
        doThrow(new DataValidationException("")).when(personFinancialAccountService).delete(any(), any(), any());

        mockMvc.perform(delete("/v1/personFinancialAccounts/" + resourceId).content(TestUtils.getItemAsString(TestUtils.createFinAccObject()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
