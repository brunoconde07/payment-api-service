package com.api.payment.payment_api_service.controller.dto;

import com.api.payment.payment_api_service.domain.PaymentStatusDelete;

public record DeletePaymentResponse(
        String paymentId,
        PaymentStatusDelete paymentStatus
) {}
