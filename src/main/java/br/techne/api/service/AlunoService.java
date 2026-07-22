package br.techne.api.service;

import br.techne.api.domain.aluno.Aluno;
import br.techne.api.domain.aluno.AlunoRepository;
import br.techne.api.domain.aluno.dto.AlunoResponse;
import br.techne.api.domain.aluno.dto.CreateAlunoRequest;
import br.techne.api.domain.aluno.dto.UpdateAlunoRequest;
import br.techne.api.infra.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Transactional(readOnly = true)
    public Page<AlunoResponse> listar(String nome, Pageable pageable) {
        Page<Aluno> page;
        if (nome != null && !nome.isBlank()) {
            page = alunoRepository.findByNomeContainingIgnoreCase(nome.trim(), pageable);
        } else {
            page = alunoRepository.findAll(pageable);
        }
        return page.map(AlunoResponse::new);
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscarPorId(UUID id) {
        return new AlunoResponse(buscarAlunoOuFalhar(id));
    }

    @Transactional
    public AlunoResponse criar(CreateAlunoRequest request) {
        String cpf = normalizarCpf(request.cpf());
        String email = normalizarEmail(request.email());

        validarCpfUnico(cpf);
        validarEmailUnico(email);

        Aluno aluno = new Aluno(
                request.nome().trim(),
                email,
                cpf,
                request.dataNascimento()
        );

        return new AlunoResponse(alunoRepository.save(aluno));
    }

    @Transactional
    public AlunoResponse editar(UUID id, UpdateAlunoRequest request) {
        Aluno aluno = buscarAlunoOuFalhar(id);

        String cpf = normalizarCpf(request.cpf());
        String email = normalizarEmail(request.email());

        if (!aluno.getCpf().equals(cpf)) {
            validarCpfUnico(cpf);
        }
        if (!aluno.getEmail().equals(email)) {
            validarEmailUnico(email);
        }

        aluno.atualizar(
                request.nome().trim(),
                email,
                cpf,
                request.dataNascimento()
        );

        return new AlunoResponse(alunoRepository.saveAndFlush(aluno));
    }

    @Transactional
    public void excluir(UUID id) {
        Aluno aluno = buscarAlunoOuFalhar(id);
        alunoRepository.delete(aluno);
    }

    private Aluno buscarAlunoOuFalhar(UUID id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado."));
    }

    private void validarCpfUnico(String cpf) {
        if (alunoRepository.existsByCpf(cpf)) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
    }

    private void validarEmailUnico(String email) {
        if (alunoRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
    }

    private String normalizarCpf(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase();
    }
}
