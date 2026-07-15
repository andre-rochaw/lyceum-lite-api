package br.techne.api.service;

import br.techne.api.domain.usuario.Usuario;
import br.techne.api.domain.usuario.UsuarioRepository;
import br.techne.api.domain.usuario.dto.CriarUsuarioRequest;
import br.techne.api.domain.usuario.dto.UsuarioLoginRequest;
import br.techne.api.domain.usuario.dto.UsuarioResponse;
import br.techne.api.infra.exceptions.ValidationException;
import br.techne.api.infra.security.AuthTokensDTO;
import br.techne.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UsuarioService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 15;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public UsuarioResponse criar(CriarUsuarioRequest request) {
        if (usuarioRepository.existsByLogin(request.login())) {
            throw new ValidationException("Login already exists.");
        }

        Usuario usuario = new Usuario(request);
        usuario.setPassword(passwordEncoder.encode(request.password()));
        return new UsuarioResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public AuthTokensDTO login(UsuarioLoginRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.login(), request.password());

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            Usuario usuario = (Usuario) authentication.getPrincipal();

            if (usuario.isLockoutEnabled()
                    && usuario.getLockoutEnd() != null
                    && usuario.getLockoutEnd().isBefore(java.time.LocalDateTime.now())) {
                usuario.resetAccessCount();
            }

            usuario.resetAccessCount();
            usuarioRepository.save(usuario);

            String accessToken = tokenService.generateAccessToken(usuario);
            String refreshToken = null;
            if (usuario.isRefreshTokenEnabled()) {
                refreshToken = tokenService.generateRefreshToken(usuario);
            }
            return new AuthTokensDTO(accessToken, refreshToken);
        } catch (BadCredentialsException ex) {
            Usuario usuario = usuarioRepository.findByLoginToHandle(request.login());
            if (usuario != null) {
                usuario.registerFailedLogin(MAX_ATTEMPTS, LOCK_MINUTES);
                usuarioRepository.save(usuario);
            }
            throw new BadCredentialsException("Wrong login or password.");
        }
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorToken(String tokenJwt) {
        String usuarioId = tokenService.getIdClaim(tokenJwt);
        UUID uuid = UUID.fromString(usuarioId);
        Usuario usuario = usuarioRepository.findById(uuid)
                .orElseThrow(() -> new ValidationException("Usuario not found for the provided token."));
        return new UsuarioResponse(usuario);
    }
}
