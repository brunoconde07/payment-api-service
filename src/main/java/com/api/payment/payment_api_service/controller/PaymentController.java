package com.api.payment.payment_api_service.controller;

import com.api.payment.payment_api_service.controller.dto.PatchPaymentRequest;
import com.api.payment.payment_api_service.controller.dto.PatchPaymentResponse;
import com.api.payment.payment_api_service.controller.dto.PostPaymentRequest;
import com.api.payment.payment_api_service.controller.dto.PostPaymentResponse;
import com.api.payment.payment_api_service.domain.PaymentStatusInit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "Payment endpoints")
public class PaymentController {

    @PostMapping
    @Operation(summary = "Post payment", description = "Create a payment and returns its id and status (always PENDING)")
    @ApiResponse(responseCode = "201", description = "Payment created successfully")
    public ResponseEntity<PostPaymentResponse> postPayments(@RequestBody PostPaymentRequest request) {
        PostPaymentResponse response = new PostPaymentResponse(
                request.paymentMethod(),
                request.paymentValue(),
                request.debtCode(),
                request.payerType(),
                request.payerId(),
                request.cardNumber(),
                "12312321",
                PaymentStatusInit.PENDING
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping
    @Operation(summary = "Patch payment", description = "Update a payment status")
    @ApiResponse(responseCode = "200", description = "Payment updated successfully")
    public ResponseEntity<PatchPaymentResponse> patchPaymentResponse(@RequestBody PatchPaymentRequest request) {
        PatchPaymentResponse response = new PatchPaymentResponse(
                request.paymentId(),
                request.paymentStatus()
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
