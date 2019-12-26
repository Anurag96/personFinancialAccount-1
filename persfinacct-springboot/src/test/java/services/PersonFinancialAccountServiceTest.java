package services;

import com.esrx.platforms.entity.common.datasync.model.message.Header;
import com.esrx.services.personfinancialaccounts.dto.CollectionAgencyDto;
import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.dto.StatusDto;
import com.esrx.services.personfinancialaccounts.exception.PersonFinancialAccountNotFoundException;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.model.Status;
import com.esrx.services.personfinancialaccounts.models.SearchParameter;
import com.esrx.services.personfinancialaccounts.processors.PFAProcessor;
import com.esrx.services.personfinancialaccounts.processors.PostProcessor;
import com.esrx.services.personfinancialaccounts.repositories.PFACustomRepository;
import com.esrx.services.personfinancialaccounts.repositories.PersonFinancialAccountRepository;
import com.esrx.services.personfinancialaccounts.transformer.PFABOTransformer;
import com.esrx.services.personfinancialaccounts.transformer.PFADTOTransformer;
import com.esrx.services.personfinancialaccounts.util.TestUtils;
import com.esrx.services.personfinancialaccounts.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersonFinancialAccountServiceTest {
    private static final UUID THE_UUID = UUID.randomUUID();

    @Mock
    private PersonFinancialAccountRepository repository;

    @Mock
    private Utils utils;

    @Mock
    private PFACustomRepository customRepository;

    @Mock
    private PFABOTransformer pfaboTransformer;

    @Mock
    private PFADTOTransformer pfadtoTransformer;

    @Mock
    private CollectionAgencyDto collectionAgencyDto;

    @Mock
    private PostProcessor postProcessor;

    @Mock
    private PFAProcessor pfaProcessor;

    @InjectMocks
    private PersonFinancialAccountServiceImpl service;

    private PersonFinancialAccount personFinancialAccount = new PersonFinancialAccount();
    private PersonFinancialAccountDto personFinancialAccountDto = new PersonFinancialAccountDto();

    @Before
    public void setup() {
        personFinancialAccount.setResourceId(UUID.randomUUID());
    }

    @Test
    public void findOneHappyPath() {
        when(repository.findById(THE_UUID)).thenReturn(Optional.of(personFinancialAccountDto));
        when(pfadtoTransformer.getPersonFinancialAccountBo(personFinancialAccountDto))
                .thenReturn(personFinancialAccount);
        PersonFinancialAccount returnedPersonFinancialAccount = service.findOne(THE_UUID);
        assertEquals(personFinancialAccount, returnedPersonFinancialAccount);
    }

    @Test(expected = PersonFinancialAccountNotFoundException.class)
    public void findOneNotFound() {
        when(repository.findById(THE_UUID)).thenReturn(Optional.empty());
        service.findOne(THE_UUID);
    }

    @Test
    public void testServiceInsert() {
        PersonFinancialAccount accDetails = new PersonFinancialAccount();
        PersonFinancialAccountDto accDetailsDto = new PersonFinancialAccountDto();

        when(repository.save(accDetailsDto)).thenReturn(accDetailsDto);
        when(pfaboTransformer.getPersonFinancialAccountDto(accDetails)).thenReturn(accDetailsDto);
        when(pfadtoTransformer.getPersonFinancialAccountBo(accDetailsDto)).thenReturn(personFinancialAccount);

        assertEquals(personFinancialAccount, service.insert(accDetails, any()));

    }

    @Test
    public void testServiceUpdate() {
        LocalDateTime dateTime = LocalDateTime.now();

        PersonFinancialAccount incomingPFADetails = TestUtils.createFinAccObject();
        incomingPFADetails.setStatus(new Status(dateTime.minusDays(20), Status.StatusType.ACTIVE.getStatus()));

        PersonFinancialAccountDto existingPFADto = TestUtils.getPfaDtoMock(dateTime);
        when(repository.findById(any())).thenReturn(Optional.ofNullable(existingPFADto));

        personFinancialAccountDto.setStatusList(Arrays.asList(new StatusDto(dateTime.plusDays(20), Status.StatusType.INACTIVE.getStatus())));

        when(pfaboTransformer.getPersonFinancialAccountDto(any())).thenReturn(personFinancialAccountDto);
        when(pfadtoTransformer.getPersonFinancialAccountBo(any())).thenReturn(personFinancialAccount);

        when(utils.mergePFADetails(any(), any())).thenReturn(personFinancialAccountDto);
        doNothing().when(utils).loadHeaderDetails(any(), any());

        when(repository.save(personFinancialAccountDto)).thenReturn(personFinancialAccountDto);
        assertEquals(personFinancialAccount, service.update(null, incomingPFADetails, new Header()));
    }

    @Test
    public void testServiceInsertWithCollectionAgencies() {
        PersonFinancialAccount accDetails = new PersonFinancialAccount();
        PersonFinancialAccountDto accDetailsDto = new PersonFinancialAccountDto();
        accDetailsDto.setCollectionAgencies(Arrays.asList(collectionAgencyDto));

        when(repository.save(accDetailsDto)).thenReturn(accDetailsDto);
        when(pfaboTransformer.getPersonFinancialAccountDto(accDetails)).thenReturn(accDetailsDto);
        when(pfadtoTransformer.getPersonFinancialAccountBo(accDetailsDto)).thenReturn(personFinancialAccount);

        PersonFinancialAccount result = service.insert(accDetails, any());
        assertEquals(personFinancialAccount, result);
        assertNotEquals(result, null);

    }

    @Test
    public void testFindAll() {
        List<PersonFinancialAccountDto> expected = Arrays.asList(TestUtils.createFinAccObject()).stream().map(pfaboTransformer::getPersonFinancialAccountDto)
                .collect(Collectors.toList());

        List<PersonFinancialAccount> result = Arrays.asList(expected.get(0)).stream().map(pfadtoTransformer::getPersonFinancialAccountBo)
                .collect(Collectors.toList());

        when(customRepository.search(any(SearchParameter.class))).thenReturn(expected);
        assertEquals(result, service.findAll(new SearchParameter()));
    }

    @Test
    public void testFindAllIfNoResultsFound() {
        when(customRepository.search(any(SearchParameter.class))).thenReturn(new ArrayList<>());
        assertEquals(new ArrayList<>(), service.findAll(new SearchParameter()));
    }

    @Test
    public void testDeleteHappyPath() {
        doNothing().when(pfaProcessor).processPFAEvent(any());
        service.delete(new PersonFinancialAccountDto(), THE_UUID, null);
        verify(repository, times(1)).deleteById(THE_UUID);
    }

    @Test(expected = PersonFinancialAccountNotFoundException.class)
    public void testDeletePFANotFound() {
        when(repository.findById(THE_UUID)).thenReturn(Optional.empty());
        service.delete(null, THE_UUID, null);
    }
}
