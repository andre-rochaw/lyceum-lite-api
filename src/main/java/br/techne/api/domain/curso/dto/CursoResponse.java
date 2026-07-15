package br.techne.api.domain.curso.dto;

import br.techne.api.domain.curso.Curso;

import java.time.LocalDateTime;
import java.util.UUID;

public record CursoResponse(
        UUID id,
        String nome,
        String descricao,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public CursoResponse(Curso curso) {
        this(
                curso.getId(),
                curso.getNome(),
                curso.getDescricao(),
                curso.getCreatedAt(),
                curso.getUpdatedAt()
        );
    }
}
