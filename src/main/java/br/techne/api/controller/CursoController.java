package br.techne.api.controller;

import br.techne.api.domain.curso.dto.CreateCursoRequest;
import br.techne.api.domain.curso.dto.CursoResponse;
import br.techne.api.domain.curso.dto.UpdateCursoRequest;
import br.techne.api.service.CursoService;
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
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<Page<CursoResponse>> listar(
            @RequestParam(required = false) String nome,
            @PageableDefault(sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(cursoService.listar(nome, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(cursoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CursoResponse> criar(@RequestBody @Valid CreateCursoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponse> editar(@PathVariable UUID id,
                                                @RequestBody @Valid UpdateCursoRequest request) {
        return ResponseEntity.ok(cursoService.editar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        cursoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
