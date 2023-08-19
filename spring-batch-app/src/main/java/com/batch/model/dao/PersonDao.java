package com.batch.model.dao;

import com.batch.model.entity.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonDao extends CrudRepository<Person, Long> {



}
