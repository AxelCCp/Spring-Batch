package com.batch.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="transfer_payment_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferPayment {

    @Id
    @Column(name="transaction_id", nullable = false)                                                                    //no puede estar nulo
    private String transactionId;

    @Column(name="available_balance", nullable = false)
    private Double availableBalance;

    @Column(name="amount_paid", nullable = false)
    private Double amountPaid;

    @Column(name="is_enabled", nullable = false)
    private Boolean isEnabled;

    @Column(name="id_processed", nullable = false)
    private Boolean isProcessed;

    private String error;

}
