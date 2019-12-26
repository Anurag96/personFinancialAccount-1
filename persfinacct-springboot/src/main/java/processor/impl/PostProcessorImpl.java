package processor.impl;

import com.esrx.services.personfinancialaccounts.controllers.PersonFinancialAccountController;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.processors.PostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@Component
public class PostProcessorImpl implements PostProcessor {

    @Override
    public PersonFinancialAccount addLinks(PersonFinancialAccount pfa) {
        pfa.add(linkTo(methodOn(PersonFinancialAccountController.class).findById(pfa.getResourceId())).withSelfRel());
        return pfa;
    }
}
