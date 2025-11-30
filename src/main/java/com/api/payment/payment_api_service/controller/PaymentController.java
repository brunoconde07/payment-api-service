package com.api.payment.payment_api_service.controller;

import com.api.payment.payment_api_service.controller.dto.*;
import com.api.payment.payment_api_service.domain.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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

    @GetMapping
    @Operation(summary = "Get payments", description = "Get payments. Filters are optional")
    @ApiResponse(responseCode = "200", description = "Payments fetched successfully")
    public GetPaymentsResponse getPayments(@ParameterObject GetPaymentsFilter filter) {

        System.out.println(filter);

        PaymentResponse p1 = new PaymentResponse(
                PaymentMethod.CREDIT_CARD,
                new BigDecimal("5000"),
                123,
                PayerType.CNPJ,
                "123.123.123-89",
                "1234123412341234",
                "4354324",
                PaymentStatus.PROCESSED_SUCCESSFULLY
        );

        PaymentResponse p2 = new PaymentResponse(
                PaymentMethod.PIX,
                new BigDecimal("5000"),
                123,
                PayerType.CPF,
                "123.123.123-89",
                null,
                "4354324",
                PaymentStatus.PROCESSED_SUCCESSFULLY
        );

        return new GetPaymentsResponse(List.of(p1, p2));
    }

    @DeleteMapping("/{paymentId}")
    @Operation(summary = "Delete payment", description = "Change payment status to INACTIVE (soft delete)")
    @ApiResponse(responseCode = "200", description = "Payment deleted successfully", content = @Content(schema = @Schema(implementation = DeletePaymentResponse.class)))
    public DeletePaymentResponse deletePayment(@PathVariable String paymentId) {
        return new DeletePaymentResponse(paymentId, PaymentStatusDelete.INACTIVE);
    }
}
