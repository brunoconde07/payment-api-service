package com.api.payment.payment_api_service.controller;

import com.api.payment.payment_api_service.controller.dto.*;
import com.api.payment.payment_api_service.domain.*;
import com.api.payment.payment_api_service.domain.entity.PaymentEntity;
import com.api.payment.payment_api_service.repository.PaymentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "Payment endpoints")
public class PaymentController {

    private final PaymentRepository repository;

    public PaymentController(PaymentRepository repository) {
        this.repository = repository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Post payment", description = "Create a payment and returns its id and status (always PENDING)")
    @ApiResponse(responseCode = "201", description = "Payment created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostPaymentResponse.class)))
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

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Patch payment", description = "Update a payment status")
    @ApiResponse(responseCode = "200", description = "Payment updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatchPaymentResponse.class)))
    public ResponseEntity<PatchPaymentResponse> patchPaymentResponse(@RequestBody PatchPaymentRequest request) {
        PatchPaymentResponse response = new PatchPaymentResponse(
                request.paymentId(),
                request.paymentStatus()
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get payments", description = "Get payments. Filters are optional")
    @ApiResponse(responseCode = "200", description = "Payments fetched successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetPaymentsResponse.class)))
    public GetPaymentsResponse getPayments(@ParameterObject GetPaymentsFilter filter) {
        PaymentEntity probe = new PaymentEntity();

        probe.setPaymentStatus(filter.paymentStatus());
        probe.setDebtCode(filter.debtCode());
        probe.setPayerId(filter.payerId());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues();
        Example<PaymentEntity> example = Example.of(probe, matcher);

        List<PaymentEntity> paymentEntities = repository.findAll(example);

        List<PaymentResponse> paymentDtos = paymentEntities.stream().map(this::mapToResponse).toList();

        return new GetPaymentsResponse(paymentDtos);
    }

    @DeleteMapping(value = "/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete payment", description = "Change payment status to INACTIVE (soft delete)")
    @ApiResponse(responseCode = "200", description = "Payment deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeletePaymentResponse.class)))
    public DeletePaymentResponse deletePayment(@PathVariable String paymentId) {
        return new DeletePaymentResponse(paymentId, PaymentStatusDelete.INACTIVE);
    }

    private PaymentResponse mapToResponse(PaymentEntity entity) {
        return new PaymentResponse(
                entity.getPaymentMethod(),
                entity.getPaymentValue(),
                entity.getDebtCode(),
                entity.getPayerType(),
                entity.getPayerId(),
                entity.getCardNumber(),
                entity.getId().toString(),
                entity.getPaymentStatus()
        );
    }
}
