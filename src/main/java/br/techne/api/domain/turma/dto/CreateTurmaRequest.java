package br.techne.api.domain.turma.dto;

import br.techne.api.domain.turma.StatusTurma;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateTurmaRequest(
        @NotBlank(message = "Nome e obrigatorio.")
        @Size(min = 3, message = "Nome deve ter no minimo 3 caracteres.")
        String nome,

        @NotNull(message = "Disciplina e obrigatoria.")
        UUID disciplinaId,

        @NotNull(message = "Ano e obrigatorio.")
        @Positive(message = "Ano deve ser maior que zero.")
        Integer ano,

        @NotNull(message = "Semestre e obrigatorio.")
        @Positive(message = "Semestre deve ser maior que zero.")
        Integer semestre,

        @NotNull(message = "Limite de vagas e obrigatorio.")
        @Positive(message = "Limite de vagas deve ser maior que zero.")
        Integer limiteVagas,

        @NotNull(message = "Status e obrigatorio.")
        StatusTurma status
) {
}
