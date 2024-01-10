package ru.khokhlov.biletka.dto.universal;

public record PublicClient(
    Long id,
    String fullName,
    String phone,
    String email,
    String birthday
) {
}
