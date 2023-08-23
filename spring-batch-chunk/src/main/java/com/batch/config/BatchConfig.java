package com.batch.config;

import com.batch.model.entity.Person;
import com.batch.steps.PersonItemProcessor;
import com.batch.steps.PersonItemReader;
import com.batch.steps.PersonItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public PersonItemReader itemReader(){
        return new PersonItemReader();
    }

    @Bean
    public PersonItemWriter itemWriter(){
        return new PersonItemWriter();
    }

    @Bean
    public PersonItemProcessor itemProcessor(){
        return new PersonItemProcessor();
    }

    //---------------------CONFIGURACION DEL JOB----------------------------
    //con chunck, el reader, processor y writer, van en un solo paso

    @Bean
    public TaskExecutor taskExecutor() {                                                                        //configuracion de los hilos
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();                                             //obj de java que ayuda a gestionar hilos. con esto batch coordina los hilos.
        taskExecutor.setCorePoolSize(1);                                                                                //define con cuantos hilos va a comenzar la aplicacion.
        taskExecutor.setMaxPoolSize(5);                                                                                 //si la aplicacion, en tiempo de ejecucion, necesita más hilos para el job, puede desplegar automaticamente hilos adicionales.
        taskExecutor.setQueueCapacity(5);                                                                               //define el máximo de tareas en cola. si tengo 10 procesos y solo tengo 5 hilos, los otros 5 procesos van a estar en cola.Y si hay otros procesos, estos no se cargaran en memoria.
        return taskExecutor;
    }
    @Bean
    public Step readFile(){
        return stepBuilderFactory.get("readFile")                                                                       //se pone el nombre del paso "readFileWithChunck". //luego se especifican 2 genericos, el obj de entrada y el obj de salida al finalizar el proceso.  //chunck() : especifica el tamaño del lote.
                .<Person, Person>chunk(10)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("readFileWithChunck").start(readFile()).build();                                   //se agrega el paso de leer archivo.
    }

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

}
