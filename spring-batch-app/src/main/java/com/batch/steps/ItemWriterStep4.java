package com.batch.steps;

import com.batch.model.entity.Person;
import com.batch.model.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class ItemWriterStep4 implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("---------------------Inicio step escritura-----------------------");

        List<Person> personList = (List<Person>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("personList");
        personList.forEach(person -> {
            if(person != null){
                log.info(person.toString());
            }
        });

        personService.saveAll(personList);

        log.info("---------------------Fin step escritura-----------------------");
        return RepeatStatus.FINISHED;
    }

    @Autowired
    private PersonService personService;
}
