package com.api.payment.payment_api_service.controller.dto;

import com.api.payment.payment_api_service.domain.PayerType;
import com.api.payment.payment_api_service.domain.PaymentMethod;
import com.api.payment.payment_api_service.domain.PaymentStatusInit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;


public record PostPaymentResponse(
        PaymentMethod paymentMethod,
        BigDecimal paymentValue,
        Integer debtCode,
        PayerType payerType,
        String payerId,
        String cardNumber,

        @Schema(description = "Payment Id", example = "1023810931")
        String paymentId,

        @Schema(description = "Payment status", example = "PENDING")
        PaymentStatusInit paymentStatus
) {}
