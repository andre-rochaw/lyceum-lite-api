package br.techne.api.service;

import br.techne.api.domain.disciplina.Disciplina;
import br.techne.api.domain.disciplina.DisciplinaRepository;
import br.techne.api.domain.turma.StatusTurma;
import br.techne.api.domain.turma.Turma;
import br.techne.api.domain.turma.TurmaRepository;
import br.techne.api.domain.turma.dto.CreateTurmaRequest;
import br.techne.api.domain.turma.dto.TurmaResponse;
import br.techne.api.domain.turma.dto.UpdateTurmaRequest;
import br.techne.api.infra.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Transactional(readOnly = true)
    public Page<TurmaResponse> listar(String nome,
                                      UUID disciplinaId,
                                      UUID cursoId,
                                      Integer ano,
                                      Integer semestre,
                                      StatusTurma status,
                                      Pageable pageable) {
        String nomeFiltro = (nome != null && !nome.isBlank()) ? nome.trim() : null;
        String statusFiltro = status != null ? status.name() : null;

        return turmaRepository.filtrar(
                nomeFiltro,
                disciplinaId,
                cursoId,
                ano,
                semestre,
                statusFiltro,
                pageable
        ).map(TurmaResponse::new);
    }

    @Transactional(readOnly = true)
    public TurmaResponse buscarPorId(UUID id) {
        return new TurmaResponse(buscarTurmaOuFalhar(id));
    }

    @Transactional
    public TurmaResponse criar(CreateTurmaRequest request) {
        Disciplina disciplina = buscarDisciplinaOuFalhar(request.disciplinaId());
        Turma turma = new Turma(
                request.nome().trim(),
                disciplina,
                request.ano(),
                request.semestre(),
                request.limiteVagas(),
                request.status()
        );
        return new TurmaResponse(turmaRepository.save(turma));
    }

    @Transactional
    public TurmaResponse editar(UUID id, UpdateTurmaRequest request) {
        Turma turma = buscarTurmaOuFalhar(id);
        Disciplina disciplina = buscarDisciplinaOuFalhar(request.disciplinaId());
        turma.atualizar(
                request.nome().trim(),
                disciplina,
                request.ano(),
                request.semestre(),
                request.limiteVagas(),
                request.status()
        );
        return new TurmaResponse(turmaRepository.saveAndFlush(turma));
    }

    @Transactional
    public void excluir(UUID id) {
        Turma turma = buscarTurmaOuFalhar(id);
        turmaRepository.delete(turma);
    }

    private Turma buscarTurmaOuFalhar(UUID id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turma nao encontrada."));
    }

    private Disciplina buscarDisciplinaOuFalhar(UUID id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina nao encontrada."));
    }
}
