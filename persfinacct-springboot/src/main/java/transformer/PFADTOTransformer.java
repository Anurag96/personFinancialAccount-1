package transformer;

import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.dto.StatusDto;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts PFA DTO to BO Object
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class PFADTOTransformer {

    private final ModelMapper modelMapper;

    public PersonFinancialAccount getPersonFinancialAccountBo(PersonFinancialAccountDto personFinancialAccountDto) {
        log.debug("personFinancialAccountDto :: {}", personFinancialAccountDto);
        PersonFinancialAccount personFinancialAccount = modelMapper.map(personFinancialAccountDto, PersonFinancialAccount.class);

        if (personFinancialAccountDto != null && CollectionUtils.isNotEmpty(personFinancialAccountDto.getStatusList())) {
            List<StatusDto> statusList = personFinancialAccountDto.getStatusList().stream().sorted(Comparator.comparing(StatusDto::getEffectiveDateTime)).collect(Collectors.toList());

            log.info("statusList :: {}", statusList);

            StatusDto statusDto = statusList.get(statusList.size() - 1);
            log.info("statusDto : {}", statusDto);
            log.info("personFinancialAccount : {}", personFinancialAccount);
            personFinancialAccount.setStatus(new Status(statusDto.getEffectiveDateTime(), statusDto.getValue()));
        }
        log.debug("personFinancialAccount :: {}", personFinancialAccount);
        return personFinancialAccount;
    }
}
