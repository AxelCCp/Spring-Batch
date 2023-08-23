package com.batch.model.service;

import com.batch.model.entity.Person;

import java.util.List;

public interface PersonService {

    Iterable<Person> saveAll(List<Person> personList);

}
