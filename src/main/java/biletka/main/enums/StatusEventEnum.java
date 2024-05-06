package biletka.main.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StatusEventEnum {
    ACTIVE("ACTIVE"),
    BANNED("BANNED"),
    INACTIVE("INACTIVE");

    private final String value;

    private String getStatusEvent() {
        return value;
    }
}
