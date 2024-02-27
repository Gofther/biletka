package ru.khokhlov.biletka.dto.response;

public record TicketUserResponse(
        Long id,
        String name,
        String placeName,
        String address,
        String dateTime,
        String hall,
        Integer rowNumber,
        Integer seatNumber,
        String price,
        String email,
        String phone,
        String fullName
) {
}
