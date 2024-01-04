package ru.khokhlov.biletka.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public record AuthFormDTO(
        @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b", message = "Invalid email format")
        @NotBlank(message = "Email is mandatory")
        String email,

        /*
         *     Минимум 8 символов.
         *     Содержит хотя бы одну строчную букву (a-z).
         *     Содержит хотя бы одну заглавную букву (A-Z).
         *     Содержит хотя бы одну цифру (0-9).
         *     Содержит хотя бы один специальный символ из [@ $!%*?&].
         */
        @Pattern(regexp = "^(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid password format")
        @NotBlank(message = "Password is mandatory")
        String password
) {
}
