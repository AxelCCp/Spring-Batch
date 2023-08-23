package com.batch.steps;

import com.batch.model.entity.Person;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;

public class PersonItemReader extends FlatFileItemReader <Person>{                                                      //FlatFileItemReader : ayuda a leer un archivo automaticamente.


    public PersonItemReader() {
        setName("readPersons");                                                                                         //se setean las propiedades a nuestro FlatFileItemReader.
        setResource(new ClassPathResource("persons.csv"));                                                              //define donde va a estar el archivo, con ClassPathResource() se hace referencia a la carpeta resource. y dentro de la carpeta resource se buscara el archivo persons.csv
        setLinesToSkip(1);                                                                                              //cuantas lineas debe saltar antes de empezar a leer el archivo.
        setEncoding(StandardCharsets.UTF_8.name());
        setLineMapper(getLineMapper());                                                                                 //configuracion de como se va a leer el archivo.
    }

                                                                                                                        //se va a tomar un string y se va a convertir a obj de tipo Person.
    public LineMapper<Person>getLineMapper(){
        DefaultLineMapper<Person>lineMapper = new DefaultLineMapper<>();                                                //con este obj se configuran las propiedades de lectura del archivo. Al lineMapper  le llega una linea completa del registro.
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();                                            //el lineMapper se ayuda con el lineTokenizer, para dividir como un split, toda la linea de string.
        String[]columns = new String[] {"name", "lastname", "age"};                                                     //define las columnas.
        int[]indexFiles = new int[]{0,1,2};                                                                             //especifica el indice de "name", "lastname", "age".
        lineTokenizer.setNames(columns);
        lineTokenizer.setIncludedFields(indexFiles);
        lineTokenizer.setDelimiter(",");                                                                                //aqui va el separador.
        BeanWrapperFieldSetMapper<Person>fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Person.class);                                                                     //con esto le decimos a batch q , va a mapear estas columnas con un obj de tipo persona.
        lineMapper.setLineTokenizer(lineTokenizer);                                                                     //se setea tod0 en nuestro lineMapper.
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
}
