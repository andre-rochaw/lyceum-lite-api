package br.techne.api.service;

import br.techne.api.domain.curso.Curso;
import br.techne.api.domain.curso.CursoRepository;
import br.techne.api.domain.curso.dto.CreateCursoRequest;
import br.techne.api.domain.curso.dto.CursoResponse;
import br.techne.api.domain.curso.dto.UpdateCursoRequest;
import br.techne.api.infra.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional(readOnly = true)
    public Page<CursoResponse> listar(Pageable pageable) {
        return cursoRepository.findAll(pageable)
                .map(CursoResponse::new);
    }

    @Transactional(readOnly = true)
    public CursoResponse buscarPorId(UUID id) {
        return new CursoResponse(buscarCursoOuFalhar(id));
    }

    @Transactional
    public CursoResponse criar(CreateCursoRequest request) {
        Curso curso = new Curso(
                request.nome().trim(),
                normalizarDescricao(request.descricao())
        );
        return new CursoResponse(cursoRepository.save(curso));
    }

    @Transactional
    public CursoResponse editar(UUID id, UpdateCursoRequest request) {
        Curso curso = buscarCursoOuFalhar(id);
        curso.atualizar(
                request.nome().trim(),
                normalizarDescricao(request.descricao())
        );
        return new CursoResponse(cursoRepository.saveAndFlush(curso));
    }

    @Transactional
    public void excluir(UUID id) {
        Curso curso = buscarCursoOuFalhar(id);
        cursoRepository.delete(curso);
    }

    private Curso buscarCursoOuFalhar(UUID id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado."));
    }

    private String normalizarDescricao(String descricao) {
        if (descricao == null) {
            return null;
        }
        String trimmed = descricao.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
