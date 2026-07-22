package br.techne.api.domain.disciplina;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DisciplinaRepository extends JpaRepository<Disciplina, UUID> {

    boolean existsByNomeIgnoreCase(String nome);

    Page<Disciplina> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Disciplina> findByCursoId(UUID cursoId, Pageable pageable);

    Page<Disciplina> findByCursoIdAndNomeContainingIgnoreCase(UUID cursoId, String nome, Pageable pageable);
}
