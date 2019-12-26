package controller;


import com.esrx.services.personfinancialaccounts.exception.ParametersRequiredException;
import com.esrx.services.personfinancialaccounts.model.OperationType;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.models.SearchParameter;
import com.esrx.services.personfinancialaccounts.processors.PostProcessor;
import com.esrx.services.personfinancialaccounts.services.PersonFinancialAccountService;
import com.esrx.services.personfinancialaccounts.util.Utils;
import com.express_scripts.inf.logging.Loggable;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@Loggable
@RequestMapping(value = "/v1/personFinancialAccounts")
@RestController
@Api(value = "PersonFinancialAccount", tags = "Person Financial Account Operations", description = " ")
public class PersonFinancialAccountController {

    private final PersonFinancialAccountService service;
    private final PostProcessor postProcessor;
    private final Utils utils;

    public PersonFinancialAccountController(PersonFinancialAccountService service, PostProcessor postProcessor, Utils utils) {
        this.service = service;
        this.postProcessor = postProcessor;
        this.utils = utils;
    }

    @GetMapping(value = "/{resourceId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single Person Financial Account using resourceId", response = PersonFinancialAccount.class)
    @Timed(value = "java", extraTags = {"reportGroups", "getPFAByResourceIdAPI"})
    public PersonFinancialAccount findById(@PathVariable UUID resourceId) {
        log.debug("Get Person Financial Account by resourceId invoked. resourceId = {}", resourceId);
        PersonFinancialAccount personFinancialAccount = service.findOne(resourceId);
        personFinancialAccount.add(linkTo(methodOn(PersonFinancialAccountController.class).findById(resourceId)).withSelfRel());
        log.info("personFinancialAccount :: {}", personFinancialAccount);
        return personFinancialAccount;
    }

    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single Person Financial Account using individualAGN,customerNumber,mailGroup,subGroup,membershipId", response = PersonFinancialAccount.class)
    @Timed(value = "java", extraTags = {"reportGroups", "filterPFAAPI"})
    public List<PersonFinancialAccount> findPersonFinancialAccount(
            @ApiParam("Optional. This will limit the retrieval to accounts corresponding to the for Person ID") @RequestParam(value = "personResourceId", required = false) String personResourceId,
            @ApiParam("individualAGN, this will return the records that has matching AGN") @RequestParam(value = "individualAGN", required = false) String individualAGN,
            @ApiParam("Customer Number, Mail Group and Sub Group goes together in the query.") @RequestParam(value = "customerNumber", required = false) String customerNumber,
            @ApiParam("Customer Number, Mail Group and Sub Group goes together in the query.") @RequestParam(value = "mailGroup", required = false) String mailGroup,
            @ApiParam("Customer Number, Mail Group and Sub Group goes together in the query.") @RequestParam(value = "subGroup", required = false) String subGroup,
            @ApiParam("Retrieves the records that has the matching eligibility membershipId") @RequestParam(value = "membershipId", required = false) String membershipId

    ) {
        if (StringUtils.isBlank(individualAGN) && StringUtils.isBlank(customerNumber) && StringUtils.isBlank(mailGroup) && StringUtils.isBlank(subGroup) && StringUtils.isBlank(membershipId) && StringUtils.isBlank(personResourceId)) {
            throw new ParametersRequiredException("Please provide at least one valid search criteria");
        }

        if (StringUtils.isBlank(customerNumber) && (StringUtils.isNotBlank(mailGroup) || StringUtils.isNotBlank(subGroup))) {
            throw new ParametersRequiredException("Please provide a Customer Number.");
        }
        UUID personResourceUUID = null;
        if (StringUtils.isNotBlank(personResourceId)) {
            personResourceUUID = utils.getUUIDFromString(personResourceId, "Person Resource Id");
        }

        SearchParameter searchParams = new SearchParameter(StringUtils.trimToNull(individualAGN), StringUtils.trimToNull(customerNumber),
                StringUtils.trimToNull(mailGroup), StringUtils.trimToNull(subGroup), StringUtils.trimToNull(membershipId), personResourceUUID);
        log.info("Search Criteria :{}", searchParams);

        List<PersonFinancialAccount> pfaList = service.findAll(searchParams);

        if (CollectionUtils.isNotEmpty(pfaList)) {
            pfaList.stream().forEach(postProcessor::addLinks);
        }
        log.info("pfaList :: {}", pfaList);
        return pfaList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new Person Financial Account", notes = "Create a new Person Financial Account by supplying all the required and optional fields", response = PersonFinancialAccount.class)
    @Timed(value = "java", extraTags = {"reportGroups", "createPFAAPI"})
    public PersonFinancialAccount create(@ApiIgnore @RequestHeader Map<String, String> header, @Valid @RequestBody PersonFinancialAccount personFinancialAccount) {
        log.debug("Create Person Financial Account invoked");

        LocalDateTime date = utils.getLocalDateTimeNow();
        personFinancialAccount.setCreatedDateTime(date);
        personFinancialAccount.setUpdatedDateTime(date);

        PersonFinancialAccount createdPersonFinancialAccount = service.insert(personFinancialAccount, utils.getHeaderDetailsFromHTTPRequest(header, OperationType.INSERT));
        if (createdPersonFinancialAccount != null) {
            createdPersonFinancialAccount.add(linkTo(methodOn(PersonFinancialAccountController.class).findById(createdPersonFinancialAccount.getResourceId())).withSelfRel());
        }
        return createdPersonFinancialAccount;
    }

    @PutMapping(value = "/{resourceId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update a Person Financial Account", notes = "Update a Person Financial Account identified by the resourceId", response = PersonFinancialAccount.class)
    @Timed(value = "java", extraTags = {"reportGroups", "updatePFAAPI"})
    public PersonFinancialAccount update(@ApiIgnore @RequestHeader Map<String, String> header, @PathVariable UUID resourceId, @Valid @RequestBody PersonFinancialAccount personFinancialAccount) {
        log.debug("Update Person Financial Account invoked");
        personFinancialAccount.setResourceId(resourceId);
        personFinancialAccount.setUpdatedDateTime(utils.getLocalDateTimeNow());

        PersonFinancialAccount updatedPFA = service.update(null, personFinancialAccount, utils.getHeaderDetailsFromHTTPRequest(header, OperationType.UPDATE));
        if (updatedPFA != null) {
            updatedPFA.add(linkTo(methodOn(PersonFinancialAccountController.class).findById(updatedPFA.getResourceId())).withSelfRel());
        }
        return updatedPFA;
    }


    /**
     * Delete the PFA for resourceId
     *
     * @param resourceId String
     */
    @DeleteMapping(value = "/{resourceId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete an existing Person Financial Account", notes = "Delete an existing Person Financial Account by its ResourceId", response = Boolean.class)
    @Timed(value = "java", extraTags = {"reportGroups", "deletePFAAPI"})
    public void delete(@ApiIgnore @RequestHeader Map<String, String> header, @PathVariable String resourceId) {
        log.info("Delete invoked for resourceId :: {}", resourceId);
        service.delete(null, utils.getUUIDFromString(resourceId, "Resource Id"), utils.getHeaderDetailsFromHTTPRequest(header, OperationType.DELETE));
    }
}