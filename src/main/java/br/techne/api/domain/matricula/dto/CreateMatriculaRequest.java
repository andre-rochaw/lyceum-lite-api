package br.techne.api.domain.matricula.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateMatriculaRequest(
        @NotNull(message = "Aluno e obrigatorio.")
        UUID alunoId,

        @NotNull(message = "Turma e obrigatoria.")
        UUID turmaId
) {
}
