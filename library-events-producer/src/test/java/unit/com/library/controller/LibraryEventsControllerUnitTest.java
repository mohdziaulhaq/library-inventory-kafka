package com.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.domain.LibraryEvent;
import com.library.producer.LibraryEventsProducer;
import com.library.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//test slice
@WebMvcTest(controllers = LibraryEventsController.class)
class LibraryEventsControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LibraryEventsProducer libraryEventsProducer;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void postLibraryEvent() throws Exception {
        //given
        var json = objectMapper.writeValueAsString(TestUtil.libraryEventRecord());

        when(libraryEventsProducer.sendLibraryEvent_Approach3(isA(LibraryEvent.class)))
                .thenReturn(null);
        //when
        mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        //then
    }

    @Test
    void postLibraryEvent_invalidValues() throws Exception {
        //given
        var json = objectMapper.writeValueAsString(TestUtil.libraryEventRecordWithInvalidBook());

        when(libraryEventsProducer.sendLibraryEvent_Approach3(isA(LibraryEvent.class)))
                .thenReturn(null);
        var expectedErrorMessage = "book.bookId: must not be null, book.bookName: must not be blank";
        //when
        mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));
        //then

    }

    @Test
    void putLibraryEvent_InvalidEventId() throws Exception {
       var json = objectMapper.writeValueAsString(TestUtil.libraryEventRecordUpdateWithNullLibraryEventId());
        when(libraryEventsProducer.sendLibraryEvent_Approach3(isA(LibraryEvent.class)))
                .thenReturn(null);
       var expectedErrorMessage = "Please pass the libraryEventId";

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));

    }

    @Test
    void putLibraryEvent_InvalidEventType() throws Exception {
        var json = objectMapper.writeValueAsString(TestUtil.libraryEventRecordUpdateWithInvalidLibraryEventType());
        when(libraryEventsProducer.sendLibraryEvent_Approach3(isA(LibraryEvent.class)))
                .thenReturn(null);
        var expectedErrorMessage = "Only UPDATE event type is supported";

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));

    }
}