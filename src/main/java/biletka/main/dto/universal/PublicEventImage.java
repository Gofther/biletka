package biletka.main.dto.universal;

public record PublicEventImage(
        byte[] imageData,
        String name,
        String type
) {
}
