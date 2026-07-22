package br.techne.api.domain.turma;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TurmaRepository extends JpaRepository<Turma, UUID> {

    @Query(
            value = """
                    SELECT t.* FROM turmas t
                    INNER JOIN disciplinas d ON d.id = t.disciplina_id
                    WHERE (:nome IS NULL OR t.nome COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :nome, '%'))
                      AND (:disciplinaId IS NULL OR t.disciplina_id = :disciplinaId)
                      AND (:cursoId IS NULL OR d.curso_id = :cursoId)
                      AND (:ano IS NULL OR t.ano = :ano)
                      AND (:semestre IS NULL OR t.semestre = :semestre)
                      AND (:status IS NULL OR t.status = :status)
                    """,
            countQuery = """
                    SELECT COUNT(*) FROM turmas t
                    INNER JOIN disciplinas d ON d.id = t.disciplina_id
                    WHERE (:nome IS NULL OR t.nome COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :nome, '%'))
                      AND (:disciplinaId IS NULL OR t.disciplina_id = :disciplinaId)
                      AND (:cursoId IS NULL OR d.curso_id = :cursoId)
                      AND (:ano IS NULL OR t.ano = :ano)
                      AND (:semestre IS NULL OR t.semestre = :semestre)
                      AND (:status IS NULL OR t.status = :status)
                    """,
            nativeQuery = true
    )
    Page<Turma> filtrar(@Param("nome") String nome,
                        @Param("disciplinaId") UUID disciplinaId,
                        @Param("cursoId") UUID cursoId,
                        @Param("ano") Integer ano,
                        @Param("semestre") Integer semestre,
                        @Param("status") String status,
                        Pageable pageable);
}
