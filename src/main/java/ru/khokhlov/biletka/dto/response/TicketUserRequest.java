package ru.khokhlov.biletka.dto.response;

public record TicketUserRequest(
        Long id,
        String name,
        String placeName,
        String address,
        String dateTime,
        String hall,
        Integer rowNumber,
        Integer seatNumber,
        Integer price,
        String email,
        String phone,
        String fullName
) {
}
