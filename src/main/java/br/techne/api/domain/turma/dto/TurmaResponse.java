package br.techne.api.domain.turma.dto;

import br.techne.api.domain.turma.StatusTurma;
import br.techne.api.domain.turma.Turma;

import java.time.LocalDateTime;
import java.util.UUID;

public record TurmaResponse(
        UUID id,
        String nome,
        UUID disciplinaId,
        String disciplinaNome,
        UUID cursoId,
        String cursoNome,
        Integer ano,
        Integer semestre,
        Integer limiteVagas,
        Integer vagasOcupadas,
        StatusTurma status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public TurmaResponse(Turma turma) {
        this(
                turma.getId(),
                turma.getNome(),
                turma.getDisciplina().getId(),
                turma.getDisciplina().getNome(),
                turma.getDisciplina().getCurso().getId(),
                turma.getDisciplina().getCurso().getNome(),
                turma.getAno(),
                turma.getSemestre(),
                turma.getLimiteVagas(),
                turma.getVagasOcupadas(),
                turma.getStatus(),
                turma.getCreatedAt(),
                turma.getUpdatedAt()
        );
    }
}
