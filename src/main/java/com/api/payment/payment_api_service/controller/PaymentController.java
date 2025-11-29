package com.api.payment.payment_api_service.controller;

import com.api.payment.payment_api_service.controller.dto.PostPaymentRequest;
import com.api.payment.payment_api_service.controller.dto.PostPaymentResponse;
import com.api.payment.payment_api_service.domain.PaymentStatusInit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "Payment endpoints")
public class PaymentController {

    @PostMapping
    @Operation(summary = "Post payment", description = "Create a payment and returns its id and status (always PENDING)")
    public PostPaymentResponse postPayments(@RequestBody PostPaymentRequest request) {
        return new PostPaymentResponse(
                request.paymentMethod(),
                request.paymentValue(),
                request.debtCode(),
                request.payerType(),
                request.payerId(),
                request.cardNumber(),
                "12312321",
                PaymentStatusInit.PENDING
        );
    }
}
