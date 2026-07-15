package br.techne.api.infra.security;

import br.techne.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = usuarioRepository.findByLogin(username);
        if (userDetails == null) {
            userDetails = usuarioRepository.findByUsername(username);
        }
        if (userDetails == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        return userDetails;
    }
}
