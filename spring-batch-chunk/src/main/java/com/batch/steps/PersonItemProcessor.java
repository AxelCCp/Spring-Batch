package com.batch.steps;

import com.batch.model.entity.Person;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class PersonItemProcessor implements ItemProcessor<Person, Person> {                                             //<person, person> especifica el tipo que inggresa al processor y el tipo q sale del processor.

    @Override
    public Person process(Person person) throws Exception {

        //esto lo hace para mostrar como realozar algun cambio en nuestro obj.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime date = LocalDateTime.now();
        person.setCreateAt(formatter.format(date));
        return person;
    }
}
