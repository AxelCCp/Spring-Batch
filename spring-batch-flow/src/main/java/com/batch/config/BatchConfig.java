package com.batch.config;

import com.batch.springbatch.CancelTransactionTaskLet;
import com.batch.springbatch.ProcessPaymentTasklet;
import com.batch.springbatch.SendNotificationTasklet;
import com.batch.springbatch.ValidateAccountTasklet;
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
@EnableBatchProcessing
public class BatchConfig {

    //SE CREAN LOS BEANS DE LOS TASKLET
    @Bean
    public ValidateAccountTasklet validateAccountTasklet(){                                                             //verifica la cuenta
        return new ValidateAccountTasklet();
    }

    @Bean
    public ProcessPaymentTasklet processPaymentTasklet(){
        return new ProcessPaymentTasklet();
    }

    @Bean
    public CancelTransactionTaskLet cancelTransactionTaskLet(){
        return new CancelTransactionTaskLet();
    }

    @Bean
    public SendNotificationTasklet sendNotificationTasklet(){
        return new SendNotificationTasklet();
    }

    //SE CREAN LOS BEAND DE LOS STEPS

    @Bean
    @JobScope                                                                                                           //le decimos que este step, solamente va a tener el alcanse de job. esto quiere decir q cuando el job se empiece a ejecutar, se va a quedar el bean. y una vez q termine el job, se va a eliminar este obj. y solamente se va a ejecutar cuando se ejecute nuestro job. esto es pq en ValidateAccountTasklet se maneja el valid y invalid.
    public Step validateAccountStep(){
        return stepBuilderFactory.get("validateAccountStep").tasklet(validateAccountTasklet()).build();
    }

    @Bean
    public Step processPaymentStep(){
        return stepBuilderFactory.get("processPaymentStep").tasklet(processPaymentTasklet()).build();
    }

    @Bean
    public Step cancelTransactionStep(){
        return stepBuilderFactory.get("cancelTransactionStep").tasklet(cancelTransactionTaskLet()).build();
    }

    @Bean
    public Step sendNotificationStep(){
        return stepBuilderFactory.get("sendNotificationStep").tasklet(sendNotificationTasklet()).build();
    }


    //CREAMOS NUESTRO JOB CON EL FLUJO CONDICIONAL Q VAMOS A MANEJAR.
    @Bean
    public Job transactionPaymentsJob(){
        return jobBuilderFactory.get("transactionPaymentsJob")
                .start(validateAccountStep())
                .on("VALID").to(processPaymentStep())                                                                   //on() : valida el exit status del paso anterior. si es VALID , con el to() define hacia donde va a ir. con VALID va a ir a "processPaymentStep"
                .next(sendNotificationStep())                                                                           //llamas al paso de la notificacion sendNotificationStep

                .from(validateAccountStep())                                                                             //con from() vuelves al step q se pasa por argumento, para indicar el otro camino. el camino del error.
                .on("INVALID").to(cancelTransactionStep())
                .next(sendNotificationStep())

                .end().build();

    }


    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
}
