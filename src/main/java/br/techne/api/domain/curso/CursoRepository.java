package br.techne.api.domain.curso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CursoRepository extends JpaRepository<Curso, UUID> {

    boolean existsByNomeIgnoreCase(String nome);

    @Query(
            value = """
                    SELECT * FROM cursos c
                    WHERE c.nome COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :nome, '%')
                    """,
            countQuery = """
                    SELECT COUNT(*) FROM cursos c
                    WHERE c.nome COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :nome, '%')
                    """,
            nativeQuery = true
    )
    Page<Curso> findByNomeContainingIgnoreCaseAndAccent(@Param("nome") String nome, Pageable pageable);
}
