package com.api.payment.payment_api_service.controller.dto;

import com.api.payment.payment_api_service.domain.PaymentStatusPatch;
import io.swagger.v3.oas.annotations.media.Schema;

public record PatchPaymentResponse(

    @Schema(description = "Payment id", example = "12392912")
    String paymentId,

    @Schema(description = "Payment status", example = "PROCESSED_SUCCESSFULLY")
    PaymentStatusPatch paymentStatus
) {}
