package transformer;

import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.dto.StatusDto;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.model.Status;
import com.esrx.services.personfinancialaccounts.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class PFABOTransformerTest {

    @InjectMocks
    private PFABOTransformer pfaboTransformer;

    @Mock
    private ModelMapper modelMapper;

    private PersonFinancialAccount personFinancialAccount = TestUtils.createFinAccObject();

    @Before
    public void setup() throws Exception {
        personFinancialAccount.setStatus(new Status(LocalDateTime.now(), Status.StatusType.ACTIVE.getStatus()));
    }

    @Test
    public void getPersonFinancialAccountDto() {
        PersonFinancialAccountDto personFinancialAccountDto = new PersonFinancialAccountDto();
        when(modelMapper.map(personFinancialAccount, PersonFinancialAccountDto.class)).thenReturn(personFinancialAccountDto);

        assertEquals(personFinancialAccountDto, pfaboTransformer.getPersonFinancialAccountDto(personFinancialAccount));
        assertEquals(personFinancialAccountDto.getStatusList().get(0).getEffectiveDateTime(), personFinancialAccount.getStatus().getEffectiveDateTime());
        assertEquals(personFinancialAccountDto.getStatusList().get(0).getValue(), personFinancialAccount.getStatus().getValue());

        verify(modelMapper).map(personFinancialAccount, PersonFinancialAccountDto.class);
    }

    @Test
    public void getPersonFinancialAccountDtoWithDuplicateStatus() {
        PersonFinancialAccountDto personFinancialAccountDto = new PersonFinancialAccountDto();
        personFinancialAccountDto.setStatusList(Arrays.asList(new StatusDto(LocalDateTime.now(), Status.StatusType.ACTIVE.getStatus())));

        when(modelMapper.map(personFinancialAccount, PersonFinancialAccountDto.class)).thenReturn(personFinancialAccountDto);
        assertEquals(personFinancialAccountDto, pfaboTransformer.getPersonFinancialAccountDto(personFinancialAccount));
        assertEquals(1, personFinancialAccountDto.getStatusList().size());

        verify(modelMapper).map(personFinancialAccount, PersonFinancialAccountDto.class);
    }

    @Test
    public void getPersonFinancialAccountDtoWithNewStatus() {
        PersonFinancialAccountDto personFinancialAccountDto = new PersonFinancialAccountDto();
        personFinancialAccountDto.setStatusList(Arrays.asList(new StatusDto(LocalDateTime.now(), Status.StatusType.ON_HOLD.getStatus())));

        when(modelMapper.map(personFinancialAccount, PersonFinancialAccountDto.class)).thenReturn(personFinancialAccountDto);
        assertEquals(personFinancialAccountDto, pfaboTransformer.getPersonFinancialAccountDto(personFinancialAccount));
        assertEquals(1, personFinancialAccountDto.getStatusList().size());

        verify(modelMapper).map(personFinancialAccount, PersonFinancialAccountDto.class);
    }
}
