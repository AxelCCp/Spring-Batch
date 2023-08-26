package com.batch.springbatch;

import com.batch.model.entity.TransferPayment;
import com.batch.model.model.dao.TransferPaymentRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidateAccountTasklet implements Tasklet {

    //chunkContext : obj para entrar al contexto de los steps de spring batch.
    //stepContribution : sirve para settear los estados de salida de cada step.

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        Boolean filterIsAproved = true;

        String transactionId = (String) chunkContext                                                            //se recupera el id de transaccion.
                                    .getStepContext()                                                                   //obtienes el contexto del step.
                                    .getJobParameters()                                                                 //obtienes todos los parametros q se envían en el job parameter.
                                    .get("transactionId");                                                              //de todos lo parametros, se rescata solo el transactionId.

        TransferPayment transferPayment = transferPaymentRepository.findById(transactionId).orElseThrow();              //orElseThrow() : busca el registro, y si no lo encuentra, lanza una exception.

        if(!transferPayment.getIsEnabled()){                                                                             //si la cuenta está en false (inactiva)
            //error pq la cuenta esta inactiva
            chunkContext.getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getExecutionContext()
                    .put("message", "Error, porque la cuenta esta inactiva");                                           //se manda un obj llave - valor, al contexto de spring batch, para q ese mensaje esté disponible para cualquier tasklet.
            filterIsAproved = false;
        }

        if(transferPayment.getAmountPaid() > transferPayment.getAvailableBalance()){                                    //si el valor q pago es mayor al saldo disponible.
            //error por saldo insuficiente
            chunkContext.getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getExecutionContext()
                    .put("message", "Error, debido a saldo insuficiente");                                              //se manda un obj llave - valor, al contexto de spring batch, para q ese mensaje esté disponible para cualquier tasklet.
            filterIsAproved = false;
        }

        ExitStatus exitStatus = null;
        if(filterIsAproved){
            exitStatus = new ExitStatus("VALID");
            stepContribution.setExitStatus(exitStatus);                                                                 //con el stepContribution tomamos las deciciones dentro del spring batch.
        } else {
            exitStatus = new ExitStatus("INVALID");
            stepContribution.setExitStatus(exitStatus);
        }

        //CON TOD0 LO ANTERIOR SE SETTEA LA RESPUESTA DE TASKLET

        return RepeatStatus.FINISHED;
    }


    @Autowired
    private TransferPaymentRepository transferPaymentRepository;
}
