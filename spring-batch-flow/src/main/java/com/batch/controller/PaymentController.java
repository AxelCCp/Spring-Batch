package com.batch.controller;

import com.batch.controller.request.TransferPaymentDTO;
import com.batch.model.entity.TransferPayment;
import com.batch.model.model.dao.TransferPaymentRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    @PostMapping("/transfer")
    public ResponseEntity<?>transferPayment(@RequestBody TransferPaymentDTO transferPaymentDTO) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        String transactionId = UUID.randomUUID().toString();                                                            //uuid es una clase que genera un id unico

        //se usa el patron de diseño builder, gracias a la anotacion builder en la clase entity.
        TransferPayment transferPayment = TransferPayment.builder()
                .transactionId(transactionId)
                .availableBalance(transferPaymentDTO.getAvailableBalance())
                .amountPaid(transferPaymentDTO.getAmountPaid())
                .isEnabled(transferPaymentDTO.getIsEnabled())
                .isProcessed(false)
                .build();

        transferPaymentRepository.save(transferPayment);

        //parametros para cada uno de los step
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", UUID.randomUUID().toString())                                                          //identificacion de la ejecucion del job.
                .addString("transactionId", transactionId)                                                              //id de la transaccion.
                .toJobParameters();

        jobLauncher.run(job, jobParameters);

        Map<String, Object> httpResponse = new HashMap<>();
        httpResponse.put("TransactionId", transactionId);
        httpResponse.put("Message", "Transaccion recibida");
        return ResponseEntity.ok(httpResponse);
    }


    @Autowired
    private TransferPaymentRepository transferPaymentRepository;

    @Autowired
    private JobLauncher jobLauncher;                                                                                    //se inyecta directamente desde la dependencia de spring batch

    @Autowired
    private Job job;

}
