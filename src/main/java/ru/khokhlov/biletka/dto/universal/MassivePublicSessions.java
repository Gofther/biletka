package ru.khokhlov.biletka.dto.universal;

import java.util.List;

public record MassivePublicSessions(
        List<PublicSession> publicSessions
) {
}
