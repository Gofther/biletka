package ru.khokhlov.biletka.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    ACTIVE("active"),
    VISITED("visited"),
    REFUNDED("refunded"),
    CANCELED("canceled");

    private final String statusName;
}
