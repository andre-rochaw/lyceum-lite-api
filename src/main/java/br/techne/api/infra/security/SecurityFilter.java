package br.techne.api.infra.security;

import br.techne.api.domain.usuario.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticateUserWithValidJwt authenticateUserWithValidJwt;

    private final ConcurrentHashMap<String, Usuario> usuarioCache = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = getAccessToken(request);
        String refreshToken = getRefreshToken(request);

        if (accessToken != null && tokenService.isAccessTokenValid(accessToken)) {
            authenticateUsuario(tokenService.getSubject(accessToken));
        } else if (refreshToken != null && tokenService.isRefreshTokenValid(refreshToken)) {
            String subject = tokenService.getRefreshSubject(refreshToken);
            Usuario usuario = getUsuarioFromCacheOrDb(subject);
            if (usuario != null && usuario.isRefreshTokenEnabled()) {
                String newAccessToken = tokenService.generateAccessToken(usuario);
                response.setHeader("Authorization", "Bearer " + newAccessToken);
                authenticateUsuario(subject);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUsuario(String subject) {
        Usuario usuario = getUsuarioFromCacheOrDb(subject);
        if (usuario != null) {
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private Usuario getUsuarioFromCacheOrDb(String subject) {
        Usuario cached = usuarioCache.get(subject);
        if (cached != null) {
            return cached;
        }
        Usuario usuario = authenticateUserWithValidJwt.findUsuarioAuthenticated(subject);
        if (usuario != null) {
            usuarioCache.put(subject, usuario);
        }
        return usuario;
    }

    private String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String getAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
