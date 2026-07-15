package br.techne.api.domain.curso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCursoRequest(
        @NotBlank(message = "Nome é obrigatório.")
        @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres.")
        String nome,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres.")
        String descricao
) {
}
