package br.techne.api.domain.matricula;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface MatriculaRepository extends JpaRepository<Matricula, UUID> {

    Optional<Matricula> findFirstByAlunoIdAndTurmaIdAndStatusIn(
            UUID alunoId,
            UUID turmaId,
            Collection<StatusMatricula> statuses);

    @Query("""
            SELECT m FROM Matricula m
            WHERE (:alunoId IS NULL OR m.aluno.id = :alunoId)
              AND (:turmaId IS NULL OR m.turma.id = :turmaId)
              AND (:status IS NULL OR m.status = :status)
            """)
    Page<Matricula> filtrar(@Param("alunoId") UUID alunoId,
                            @Param("turmaId") UUID turmaId,
                            @Param("status") StatusMatricula status,
                            Pageable pageable);
}
