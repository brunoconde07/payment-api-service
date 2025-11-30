package com.api.payment.payment_api_service.controller.dto;

import com.api.payment.payment_api_service.domain.PayerType;
import com.api.payment.payment_api_service.domain.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;


public record PostPaymentRequest(
        @Schema(description = "Payment method", example = "PIX")
        PaymentMethod paymentMethod,

        @Schema(description = "Payment value", example = "100")
        BigDecimal paymentValue,

        @Schema(description = "Debt code", example = "123")
        Integer debtCode,

        @Schema(description = "Type of payer document", example = "CPF")
        PayerType payerType,

        @Schema(description = "Payer id", example = "123.123.123-00")
        String payerId,

        @Schema(description = "Card number", example = "1234123412341234")
        String cardNumber
) {}
