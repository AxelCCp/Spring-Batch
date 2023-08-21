package com.batch.steps;

import com.batch.model.entity.Person;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ItemReaderStep2 implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("-----------------------------Inicio del paso de lectura------------------------------");
        Reader reader = new FileReader(resourceLoader.getResource("classpath:files/destination/persons.csv").getFile());         //classpath devuelve la ruta absoluta de la carpeta "resources".
        CSVParser parser = new CSVParserBuilder().withSeparator(',').build();                                                           //sirve para especificar el separador de nuestro documento.
        CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).withSkipLines(1).build();                              //csvReader: nos va a permitir leer los registros del archivo. con "1" se le dice q se salte la primera linea del titulo.
        List<Person>personList = new ArrayList<>();
        String[]actualLine;
        while((actualLine = csvReader.readNext()) != null){
            Person person = new Person();
            person.setName(actualLine[0]);
            person.setLastname(actualLine[1]);
            person.setAge(Integer.valueOf(actualLine[2]));
            personList.add(person);
        }
        csvReader.close();
        reader.close();
        log.info("-----------------------------Fin del paso de lectura------------------------------");

        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("personList", personList);         //guarda la lista en el contexto de springbatch

        return RepeatStatus.FINISHED;
    }

    @Autowired
    private ResourceLoader resourceLoader;                                                                                              //es un obj del core de spring, nos ayuda a importar archivos desde nuestra carpeta resource.
}
