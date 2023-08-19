package com.batch.model.service;

import com.batch.model.dao.PersonDao;
import com.batch.model.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService{
    @Override
    @Transactional
    public void saveAll(List<Person> personList) {
        personDao.saveAll(personList);
    }

    @Autowired
    private PersonDao personDao;
}
