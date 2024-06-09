package biletka.main.exception;

public record ErrorMessage(
        String fieldName,
        String message
) {
}
