package com.api.payment.payment_api_service.controller.dto;

import com.api.payment.payment_api_service.domain.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PatchPaymentRequest (

    @NotNull
    @Schema(description = "Payment status", example = "PROCESSED_SUCCESSFULLY")
    PaymentStatus paymentStatus
) {}
