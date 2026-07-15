package br.techne.api.controller;

import br.techne.api.domain.usuario.dto.CriarUsuarioRequest;
import br.techne.api.domain.usuario.dto.UsuarioLoginRequest;
import br.techne.api.domain.usuario.dto.UsuarioResponse;
import br.techne.api.infra.security.AccessTokenDTO;
import br.techne.api.infra.security.AuthTokensDTO;
import br.techne.api.infra.utils.CookieManager;
import br.techne.api.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CookieManager cookieManager;

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<AccessTokenDTO> login(@RequestBody @Valid UsuarioLoginRequest request,
                                                HttpServletResponse response) {
        AuthTokensDTO tokens = usuarioService.login(request);
        if (tokens.refreshToken() != null) {
            cookieManager.addRefreshTokenCookie(response, tokens.refreshToken());
        }
        return ResponseEntity.ok(new AccessTokenDTO(tokens.accessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        cookieManager.clearRefreshTokenCookie(response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/criar")
    @Transactional
    public ResponseEntity<UsuarioResponse> criar(@RequestBody @Valid CriarUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(request));
    }

    @GetMapping("/por-token-jwt")
    public ResponseEntity<UsuarioResponse> buscarPorToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(usuarioService.buscarPorToken(token));
    }
}
