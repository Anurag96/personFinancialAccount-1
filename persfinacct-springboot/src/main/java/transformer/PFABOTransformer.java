package transformer;

import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.dto.StatusDto;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

/**
 * Converts PFA BO to DTO Object
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class PFABOTransformer {

    private final ModelMapper modelMapper;

    /**
     * Build PersonFinancialAccount DTO
     *
     * @param personFinancialAccount PersonFinancialAccount BO
     * @return PersonFinancialAccount DTO
     */
    public PersonFinancialAccountDto getPersonFinancialAccountDto(PersonFinancialAccount personFinancialAccount) {
        log.debug("PersonFinancialAccount :: {}", personFinancialAccount);
        PersonFinancialAccountDto personFinancialAccountDto = modelMapper.map(personFinancialAccount, PersonFinancialAccountDto.class);

        if (personFinancialAccount != null && !ObjectUtils.isEmpty(personFinancialAccount.getStatus())) {
            StatusDto newStatus = new StatusDto(personFinancialAccount.getStatus().getEffectiveDateTime(), personFinancialAccount.getStatus().getValue());
            personFinancialAccountDto.setStatusList(Arrays.asList(newStatus));
        }
        log.debug("personFinancialAccountDto :: {}", personFinancialAccountDto);
        return personFinancialAccountDto;
    }
}
