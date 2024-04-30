package biletka.main.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidDataException extends RuntimeException{
    private final List<ErrorMessage> errorMessage;

    public InvalidDataException(List<ErrorMessage> errorMessage) {
        System.out.println(errorMessage);
        super();
        this.errorMessage = errorMessage;
    }

    public List<ErrorMessage> getInvalidFields() {
        return errorMessage;
    }
}
