package converter;

import com.esrx.platforms.entity.common.datasync.model.message.Message;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CommonMessageConverter {

    @Qualifier("serializingObjectMapper")
    private final ObjectMapper mapper;

    public Message<PersonFinancialAccount> convertToMessage(String messageAsString) throws IOException {
            return mapper.readValue(messageAsString, new TypeReference<Message<PersonFinancialAccount>>() {
            });
    }

}
