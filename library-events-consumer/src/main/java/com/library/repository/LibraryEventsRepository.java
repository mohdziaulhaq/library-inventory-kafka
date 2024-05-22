package com.library.repository;

import com.library.entity.LibraryEvent;
import org.springframework.data.repository.CrudRepository;

public interface LibraryEventsRepository extends CrudRepository<LibraryEvent, Integer> {
}
