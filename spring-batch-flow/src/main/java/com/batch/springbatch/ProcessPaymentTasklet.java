package com.batch.springbatch;

import com.batch.model.model.dao.TransferPaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ProcessPaymentTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        String transactionId = (String) chunkContext                                                                    //se recupera el id de transaccion.
                .getStepContext()                                                                                       //obtienes el contexto del step.
                .getJobParameters()                                                                                     //obtienes todos los parametros q se envían en el job parameter.
                .get("transactionId");                                                                                  //de todos lo parametros, se rescata solo el transactionId.

        log.info("--------------------se procesa el pago de la transaccion {} exitosamente---------------------", transactionId);

        transferPaymentRepository.updateTransactionStatus(true, transactionId);

        return RepeatStatus.FINISHED;
    }

    @Autowired
    private TransferPaymentRepository transferPaymentRepository;

}
