package controllers;

import com.esrx.services.personfinancialaccounts.exception.ParametersRequiredException;
import com.esrx.services.personfinancialaccounts.exception.PersonFinancialAccountNotFoundException;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.models.SearchParameter;
import com.esrx.services.personfinancialaccounts.processors.PFAProcessor;
import com.esrx.services.personfinancialaccounts.processors.PostProcessor;
import com.esrx.services.personfinancialaccounts.services.PersonFinancialAccountService;
import com.esrx.services.personfinancialaccounts.util.TestUtils;
import com.esrx.services.personfinancialaccounts.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RunWith(MockitoJUnitRunner.class)
public class PersonFinancialAccountControllerUnitTest {

    @Mock
    private PersonFinancialAccountService service;

    @Spy
    Utils utils = new Utils();

    @InjectMocks
    private PersonFinancialAccountController controller;

    @Mock
    private PostProcessor postProcessor;

    @Mock
    private PFAProcessor pfaProcessor;

    @Before
    public void setup() throws Exception {
    }

    @Test
    public void getPersonFinancialAccountById() {
        PersonFinancialAccount expected = TestUtils.createFinAccObject();
        expected.setResourceId(UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));

        when(service.findOne(any(UUID.class))).thenReturn(expected);
        PersonFinancialAccount results = controller.findById(UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
        assertNotEquals(results, null);
        assertEquals(results.getResourceId(), UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
    }

    @Test(expected = PersonFinancialAccountNotFoundException.class)
    public void getPersonFinancialAccountByIdNotFound() {
        when(service.findOne(any(UUID.class))).thenThrow(new PersonFinancialAccountNotFoundException("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));

        controller.findById(UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPersonFinancialAccountByIdIsNotUUID() {
        when(service.findOne(any(UUID.class))).thenThrow(new IllegalArgumentException());

        controller.findById(UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add970"));
    }

    @Test
    public void createPersonFinancialAccount() {
        PersonFinancialAccount accDetails = TestUtils.createFinAccObject();
        accDetails.setResourceId(UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));

        when(service.insert(any(), any())).thenReturn(accDetails);
        PersonFinancialAccount results = controller.create(new HashMap<>(), accDetails);

        assertNotNull(results);
        assertEquals(results.getResourceId(), UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
    }

    @Test(expected = NumberFormatException.class)
    public void updatePersonFinancialAccountByIdIsNotUUID() {
        controller.update(new HashMap<>(), UUID.fromString("d16324e4-08c0-kccd-ac23-7aa2add9702a"), new PersonFinancialAccount());
    }

    @Test
    public void findPersonFinancialAccountWithAllParams() {
        PersonFinancialAccount expected = TestUtils.createFinAccObject();
        expected.setResourceId(UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
        expected.add(linkTo(methodOn(PersonFinancialAccountController.class).findById(expected.getResourceId())).withSelfRel());

        when(postProcessor.addLinks(any(PersonFinancialAccount.class))).thenReturn(expected);
        when(service.findAll(any(SearchParameter.class))).thenReturn(Arrays.asList(expected));

        List<PersonFinancialAccount> results = controller.findPersonFinancialAccount("d16324e4-08c0-4ccd-ac23-7aa2add97011", "04566666667", "U344444449", "PD2", "KHXB", "045669234345");
        assertNotEquals(results, null);
        assertEquals(results.get(0).getResourceId(), UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
    }

    @Test(expected = ParametersRequiredException.class)
    public void findPersonFinancialAccountWithNoParams() {
        controller.findPersonFinancialAccount(null, null, null, null, null, null);
    }

    @Test(expected = ParametersRequiredException.class)
    public void findPersonFinancialAccountWithInvalidPersonResourceIdWithAllParams() {
        controller.findPersonFinancialAccount("k16324e4-08c0-4ccd-ac23-7aa2add97011", "04566666667", "U344444449", "PD2", "PD2", "045669234345");
    }

    @Test(expected = ParametersRequiredException.class)
    public void findPersonFinancialAccountWithInvalidPersonResourceId() {
        controller.findPersonFinancialAccount("k16324e4-08c0-4ccd-ac23-7aa2add97011", null, null, null, null, null);
    }

    @Test(expected = ParametersRequiredException.class)
    public void findPersonFinancialAccountWithBlankParams() {
        controller.findPersonFinancialAccount(" ", " ", " ", " ", " ", " ");
    }

    @Test(expected = ParametersRequiredException.class)
    public void findPersonFinancialAccountWithMailGroup() {
        controller.findPersonFinancialAccount(null, null, null, "PD2", null, null);
    }

    @Test(expected = ParametersRequiredException.class)
    public void findPersonFinancialAccountWithSubGroup() {
        controller.findPersonFinancialAccount(null, null, null, null, "PD2", null);
    }

    @Test(expected = ParametersRequiredException.class)
    public void findPersonFinancialAccountWithMailGroupAndSubGroup() {
        controller.findPersonFinancialAccount(null, null, null, "PD2", "PD2", null);
    }

    @Test
    public void findPersonFinancialAccountWithCustomerNumberAndMailGroupAndSubGroup() {
        PersonFinancialAccount expected = TestUtils.createFinAccObject();
        expected.setResourceId(UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
        expected.add(linkTo(methodOn(PersonFinancialAccountController.class).findById(expected.getResourceId())).withSelfRel());

        when(postProcessor.addLinks(any(PersonFinancialAccount.class))).thenReturn(expected);
        when(service.findAll(any(SearchParameter.class))).thenReturn(Arrays.asList(expected));

        List<PersonFinancialAccount> results = controller.findPersonFinancialAccount(null, null, "U344444449", "PD2", "KHXB", null);
        assertNotEquals(results, null);
        assertEquals(results.get(0).getResourceId(), UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
    }

    @Test
    public void findPersonFinancialAccountWithCustomerNumber() {
        PersonFinancialAccount expected = TestUtils.createFinAccObject();
        expected.setResourceId(UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
        expected.add(linkTo(methodOn(PersonFinancialAccountController.class).findById(expected.getResourceId())).withSelfRel());

        when(postProcessor.addLinks(any(PersonFinancialAccount.class))).thenReturn(expected);
        when(service.findAll(any(SearchParameter.class))).thenReturn(Arrays.asList(expected));

        List<PersonFinancialAccount> results = controller.findPersonFinancialAccount(null, null, "U344444449", null, null, null);
        assertNotEquals(results, null);
        assertEquals(results.get(0).getResourceId(), UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
    }

    @Test
    public void findPersonFinancialAccountWithIndividualAGN() {
        PersonFinancialAccount expected = TestUtils.createFinAccObject();
        expected.setResourceId(UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
        expected.add(linkTo(methodOn(PersonFinancialAccountController.class).findById(expected.getResourceId())).withSelfRel());

        when(postProcessor.addLinks(any(PersonFinancialAccount.class))).thenReturn(expected);
        when(service.findAll(any(SearchParameter.class))).thenReturn(Arrays.asList(expected));

        List<PersonFinancialAccount> results = controller.findPersonFinancialAccount(null, "04566666667", null, null, null, null);
        assertNotEquals(results, null);
        assertEquals(results.get(0).getResourceId(), UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
    }

    @Test
    public void findPersonFinancialAccountWithMembershipId() {
        PersonFinancialAccount expected = TestUtils.createFinAccObject();
        expected.setResourceId(UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
        expected.add(linkTo(methodOn(PersonFinancialAccountController.class).findById(expected.getResourceId())).withSelfRel());

        when(postProcessor.addLinks(any(PersonFinancialAccount.class))).thenReturn(expected);
        when(service.findAll(any(SearchParameter.class))).thenReturn(Arrays.asList(expected));

        List<PersonFinancialAccount> results = controller.findPersonFinancialAccount(null, null, null, null, null, "045669234345");
        assertNotEquals(results, null);
        assertEquals(results.get(0).getResourceId(), UUID.fromString("d16324e4-08c0-4ccd-ac23-7aa2add9702a"));
    }

    @Test
    public void findPersonFinancialAccountNoRecords() {
        when(service.findAll(any(SearchParameter.class))).thenReturn(new ArrayList<>());

        List<PersonFinancialAccount> results = controller.findPersonFinancialAccount(null, null, null, null, null, "045669234345");
        assertNotEquals(results, null);
    }
}