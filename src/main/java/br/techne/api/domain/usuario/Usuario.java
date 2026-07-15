package br.techne.api.domain.usuario;

import br.techne.api.domain.usuario.dto.CriarUsuarioRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "usuario")
@Entity(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "senha", nullable = false)
    private String password;

    @Column(name = "nome_usuario", unique = true, length = 20)
    private String username;

    @Column(name = "nome")
    private String name;

    @Column(name = "cpf", length = 14, unique = true)
    private String cpf;

    @Column(name = "telefone", length = 14)
    private String phone;

    @Column(name = "data_nascimento")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private UsuarioRole role;

    @Column(name = "tentativas_falhas")
    private int accessFailedCount = 0;

    @Column(name = "bloqueado")
    private boolean lockoutEnabled = false;

    @Column(name = "bloqueio_fim")
    private LocalDateTime lockoutEnd;

    @Column(name = "refresh_token_ativo")
    private boolean refreshTokenEnabled = false;

    @Column(name = "dois_fatores")
    private boolean twoFactorEnabled = false;

    @Column(name = "token_email")
    private String tokenMail;

    @Column(name = "token_expiracao")
    private LocalDateTime tokenExpiration;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "atualizado_em")
    private LocalDateTime updatedAt;

    public Usuario(CriarUsuarioRequest request) {
        this.login = request.login();
        this.password = request.password();
        this.name = request.name();
        this.username = request.username();
        this.cpf = request.cpf();
        this.phone = request.phone();
        this.birthday = request.birthday();
        this.twoFactorEnabled = Boolean.TRUE.equals(request.twoFactorEnabled());
        this.refreshTokenEnabled = request.refreshTokenEnabled() == null || request.refreshTokenEnabled();
        this.role = UsuarioRole.USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UsuarioRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public String getDisplayUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (!lockoutEnabled) {
            return true;
        }
        if (lockoutEnd != null && LocalDateTime.now().isAfter(lockoutEnd)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isAccountNonLocked();
    }

    public void resetAccessCount() {
        this.accessFailedCount = 0;
        this.lockoutEnabled = false;
        this.lockoutEnd = null;
    }

    public void registerFailedLogin(int maxAttempts, int lockMinutes) {
        this.accessFailedCount = this.accessFailedCount + 1;
        if (this.accessFailedCount >= maxAttempts) {
            this.lockoutEnabled = true;
            this.lockoutEnd = LocalDateTime.now().plusMinutes(lockMinutes);
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
