package com.batch.steps;

import com.batch.model.entity.Person;
import com.batch.model.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class PersonItemWriter implements ItemWriter<Person> {


    @Override
    public void write(List<? extends Person> list) throws Exception {                                                   //metodo pra guardar los registros en la bbdd
        list.forEach(person -> person.toString());
        personService.saveAll((List<Person>) list);
    }

    @Autowired
    private PersonService personService;


}
