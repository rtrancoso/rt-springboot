package br.com.rtrancoso.springboot.base.stream.event.producer;

import br.com.rtrancoso.springboot.base.stream.event.EventSource;
import br.com.rtrancoso.springboot.base.stream.exception.EventProducerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventProducer {

    private final StreamBridge streamBridge;

    public void sendMessage(EventSource eventSource) {
        try {
            log.info("sending message to channel {}", eventSource.getEventType().getChannel());
            streamBridge.send(eventSource.getEventType().getChannel(), MessageBuilder.withPayload(eventSource).build());
        } catch (Exception ex) {
            log.error("error sending message to channel {}", eventSource.getEventType().getChannel(), ex);
            throw new EventProducerException("error sending message to channel", ex);
        }
    }

}
