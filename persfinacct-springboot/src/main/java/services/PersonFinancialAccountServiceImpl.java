package services;

import com.esrx.platforms.entity.common.datasync.model.message.Header;
import com.esrx.services.personfinancialaccounts.dto.CollectionAgencyDto;
import com.esrx.services.personfinancialaccounts.dto.PersonFinancialAccountDto;
import com.esrx.services.personfinancialaccounts.exception.PersonFinancialAccountNotFoundException;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.models.SearchParameter;
import com.esrx.services.personfinancialaccounts.processors.PFAProcessor;
import com.esrx.services.personfinancialaccounts.repositories.PFACustomRepository;
import com.esrx.services.personfinancialaccounts.repositories.PersonFinancialAccountRepository;
import com.esrx.services.personfinancialaccounts.transformer.PFABOTransformer;
import com.esrx.services.personfinancialaccounts.transformer.PFADTOTransformer;
import com.esrx.services.personfinancialaccounts.util.Utils;
import com.express_scripts.inf.logging.Loggable;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
@Loggable
public class PersonFinancialAccountServiceImpl implements PersonFinancialAccountService {
    private final PersonFinancialAccountRepository repository;
    private final PFABOTransformer pfaboTransformer;
    private final PFADTOTransformer pfadtoTransformer;
    private final Utils utils;
    private final PFACustomRepository customRepository;
    private final PFAProcessor pfaProcessor;

    @Override
    @Timed(value = "java", extraTags = {"reportGroups", "getPFAById"})
    public PersonFinancialAccount findOne(UUID resourceId) {
        Optional<PersonFinancialAccountDto> maybePersonFinancialAccount = repository.findById(resourceId);

        return maybePersonFinancialAccount.map(
                pfadtoTransformer::getPersonFinancialAccountBo
        ).orElseThrow(() -> new PersonFinancialAccountNotFoundException(resourceId.toString()));
    }

    @Override
    @Timed(value = "java", extraTags = {"reportGroups", "insertPFA"})
    public PersonFinancialAccount insert(PersonFinancialAccount personFinancialAccount, Header header) {
        log.debug("Incoming personFinancialAccount ::{}", personFinancialAccount);
        PersonFinancialAccountDto personFinancialAccountDto = pfaboTransformer
                .getPersonFinancialAccountDto(personFinancialAccount);
        personFinancialAccountDto.setResourceId(utils.getUUID());

        List<CollectionAgencyDto> collectionAgencies = personFinancialAccountDto.getCollectionAgencies();
        if (!ObjectUtils.isEmpty(collectionAgencies)) {
            collectionAgencies.stream().forEach(agency -> agency.setRelavitevId(utils.getUUID()));
        }
        utils.loadHeaderDetails(header, personFinancialAccountDto);

        PersonFinancialAccountDto savedPersonFinancialAccount = repository.save(personFinancialAccountDto);
        PersonFinancialAccount pfaAfterChange = pfadtoTransformer
                .getPersonFinancialAccountBo(savedPersonFinancialAccount);

        pfaProcessor.processPFAEvent(utils.prepareChangeMessage(header, null, pfaAfterChange));

        log.debug("Inserted personFinancialAccount ::{}", pfaAfterChange);
        return pfaAfterChange;
    }

    @Override
    @Timed(value = "java", extraTags = {"reportGroups", "updatePFA"})
    public PersonFinancialAccount update(PersonFinancialAccountDto existingPFADto, PersonFinancialAccount incomingPFADetailsBo, Header header) {
        //When Kafka Listener calls this it will be already having existing DTO information
        if (existingPFADto == null) {
            //In case of API call we have to explicitly verify the PFA existence
            Optional<PersonFinancialAccountDto> existingPFA = repository.findById(incomingPFADetailsBo.getResourceId());
            if (!existingPFA.isPresent()) {
                throw new PersonFinancialAccountNotFoundException(incomingPFADetailsBo.getResourceId().toString());
            }
            existingPFADto = existingPFA.get();
        }
        PersonFinancialAccount pfaBeforeChangeBo = pfadtoTransformer.getPersonFinancialAccountBo(existingPFADto);

        //Transformer always gets only one status from the incoming message.
        PersonFinancialAccountDto incomingPFADto = pfaboTransformer.getPersonFinancialAccountDto(incomingPFADetailsBo);
        PersonFinancialAccountDto updatedPFADto = utils.mergePFADetails(existingPFADto, incomingPFADto);

        //Loading tenant details to save in the db
        utils.loadHeaderDetails(header, updatedPFADto);

        //Saving latest changes to DB
        PersonFinancialAccountDto savedPersonFinancialAccountDto = repository.save(updatedPFADto);

        PersonFinancialAccount pfaAfterChangeBo = pfadtoTransformer.getPersonFinancialAccountBo(savedPersonFinancialAccountDto);

        //Sending the messages to Change Topic & Latest Topic
        pfaProcessor.processPFAEvent(utils.prepareChangeMessage(header, pfaBeforeChangeBo, pfaAfterChangeBo));

        return pfaAfterChangeBo;
    }

    @Override
    @Timed(value = "java", extraTags = {"reportGroups", "filterPFA"})
    public List<PersonFinancialAccount> findAll(SearchParameter searchParameter) {
        List<PersonFinancialAccountDto> pfaList = customRepository.search(searchParameter);
        return pfaList.stream().map(pfadtoTransformer::getPersonFinancialAccountBo)
                .collect(Collectors.toList());
    }

    @Override
    @Timed(value = "java", extraTags = {"reportGroups", "deletePFA"})
    public void delete(PersonFinancialAccountDto existingPFADto, UUID resourceId, Header header) {
        if (existingPFADto == null) {
            Optional<PersonFinancialAccountDto> personFinancialAccountDto = repository.findById(resourceId);
            if (!personFinancialAccountDto.isPresent()) {
                throw new PersonFinancialAccountNotFoundException(resourceId.toString());
            }
            existingPFADto = personFinancialAccountDto.get();
        }
        repository.deleteById(resourceId);
        PersonFinancialAccount pfaBeforeChange = pfadtoTransformer.getPersonFinancialAccountBo(existingPFADto);
        pfaProcessor.processPFAEvent(utils.prepareChangeMessage(header, pfaBeforeChange, null));
        log.info("Deleted PFA for : {}", resourceId);
    }
}
