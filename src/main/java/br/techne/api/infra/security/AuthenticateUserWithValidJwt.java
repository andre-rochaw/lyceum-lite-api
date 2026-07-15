package br.techne.api.infra.security;

import br.techne.api.domain.usuario.Usuario;
import br.techne.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateUserWithValidJwt {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario findUsuarioAuthenticated(String login) {
        return usuarioRepository.findByLoginToHandle(login);
    }
}
