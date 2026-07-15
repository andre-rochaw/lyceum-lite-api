package br.techne.api.domain.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CriarUsuarioRequest(
        @NotBlank(message = "Login é obrigatório.")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Formato de e-mail inválido.")
        String login,

        @NotBlank(message = "Senha é obrigatória.")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "A senha deve conter ao menos uma letra maiúscula e um número.")
        String password,

        @NotBlank(message = "Nome é obrigatório.")
        String name,

        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve seguir o formato 999.999.999-99.")
        String cpf,

        @Pattern(regexp = "\\(\\d{2,3}\\)\\d{5}-\\d{4}", message = "Telefone deve seguir o formato (99)99999-9999.")
        String phone,

        LocalDate birthday,

        @NotBlank(message = "Nome de usuário é obrigatório.")
        @Size(max = 20, message = "Nome de usuário deve ter no máximo 20 caracteres.")
        String username,

        Boolean twoFactorEnabled,
        Boolean refreshTokenEnabled
) {
}
