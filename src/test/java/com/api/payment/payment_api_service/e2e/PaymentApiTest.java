package com.api.payment.payment_api_service.e2e;

import com.api.payment.payment_api_service.controller.dto.PostPaymentRequest;
import com.api.payment.payment_api_service.controller.dto.PostPaymentResponse;
import com.api.payment.payment_api_service.domain.*;
import com.api.payment.payment_api_service.domain.entity.PaymentEntity;
import com.api.payment.payment_api_service.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;

import java.math.BigInteger;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentApiTest {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @Autowired
    private PaymentRepository repository;

    @BeforeEach
    void setup() {
        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        repository.deleteAll();

        PaymentEntity p1 = new PaymentEntity(
                PaymentMethod.CREDIT_CARD,
                new BigInteger("500000"),
                1,
                PayerType.CPF,
                "123-123-123-00",
                "1234123412341234",
                PaymentStatus.PENDING
        );

        PaymentEntity p2 = new PaymentEntity(
                PaymentMethod.PIX,
                new BigInteger("15050"),
                2,
                PayerType.CNPJ,
                "00-000-000/1234-00",
                null,
                PaymentStatus.PROCESSED_SUCCESSFULLY
        );

        PaymentEntity p3 = new PaymentEntity(
                PaymentMethod.PIX,
                new BigInteger("45050"),
                99,
                PayerType.CNPJ,
                "00-000-000/5678-00",
                null,
                PaymentStatus.PROCESSING_ERROR
        );

        PaymentEntity p4 = new PaymentEntity(
                PaymentMethod.DEBIT_CARD,
                new BigInteger("45050"),
                0,
                PayerType.CNPJ,
                "00-000-000/1234-00",
                "0000111122223333",
                PaymentStatus.INACTIVE
        );

        PaymentEntity p5 = new PaymentEntity(
                PaymentMethod.PIX,
                new BigInteger("2300"),
                0,
                PayerType.CNPJ,
                "00-000-000/1234-00",
                null,
                PaymentStatus.INACTIVE
        );

        repository.save(p1);
        repository.save(p2);
        repository.save(p3);
        repository.save(p4);
        repository.save(p5);
    }

    @Test
    void shouldValidateJsonSchema() {
        webTestClient.get()
                .uri("/api/v1/payments")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(jsonBody -> {
                    assertThat(jsonBody, matchesJsonSchemaInClasspath("payment-schema.json"));
                });
    }

    @Test
    void shouldFilterByStatus() {
        String paymentStatus = "INACTIVE";
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/payments")
                        .queryParam("paymentStatus", paymentStatus)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.length()").isEqualTo(2)
                .jsonPath("$.data[*].paymentStatus")
                .value(responseList -> {
                    List<String> statuses = (List<String>) responseList;
                    assertThat(statuses, everyItem(equalTo(paymentStatus)));
                });
    }

    @Test
    void shouldFilterByPayerId() {
        String payerId = "00-000-000/1234-00";
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/payments")
                        .queryParam("payerId", payerId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.length()").isEqualTo(3)
                .jsonPath("$.data[*].payerId")
                .value(responseList -> {
                    List<String> payerIds = (List<String>) responseList;
                    assertThat(payerIds, everyItem(equalTo(payerId)));
                });
    }

    @Test
    void shouldFilterByDebtCode() {
        Integer debtCode = 0;
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/payments")
                        .queryParam("debtCode", debtCode)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.length()").isEqualTo(2)
                .jsonPath("$.data[*].debtCode")
                .value(responseList -> {
                    List<Integer> statuses = (List<Integer>) responseList;
                    assertThat(statuses, everyItem(equalTo(debtCode)));
                });
    }

    @Test
    void shouldPostPaymentWhenValidRequest() {
        repository.deleteAll();

        PostPaymentRequest postPaymentRequest = new PostPaymentRequest(
                PaymentMethod.CREDIT_CARD,
                new BigInteger("500000"),
                1,
                PayerType.CPF,
                "123-123-123-00",
                "1234123412341234"
        );

        PostPaymentResponse postPaymentResponse = webTestClient.post()
                .uri("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postPaymentRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PostPaymentResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(postPaymentResponse).isNotNull();
        assertThat(postPaymentResponse.paymentMethod()).isEqualTo(postPaymentRequest.paymentMethod());
        assertThat(postPaymentResponse.paymentValue()).isEqualTo(postPaymentRequest.paymentValue());
        assertThat(postPaymentResponse.debtCode()).isEqualTo(postPaymentRequest.debtCode());
        assertThat(postPaymentResponse.payerType()).isEqualTo(postPaymentRequest.payerType());
        assertThat(postPaymentResponse.cardNumber()).isEqualTo(postPaymentRequest.cardNumber());

        List<PaymentEntity> savedPaymentEntities = repository.findAll();

        assertThat(savedPaymentEntities).hasSize(1);
        PaymentEntity dbRecord = savedPaymentEntities.get(0);

        assertThat(dbRecord.getId().toString()).isEqualTo(postPaymentResponse.paymentId());

        assertThat(dbRecord.getPaymentMethod()).isEqualTo(postPaymentRequest.paymentMethod());
        assertThat(dbRecord.getPaymentValue()).isEqualTo(postPaymentRequest.paymentValue());
        assertThat(dbRecord.getDebtCode()).isEqualTo(postPaymentRequest.debtCode());
        assertThat(dbRecord.getPayerType()).isEqualTo(postPaymentRequest.payerType());
        assertThat(dbRecord.getCardNumber()).isEqualTo(postPaymentRequest.cardNumber());
    }

    @Test
    void shouldNotPostPaymentWhenInvalidRequest() {

        String validCardNumber = "1234123412341234";
        String invalidCardNumberPattern = "any";

        PostPaymentRequest postPaymentRequest1InvalidNullCardNumberForValidPaymentMethod = new PostPaymentRequest(
                PaymentMethod.CREDIT_CARD,
                new BigInteger("500000"),
                1,
                PayerType.CPF,
                "123-123-123-00",
                null
        );

        PostPaymentRequest postPaymentRequest2InvalidCardNumberPatternForValidPaymentMethod = new PostPaymentRequest(
                PaymentMethod.DEBIT_CARD,
                new BigInteger("500000"),
                1,
                PayerType.CPF,
                "123-123-123-00",
                invalidCardNumberPattern
        );

        PostPaymentRequest postPaymentRequest3InvalidPaymentMethodForValidCardNumber = new PostPaymentRequest(
                PaymentMethod.PIX,
                new BigInteger("500000"),
                1,
                PayerType.CPF,
                "123-123-123-00",
                validCardNumber
        );

        PostPaymentRequest postPaymentRequest4InvalidPaymentMethodForValidCardNumber = new PostPaymentRequest(
                PaymentMethod.BANK_SLIP,
                new BigInteger("500000"),
                1,
                PayerType.CPF,
                "123-123-123-00",
                validCardNumber
        );

        webTestClient.post()
                .uri("/api/v1/payments")
                .bodyValue(postPaymentRequest1InvalidNullCardNumberForValidPaymentMethod)
                .exchange()
                .expectStatus().isBadRequest();


        webTestClient.post()
                .uri("/api/v1/payments")
                .bodyValue(postPaymentRequest2InvalidCardNumberPatternForValidPaymentMethod)
                .exchange()
                .expectStatus().isBadRequest();

        webTestClient.post()
                .uri("/api/v1/payments")
                .bodyValue(postPaymentRequest3InvalidPaymentMethodForValidCardNumber)
                .exchange()
                .expectStatus().isBadRequest();

        webTestClient.post()
                .uri("/api/v1/payments")
                .bodyValue(postPaymentRequest4InvalidPaymentMethodForValidCardNumber)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldSoftDeleteAndPreserveStatusWhenNotPending() {
        repository.deleteAll();
        PaymentEntity originalPaymentEntity = repository.save(new PaymentEntity(
                PaymentMethod.CREDIT_CARD,
                new BigInteger("500000"),
                1,
                PayerType.CPF,
                "123-123-123-00",
                "1234123412341234",
                PaymentStatus.PROCESSED_SUCCESSFULLY
        ));

        webTestClient.delete()
                .uri("/api/v1/payments/" + originalPaymentEntity.getId())
                .exchange()
                .expectStatus().isOk();

        PaymentEntity deletedPayment = repository.findById(originalPaymentEntity.getId()).orElseThrow();
        assertThat(deletedPayment.getIsDeleted()).isTrue();
        assertThat(deletedPayment.getPaymentStatus()).isEqualTo(PaymentStatus.PROCESSED_SUCCESSFULLY);
    }

    @Test
    void shouldSoftDeleteAndInactiveWhenStatusIsPending() {
        repository.deleteAll();
        PaymentEntity originalPaymentEntity = repository.save(new PaymentEntity(
                PaymentMethod.CREDIT_CARD,
                new BigInteger("500000"),
                1,
                PayerType.CPF,
                "123-123-123-00",
                "1234123412341234",
                PaymentStatus.PENDING
        ));

        webTestClient.delete()
                .uri("/api/v1/payments/" + originalPaymentEntity.getId())
                .exchange()
                .expectStatus().isOk();

        PaymentEntity deletedPayment = repository.findById(originalPaymentEntity.getId()).orElseThrow();
        assertThat(deletedPayment.getIsDeleted()).isTrue();
        assertThat(deletedPayment.getPaymentStatus()).isEqualTo(PaymentStatus.INACTIVE);
    }
}