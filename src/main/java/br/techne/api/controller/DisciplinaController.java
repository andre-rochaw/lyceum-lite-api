package br.techne.api.controller;

import br.techne.api.domain.disciplina.dto.CreateDisciplinaRequest;
import br.techne.api.domain.disciplina.dto.DisciplinaResponse;
import br.techne.api.domain.disciplina.dto.UpdateDisciplinaRequest;
import br.techne.api.service.DisciplinaService;
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
@RequestMapping("/disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @GetMapping
    public ResponseEntity<Page<DisciplinaResponse>> listar(
            @RequestParam(required = false) String nome,
            @PageableDefault(sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(disciplinaService.listar(nome, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(disciplinaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponse> criar(@RequestBody @Valid CreateDisciplinaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaResponse> editar(@PathVariable UUID id,
                                                     @RequestBody @Valid UpdateDisciplinaRequest request) {
        return ResponseEntity.ok(disciplinaService.editar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        disciplinaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
