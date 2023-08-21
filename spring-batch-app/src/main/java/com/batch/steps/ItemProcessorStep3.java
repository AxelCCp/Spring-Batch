package com.batch.steps;

import com.batch.model.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class ItemProcessorStep3 implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("-----------------------Inicio de paso de procesamiento--------------------------");

        List<Person> personList = (List<Person>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("personList");    //obten la lista desde el contexto de spring batch.
        List<Person> personFinalList = personList.stream().map(person -> {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            person.setInsertiondate(format.format(LocalDateTime.now()));
            return person;
        }).collect(Collectors.toList());

        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("personList", personFinalList);                            //sobreescribe la lista q ya se tinen y se envia la siguiente paso.

        log.info("-----------------------Fin de paso de procesamiento--------------------------");
        return RepeatStatus.FINISHED;
    }
}
