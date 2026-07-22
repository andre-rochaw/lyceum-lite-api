package br.techne.api.domain.disciplina.dto;

import br.techne.api.domain.disciplina.Disciplina;

import java.time.LocalDateTime;
import java.util.UUID;

public record DisciplinaResponse(
        UUID id,
        String nome,
        String descricao,
        Integer cargaHoraria,
        Integer creditos,
        Integer semestreRecomendado,
        String ementa,
        UUID cursoId,
        String cursoNome,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public DisciplinaResponse(Disciplina disciplina) {
        this(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getDescricao(),
                disciplina.getCargaHoraria(),
                disciplina.getCreditos(),
                disciplina.getSemestreRecomendado(),
                disciplina.getEmenta(),
                disciplina.getCurso().getId(),
                disciplina.getCurso().getNome(),
                disciplina.getCreatedAt(),
                disciplina.getUpdatedAt()
        );
    }
}
