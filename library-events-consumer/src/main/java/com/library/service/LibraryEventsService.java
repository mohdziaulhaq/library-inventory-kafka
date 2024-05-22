package com.library.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.entity.LibraryEvent;
import com.library.entity.LibraryEventType;
import com.library.repository.LibraryEventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LibraryEventsService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private LibraryEventsRepository libraryEventsRepository;


    public void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {

        LibraryEvent libraryEvent = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
        log.info("libraryEvent : {}", libraryEvent);

        switch (libraryEvent.getLibraryEventType()){
            case NEW:
                //save
                save(libraryEvent);
                break;
            case UPDATE:
                //update
                break;
            default:
                log.info("Invalid library event type");
        }

    }

    private void save(LibraryEvent libraryEvent) {

        //mapping libraryevent to book - mandatory to create a mapping between them
        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        libraryEventsRepository.save(libraryEvent);
        log.info("Successfully persisted library event : {}", libraryEvent);
    }
}
