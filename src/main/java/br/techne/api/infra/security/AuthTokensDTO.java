package br.techne.api.infra.security;

public record AuthTokensDTO(String accessToken, String refreshToken) {
}
