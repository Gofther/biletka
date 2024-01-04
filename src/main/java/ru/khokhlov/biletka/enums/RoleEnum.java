package ru.khokhlov.biletka.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum RoleEnum implements GrantedAuthority {
    ADMIN("ADMIN"),
    ORGANIZATION("ORGANIZATION"),
    USER("USER");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
