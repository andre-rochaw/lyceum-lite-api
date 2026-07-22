package br.techne.api.domain.disciplina.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateDisciplinaRequest(
        @NotBlank(message = "Nome e obrigatorio.")
        @Size(min = 3, message = "Nome deve ter no minimo 3 caracteres.")
        String nome,

        @Size(max = 500, message = "Descricao deve ter no maximo 500 caracteres.")
        String descricao,

        @NotNull(message = "Carga horaria e obrigatoria.")
        @Positive(message = "Carga horaria deve ser maior que zero.")
        Integer cargaHoraria,

        @NotNull(message = "Creditos sao obrigatorios.")
        @Positive(message = "Creditos devem ser maiores que zero.")
        Integer creditos,

        @NotNull(message = "Semestre recomendado e obrigatorio.")
        @Positive(message = "Semestre recomendado deve ser maior que zero.")
        Integer semestreRecomendado,

        @NotBlank(message = "Ementa e obrigatoria.")
        @Size(max = 2000, message = "Ementa deve ter no maximo 2000 caracteres.")
        String ementa,

        @NotNull(message = "Curso e obrigatorio.")
        UUID cursoId
) {
}
