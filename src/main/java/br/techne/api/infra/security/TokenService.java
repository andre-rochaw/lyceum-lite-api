package br.techne.api.infra.security;

import br.techne.api.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class TokenService {

    @Value("${api.security.access.secret}")
    private String accessSecret;

    @Value("${api.security.refresh.secret}")
    private String refreshSecret;

    @Value("${api.security.issuer}")
    private String issuer;

    public String generateAccessToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(accessSecret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId().toString())
                    .withClaim("role", usuario.getRole().toString())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(accessTokenExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating JWT access token", exception);
        }
    }

    public String generateRefreshToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(refreshSecret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId().toString())
                    .withClaim("refreshId", UUID.randomUUID().toString())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(refreshTokenExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating refresh token", exception);
        }
    }

    public boolean isAccessTokenValid(String tokenJwt) {
        return isTokenValid(tokenJwt, accessSecret);
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        return isTokenValid(refreshToken, refreshSecret);
    }

    public String getSubject(String tokenJwt) {
        return getSubject(tokenJwt, accessSecret);
    }

    public String getRefreshSubject(String refreshToken) {
        return getSubject(refreshToken, refreshSecret);
    }

    public String getIdClaim(String tokenJwt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(accessSecret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(tokenJwt)
                    .getClaim("id")
                    .asString();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Invalid or expired JWT token.");
        }
    }

    private boolean isTokenValid(String tokenJwt, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
            verifier.verify(tokenJwt);
            return true;
        } catch (JWTVerificationException | IllegalArgumentException exception) {
            return false;
        }
    }

    private String getSubject(String tokenJwt, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(tokenJwt)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Invalid or expired JWT token.");
        }
    }

    private Instant accessTokenExpiration() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }

    private Instant refreshTokenExpiration() {
        return LocalDateTime.now().plusDays(15).toInstant(ZoneOffset.of("-03:00"));
    }
}
