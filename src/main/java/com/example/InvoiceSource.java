package com.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
enum InvoiceSource {

    WEB(""),
    SCANSDK("scansdk"),
    LOCAL("local");

    private String value;

    static InvoiceSource of(String value) {
        for (InvoiceSource invoiceSource : InvoiceSource.values()) {
            if (invoiceSource.getValue().equals(value)) {
                return invoiceSource;
            }
        }

        log.error("Unknown invoice source value: {}, defaulting to {}", value, WEB);
        return WEB;
    }
}
