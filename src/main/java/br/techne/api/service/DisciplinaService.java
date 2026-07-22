package br.techne.api.service;

import br.techne.api.domain.curso.Curso;
import br.techne.api.domain.curso.CursoRepository;
import br.techne.api.domain.disciplina.Disciplina;
import br.techne.api.domain.disciplina.DisciplinaRepository;
import br.techne.api.domain.disciplina.dto.CreateDisciplinaRequest;
import br.techne.api.domain.disciplina.dto.DisciplinaResponse;
import br.techne.api.domain.disciplina.dto.UpdateDisciplinaRequest;
import br.techne.api.infra.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional(readOnly = true)
    public Page<DisciplinaResponse> listar(String nome, Pageable pageable) {
        Page<Disciplina> page;
        if (nome != null && !nome.isBlank()) {
            page = disciplinaRepository.findByNomeContainingIgnoreCase(nome.trim(), pageable);
        } else {
            page = disciplinaRepository.findAll(pageable);
        }
        return page.map(DisciplinaResponse::new);
    }

    @Transactional(readOnly = true)
    public DisciplinaResponse buscarPorId(UUID id) {
        return new DisciplinaResponse(buscarDisciplinaOuFalhar(id));
    }

    @Transactional
    public DisciplinaResponse criar(CreateDisciplinaRequest request) {
        String nome = request.nome().trim();
        validarNomeUnico(nome);

        Curso curso = buscarCursoOuFalhar(request.cursoId());
        Disciplina disciplina = new Disciplina(
                nome,
                normalizarDescricao(request.descricao()),
                request.cargaHoraria(),
                request.creditos(),
                request.semestreRecomendado(),
                request.ementa().trim(),
                curso
        );
        return new DisciplinaResponse(disciplinaRepository.save(disciplina));
    }

    @Transactional
    public DisciplinaResponse editar(UUID id, UpdateDisciplinaRequest request) {
        Disciplina disciplina = buscarDisciplinaOuFalhar(id);
        String nome = request.nome().trim();

        if (!disciplina.getNome().equalsIgnoreCase(nome)) {
            validarNomeUnico(nome);
        }

        Curso curso = buscarCursoOuFalhar(request.cursoId());
        disciplina.atualizar(
                nome,
                normalizarDescricao(request.descricao()),
                request.cargaHoraria(),
                request.creditos(),
                request.semestreRecomendado(),
                request.ementa().trim(),
                curso
        );
        return new DisciplinaResponse(disciplinaRepository.saveAndFlush(disciplina));
    }

    @Transactional
    public void excluir(UUID id) {
        Disciplina disciplina = buscarDisciplinaOuFalhar(id);
        disciplinaRepository.delete(disciplina);
    }

    private Disciplina buscarDisciplinaOuFalhar(UUID id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina nao encontrada."));
    }

    private Curso buscarCursoOuFalhar(UUID id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso nao encontrado."));
    }

    private void validarNomeUnico(String nome) {
        if (disciplinaRepository.existsByNomeIgnoreCase(nome)) {
            throw new IllegalArgumentException("Nome já cadastrado.");
        }
    }

    private String normalizarDescricao(String descricao) {
        if (descricao == null) {
            return null;
        }
        String trimmed = descricao.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
