package com.api.payment.payment_api_service.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PaymentMethod {
    @JsonProperty("BANK_SLIP")
    BANK_SLIP,

    @JsonProperty("PIX")
    PIX,

    @JsonProperty("CREDIT_CARD")
    CREDIT_CARD,

    @JsonProperty("DEBT_CARD")
    DEBIT_CARD
}
