package br.techne.api.domain.aluno;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Page<Aluno> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
