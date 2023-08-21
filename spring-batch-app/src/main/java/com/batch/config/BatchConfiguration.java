package com.batch.config;

import com.batch.steps.ItemDescompressStep1;
import com.batch.steps.ItemProcessorStep3;
import com.batch.steps.ItemReaderStep2;
import com.batch.steps.ItemWriterStep4;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing                                                                                                  //habilita la configuracion para spring batch
public class BatchConfiguration {

    //--------------TASKLET--------------
    @Bean
    @JobScope                                                                                                           //quiere decir que este paso va a estar solo disponible en el job. es decir,  cuando se inicie el job, se va a crear el obj. y cuando finalice el job, se va a destruir el obj.
    public ItemDescompressStep1 itemDescompressStep1(){
        return new ItemDescompressStep1();
    }

    @Bean
    @JobScope
    public ItemReaderStep2 itemReaderStep2(){
        return new ItemReaderStep2();
    }

    @Bean
    @JobScope
    public ItemProcessorStep3 itemProcessorStep3(){
        return new ItemProcessorStep3();
    }

    @Bean
    @JobScope
    public ItemWriterStep4 itemWriterStep4(){
        return new ItemWriterStep4();
    }

    //---------------------DEFINICION DE PASOS---------------------------------------

    @Bean
    public Step descompressFileStep1(){
        return stepBuilderFactory.get("itemDescompressStep1").tasklet(itemDescompressStep1()).build();
    }
    @Bean
    public Step readFileStep2(){
        return stepBuilderFactory.get("itemReaderStep2").tasklet(itemReaderStep2()).build();
    }

    @Bean
    public Step processDataStep(){
        return stepBuilderFactory.get("itemProcessorStep3").tasklet(itemProcessorStep3()).build();
    }

    @Bean
    public Step writerDataStep(){
        return stepBuilderFactory.get("itemWriterStep4").tasklet(itemWriterStep4()).build();
    }

    //--------------------------CREACION DEL JOB---------------------------------
    @Bean
    public Job readCSVJob(){
        return jobBuilderFactory.get("readCSVJob")
                .start(descompressFileStep1())
                .next(readFileStep2())
                .next(processDataStep())
                .next(writerDataStep())
                .build();
    }


    @Autowired
    public JobBuilderFactory jobBuilderFactory;                                                                         //obj para construir nuestros job
    @Autowired
    public StepBuilderFactory stepBuilderFactory;                                                                       //obj para construir nuestros step
}
