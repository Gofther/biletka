package ru.khokhlov.biletka.exception;

public record ErrorMessage(
        String fieldName,
        String message) {
}
