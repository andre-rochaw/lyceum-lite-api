package br.techne.api.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    UserDetails findByLogin(String login);

    UserDetails findByUsername(String username);

    @Query("""
            SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
            FROM Usuario u WHERE u.login = :login
            """)
    boolean existsByLogin(String login);

    @Query("""
            SELECT u FROM Usuario u WHERE u.login = :login
            """)
    Usuario findByLoginToHandle(String login);
}
