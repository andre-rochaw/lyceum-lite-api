package br.techne.api.domain.matricula.dto;

import br.techne.api.domain.matricula.Matricula;
import br.techne.api.domain.matricula.StatusMatricula;

import java.time.LocalDateTime;
import java.util.UUID;

public record MatriculaResponse(
        UUID id,
        UUID alunoId,
        String alunoNome,
        UUID turmaId,
        String turmaNome,
        UUID disciplinaId,
        String disciplinaNome,
        UUID cursoId,
        String cursoNome,
        StatusMatricula status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public MatriculaResponse(Matricula matricula) {
        this(
                matricula.getId(),
                matricula.getAluno().getId(),
                matricula.getAluno().getNome(),
                matricula.getTurma().getId(),
                matricula.getTurma().getNome(),
                matricula.getTurma().getDisciplina().getId(),
                matricula.getTurma().getDisciplina().getNome(),
                matricula.getTurma().getDisciplina().getCurso().getId(),
                matricula.getTurma().getDisciplina().getCurso().getNome(),
                matricula.getStatus(),
                matricula.getCreatedAt(),
                matricula.getUpdatedAt()
        );
    }
}
