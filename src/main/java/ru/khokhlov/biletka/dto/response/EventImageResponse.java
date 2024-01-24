package ru.khokhlov.biletka.dto.response;
import com.fasterxml.jackson.annotation.JsonProperty;
public record EventImageResponse(
    Long id,
    @JsonProperty("image_name")
    String imageName,
    @JsonProperty("image_type")
    String imageType
) {
}
