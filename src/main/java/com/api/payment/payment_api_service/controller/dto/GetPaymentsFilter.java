package com.api.payment.payment_api_service.controller.dto;

import com.api.payment.payment_api_service.domain.PaymentStatus;

public record GetPaymentsFilter(
        Integer debtCode,
        String payerId,
        PaymentStatus paymentStatus
) {}
