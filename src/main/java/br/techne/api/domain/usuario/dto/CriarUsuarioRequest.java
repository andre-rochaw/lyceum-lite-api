package br.techne.api.domain.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CriarUsuarioRequest(
        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
        String login,

        @NotBlank
        @Size(min = 8, message = "Password must have at least 8 characters")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one uppercase letter and one number")
        String password,

        @NotBlank
        String name,

        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must follow 999.999.999-99")
        String cpf,

        @Pattern(regexp = "\\(\\d{2,3}\\)\\d{5}-\\d{4}", message = "Phone must follow (99)99999-9999")
        String phone,

        LocalDate birthday,

        @NotBlank
        @Size(max = 20, message = "Username max length is 20")
        String username,

        Boolean twoFactorEnabled,
        Boolean refreshTokenEnabled
) {
}
