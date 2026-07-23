package br.techne.api.controller;

import br.techne.api.domain.turma.StatusTurma;
import br.techne.api.domain.turma.dto.CreateTurmaRequest;
import br.techne.api.domain.turma.dto.TurmaResponse;
import br.techne.api.domain.turma.dto.UpdateTurmaRequest;
import br.techne.api.service.TurmaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @GetMapping
    public ResponseEntity<Page<TurmaResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) UUID disciplinaId,
            @RequestParam(required = false) UUID cursoId,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Integer semestre,
            @RequestParam(required = false) StatusTurma status,
            @PageableDefault(sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(turmaService.listar(
                nome, disciplinaId, cursoId, ano, semestre, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(turmaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TurmaResponse> criar(@RequestBody @Valid CreateTurmaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(turmaService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurmaResponse> editar(@PathVariable UUID id,
                                                @RequestBody @Valid UpdateTurmaRequest request) {
        return ResponseEntity.ok(turmaService.editar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        turmaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
