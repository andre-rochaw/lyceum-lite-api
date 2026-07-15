package br.techne.api.domain.aluno.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateAlunoRequest(
        @NotBlank(message = "Nome é obrigatório.")
        @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres.")
        String nome,

        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "E-mail deve ter formato válido.")
        String email,

        @NotBlank(message = "CPF é obrigatório.")
        @Pattern(
                regexp = "^(\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$",
                message = "CPF deve ter exatamente 11 números ou a máscara ###.###.###-##."
        )
        String cpf,

        @NotNull(message = "Data de nascimento é obrigatória.")
        LocalDate dataNascimento
) {
}
