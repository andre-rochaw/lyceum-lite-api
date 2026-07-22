package br.techne.api.service;

import br.techne.api.domain.aluno.Aluno;
import br.techne.api.domain.aluno.AlunoRepository;
import br.techne.api.domain.matricula.Matricula;
import br.techne.api.domain.matricula.MatriculaRepository;
import br.techne.api.domain.matricula.StatusMatricula;
import br.techne.api.domain.matricula.dto.CreateMatriculaRequest;
import br.techne.api.domain.matricula.dto.MatriculaResponse;
import br.techne.api.domain.turma.StatusTurma;
import br.techne.api.domain.turma.Turma;
import br.techne.api.domain.turma.TurmaRepository;
import br.techne.api.infra.exceptions.ConflictException;
import br.techne.api.infra.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Transactional(readOnly = true)
    public Page<MatriculaResponse> listar(UUID alunoId,
                                          UUID turmaId,
                                          StatusMatricula status,
                                          Pageable pageable) {
        return matriculaRepository.filtrar(alunoId, turmaId, status, pageable)
                .map(MatriculaResponse::new);
    }

    @Transactional(readOnly = true)
    public MatriculaResponse buscarPorId(UUID id) {
        return new MatriculaResponse(buscarMatriculaOuFalhar(id));
    }

    @Transactional
    public MatriculaResponse criar(CreateMatriculaRequest request) {
        Aluno aluno = buscarAlunoOuFalhar(request.alunoId());
        Turma turma = buscarTurmaOuFalhar(request.turmaId());

        if (turma.getStatus() != StatusTurma.ABERTA) {
            throw new ConflictException("Matricula permitida apenas em turma ABERTA.");
        }

        if (matriculaRepository.existsByAlunoIdAndTurmaId(aluno.getId(), turma.getId())) {
            throw new ConflictException("Aluno ja possui matricula nesta turma.");
        }

        Matricula matricula = new Matricula(aluno, turma);
        return new MatriculaResponse(matriculaRepository.save(matricula));
    }

    @Transactional
    public MatriculaResponse confirmar(UUID id) {
        Matricula matricula = buscarMatriculaOuFalhar(id);

        if (matricula.getStatus() != StatusMatricula.PENDENTE) {
            throw new ConflictException("Somente matricula PENDENTE pode ser confirmada.");
        }

        Turma turma = matricula.getTurma();
        if (turma.getVagasOcupadas() >= turma.getLimiteVagas()) {
            throw new ConflictException("Turma sem vagas disponiveis.");
        }

        matricula.confirmar();
        turma.consumirVaga();

        matriculaRepository.save(matricula);
        turmaRepository.save(turma);

        return new MatriculaResponse(matricula);
    }

    @Transactional
    public MatriculaResponse cancelar(UUID id) {
        Matricula matricula = buscarMatriculaOuFalhar(id);
        StatusMatricula statusAtual = matricula.getStatus();

        if (statusAtual == StatusMatricula.CANCELADA) {
            throw new ConflictException("Matricula ja esta CANCELADA.");
        }

        if (statusAtual == StatusMatricula.CONFIRMADA) {
            Turma turma = matricula.getTurma();
            matricula.cancelar();
            turma.liberarVaga();
            matriculaRepository.save(matricula);
            turmaRepository.save(turma);
            return new MatriculaResponse(matricula);
        }

        // PENDENTE: cancela sem alterar vagas
        matricula.cancelar();
        return new MatriculaResponse(matriculaRepository.save(matricula));
    }

    private Matricula buscarMatriculaOuFalhar(UUID id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matricula nao encontrada."));
    }

    private Aluno buscarAlunoOuFalhar(UUID id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno nao encontrado."));
    }

    private Turma buscarTurmaOuFalhar(UUID id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turma nao encontrada."));
    }
}
