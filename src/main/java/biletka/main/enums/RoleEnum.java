package biletka.main.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum RoleEnum implements GrantedAuthority {
    ADMIN("ADMIN"),
    ORGANIZATION("ORGANIZATION"),
    CLIENT("CLIENT");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
