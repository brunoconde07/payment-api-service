package com.api.payment.payment_api_service.domain.entity;

import com.api.payment.payment_api_service.domain.PayerType;
import com.api.payment.payment_api_service.domain.PaymentMethod;
import com.api.payment.payment_api_service.domain.PaymentStatus;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private BigInteger paymentValue;

    @Column(nullable = false)
    private Integer debtCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayerType payerType;

    @Column(nullable = false)
    private String payerId;

    @Column(nullable = true)
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    public PaymentEntity() {}

    public PaymentEntity(PaymentMethod paymentMethod, BigInteger paymentValue, Integer debtCode,
                         PayerType payerType, String payerId, String cardNumber, PaymentStatus paymentStatus) {
        this.paymentMethod = paymentMethod;
        this.paymentValue = paymentValue;
        this.debtCode = debtCode;
        this.payerType = payerType;
        this.payerId = payerId;
        this.cardNumber = cardNumber;
        this.paymentStatus = paymentStatus;
    }

    public UUID getId() { return id; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public BigInteger getPaymentValue() { return paymentValue; }
    public Integer getDebtCode() { return debtCode; }
    public PayerType getPayerType() { return payerType; }
    public String getPayerId() { return payerId; }
    public String getCardNumber() { return cardNumber; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setDebtCode(Integer debtCode) {
        this.debtCode = debtCode;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }
}