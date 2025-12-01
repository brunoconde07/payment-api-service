package com.api.payment.payment_api_service.e2e;

import com.api.payment.payment_api_service.domain.*;
import com.api.payment.payment_api_service.domain.entity.PaymentEntity;
import com.api.payment.payment_api_service.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentApiTest {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @Autowired
    private PaymentRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
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

        repository.save(p1);
        repository.save(p2);
        repository.save(p3);
        repository.save(p4);
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
}