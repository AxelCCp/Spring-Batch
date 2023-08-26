package com.batch.springbatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class SendNotificationTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        String transactionId = (String) chunkContext                                                                    //se recupera el id de transaccion.
                .getStepContext()                                                                                       //obtienes el contexto del step.
                .getJobParameters()                                                                                     //obtienes todos los parametros q se envían en el job parameter.
                .get("transactionId");

        log.info("+++++++++++++++ Se a enviado una notificacion al cliente para la transaccion: ".concat(transactionId));

        return RepeatStatus.FINISHED;

    }
}
