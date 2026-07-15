package br.techne.api.domain.usuario.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioLoginRequest(
        @NotBlank String login,
        @NotBlank String password
) {
}
