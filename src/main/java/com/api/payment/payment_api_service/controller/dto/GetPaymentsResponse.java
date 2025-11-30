package com.api.payment.payment_api_service.controller.dto;

import java.util.List;

public record GetPaymentsResponse(List<PaymentResponse> data) {}
