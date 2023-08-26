package com.batch.model.model.dao;

import com.batch.model.entity.TransferPayment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransferPaymentRepository extends CrudRepository<TransferPayment, String> {

    @Modifying                                                                                                          //Modifying : le decimos a jpa q va a ser un update.
    @Transactional
    @Query("update TransferPayment tpe set tpe.isProcessed = ?1 where tpe.transactionId = ?2")
    void updateTransactionStatus(Boolean newValor, String transactionId);


    @Modifying
    @Transactional
    @Query("update TransferPayment tpe set tpe.isProcessed = ?1, tpe.error = ?2 where tpe.transactionId = ?3")
    void updateTransactionStatusError(Boolean newValor, String error, String transactionId);



}
