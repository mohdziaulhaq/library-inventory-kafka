package com.library.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LibraryEventsConsumer {

//    @KafkaListener(topics = {"library-events"})
        @KafkaListener(
            topics = {"library-events"}
//                , autoStartup = "${libraryListener.startup:true}"
//                , groupId = "library-events-listener-group"
                )
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) {
        log.info("Received record: {}",  consumerRecord);
    }
}
