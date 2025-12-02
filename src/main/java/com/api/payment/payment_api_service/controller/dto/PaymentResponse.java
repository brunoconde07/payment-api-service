package com.api.payment.payment_api_service.controller.dto;

import com.api.payment.payment_api_service.domain.PayerType;
import com.api.payment.payment_api_service.domain.PaymentMethod;
import com.api.payment.payment_api_service.domain.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigInteger;

public record PaymentResponse(
        PaymentMethod paymentMethod,
        BigInteger paymentValue,
        Integer debtCode,
        PayerType payerType,
        String payerId,

        @Schema(description = "Nullable (if applicable)", nullable = true)
        String cardNumber,

        String paymentId,
        PaymentStatus paymentStatus
) {}
