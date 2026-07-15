package br.techne.api.domain.aluno.dto;

import br.techne.api.domain.aluno.Aluno;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AlunoResponse(
        UUID id,
        String nome,
        String email,
        String cpf,
        LocalDate dataNascimento,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public AlunoResponse(Aluno aluno) {
        this(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getCpf(),
                aluno.getDataNascimento(),
                aluno.getCreatedAt(),
                aluno.getUpdatedAt()
        );
    }
}
