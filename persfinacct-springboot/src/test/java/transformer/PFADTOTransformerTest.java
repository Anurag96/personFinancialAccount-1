package transformer;

import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.dto.StatusDto;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.model.Status;
import com.esrx.services.personfinancialaccounts.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class PFADTOTransformerTest {

    LocalDateTime currentDateTime = LocalDateTime.now();

    @InjectMocks
    private PFADTOTransformer pfadtoTransformer;
    @Mock
    private ModelMapper modelMapper;
    private PersonFinancialAccountDto personFinancialAccountDto = TestUtils.getPfaDtoMock(currentDateTime);

    @Test
    public void getPersonFinancialAccountBoWhenMultipleStatus() {
        PersonFinancialAccount personFinancialAccount = new PersonFinancialAccount();
        when(modelMapper.map(personFinancialAccountDto, PersonFinancialAccount.class)).thenReturn(personFinancialAccount);

        assertEquals(personFinancialAccount, pfadtoTransformer.getPersonFinancialAccountBo(personFinancialAccountDto));
        assertEquals(personFinancialAccount.getStatus().getEffectiveDateTime(), currentDateTime.plusDays(10));
        assertEquals(personFinancialAccount.getStatus().getValue(), Status.StatusType.DECEASED.getStatus());
    }

    @Test
    public void getPersonFinancialAccountBoWhenSingleStatus() {
        PersonFinancialAccount personFinancialAccount = new PersonFinancialAccount();
        personFinancialAccountDto.setStatusList(Arrays.asList(new StatusDto(currentDateTime, Status.StatusType.INACTIVE.getStatus())));
        when(modelMapper.map(personFinancialAccountDto, PersonFinancialAccount.class)).thenReturn(personFinancialAccount);

        assertEquals(personFinancialAccount, pfadtoTransformer.getPersonFinancialAccountBo(personFinancialAccountDto));
        assertEquals(personFinancialAccount.getStatus().getEffectiveDateTime(), currentDateTime);
        assertEquals(personFinancialAccount.getStatus().getValue(), Status.StatusType.INACTIVE.getStatus());
    }

    @Test
    public void getPersonFinancialAccountBoWithNoStatus() {
        PersonFinancialAccount personFinancialAccount = new PersonFinancialAccount();
        personFinancialAccountDto.setStatusList(null);
        when(modelMapper.map(personFinancialAccountDto, PersonFinancialAccount.class)).thenReturn(personFinancialAccount);

        assertEquals(personFinancialAccount, pfadtoTransformer.getPersonFinancialAccountBo(personFinancialAccountDto));
        assertNull(personFinancialAccount.getStatus());
    }
}
