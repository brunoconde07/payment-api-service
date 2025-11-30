package com.api.payment.payment_api_service.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PaymentMethod {
    @JsonProperty("BANK SLIP")
    BANK_SLIP,

    @JsonProperty("PIX")
    PIX,

    @JsonProperty("CREDIT CARD")
    CREDIT_CARD,

    @JsonProperty("DEBT CARD")
    DEBIT_CARD
}
