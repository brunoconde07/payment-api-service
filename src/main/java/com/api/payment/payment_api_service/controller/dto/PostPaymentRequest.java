package com.api.payment.payment_api_service.controller.dto;

import com.api.payment.payment_api_service.domain.PayerType;
import com.api.payment.payment_api_service.domain.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

import java.math.BigInteger;


public record PostPaymentRequest(
        @Schema(description = "Payment method", example = "PIX")
        PaymentMethod paymentMethod,

        @Schema(description = "Payment value", example = "100")
        BigInteger paymentValue,

        @Schema(description = "Debt code", example = "123")
        Integer debtCode,

        @Schema(description = "Type of payer document", example = "CPF")
        PayerType payerType,

        @Schema(description = "Payer id", example = "123.123.123-00")
        String payerId,

        @Pattern(regexp = "^\\d{16}$", message = "Card number must contain exactly 16 digits")
        String cardNumber
) {}
