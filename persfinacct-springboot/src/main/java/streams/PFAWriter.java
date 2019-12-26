package streams;

import com.esrx.platforms.entity.common.datasync.model.message.Message;
import com.esrx.services.personfinancialaccounts.model.ChangePersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.model.PersonFinancialAccount;
import com.esrx.services.personfinancialaccounts.monitor.MeterRegistryHandler;
import com.esrx.services.personfinancialaccounts.util.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.UUID;

@Service
@Slf4j
public class PFAWriter {

    private static final String PARTITION_KEY_HEADER_NAME = "partitionKey";

    @Autowired
    private Utils utils;

    @Autowired
    private PFAStreams pfaStreams;

    @Autowired
    @Qualifier("serializingObjectMapper")
    private ObjectMapper mapper;

    @Autowired
    private MeterRegistryHandler meterRegistry;

    @Timed(value = "java", extraTags = { "reportGroups", "kafkamethods" })
    public void sendMsgToLatestTopic(Message<PersonFinancialAccount> personFinancialAccountMessage, UUID resourceId) {
        if (personFinancialAccountMessage == null) {
            log.error("Empty latest shipment found. Unable to write to latest topic!");
        } else {
            log.debug("Writing event to latest topic");
            if (personFinancialAccountMessage.getPayload() != null) {
                personFinancialAccountMessage.getPayload().setResourceId(resourceId);
            }
            String eventAsString = getInstanceAsString(personFinancialAccountMessage, mapper);

            sendString(eventAsString, pfaStreams.getPFALatestTopicChannel(), resourceId.toString().getBytes());
            meterRegistry.incrementCounter("PFAWriter.sendLatest.success");
            log.debug("Successfully wrote " + personFinancialAccountMessage.getHeader().getOperation() + " message to latest topic: message= {}", eventAsString);
        }
    }

    @Timed(value = "java", extraTags = { "reportGroups", "kafkamethods" })
    public void sendMsgToChangeTopic(Message<ChangePersonFinancialAccount> changeMessage, UUID resourceId) {
        if (changeMessage == null || changeMessage.getPayload() == null)
            log.error("Empty change found. Unable to write to change topic!");
        else {
            log.debug("Writing event to change topic");
            String eventAsString = getInstanceAsString(changeMessage, mapper);

            sendString(eventAsString, pfaStreams.getPFAChangeTopicChannel(), resourceId.toString().getBytes());
            meterRegistry.incrementCounter("pfaWriter.sendChange.success");
            log.debug("Successfully wrote message to change topic: message= {}", eventAsString);
        }
    }

    private void sendString(String eventAsString, MessageChannel channel, byte[] partitionKey) {
        channel.send(MessageBuilder.withPayload(eventAsString)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader(PARTITION_KEY_HEADER_NAME, partitionKey)
                .setHeader("kafka_messageKey", partitionKey)
                .build());
    }

    public String getInstanceAsString(Object object, ObjectMapper mapper) {
        String objectAsString = "";
        try {
            objectAsString = mapper.writer().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("could not parse json from object" + object.toString());
        }
        return objectAsString;
    }

}