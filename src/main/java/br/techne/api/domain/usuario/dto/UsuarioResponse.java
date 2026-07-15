package br.techne.api.domain.usuario.dto;

import br.techne.api.domain.usuario.Usuario;
import br.techne.api.domain.usuario.UsuarioRole;

import java.time.LocalDate;

public record UsuarioResponse(
        String id,
        String login,
        String username,
        String name,
        String cpf,
        String phone,
        LocalDate birthday,
        Boolean twoFactorEnabled,
        Boolean refreshTokenEnabled,
        UsuarioRole role
) {
    public UsuarioResponse(Usuario usuario) {
        this(
                usuario.getId().toString(),
                usuario.getLogin(),
                usuario.getDisplayUsername(),
                usuario.getName(),
                usuario.getCpf(),
                usuario.getPhone(),
                usuario.getBirthday(),
                usuario.isTwoFactorEnabled(),
                usuario.isRefreshTokenEnabled(),
                usuario.getRole()
        );
    }
}
