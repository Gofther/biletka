package ru.khokhlov.biletka.dto.response;

import java.util.List;

public record PlaceResponse(
        String name,
        String city,
        String address,
        List<Integer> hall
) {
}
