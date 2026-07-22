package br.techne.api.controller;

import br.techne.api.domain.matricula.StatusMatricula;
import br.techne.api.domain.matricula.dto.CreateMatriculaRequest;
import br.techne.api.domain.matricula.dto.MatriculaResponse;
import br.techne.api.service.MatriculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @GetMapping
    public ResponseEntity<Page<MatriculaResponse>> listar(
            @RequestParam(required = false) UUID alunoId,
            @RequestParam(required = false) UUID turmaId,
            @RequestParam(required = false) StatusMatricula status,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(matriculaService.listar(alunoId, turmaId, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(matriculaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MatriculaResponse> criar(@RequestBody @Valid CreateMatriculaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.criar(request));
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<MatriculaResponse> confirmar(@PathVariable UUID id) {
        return ResponseEntity.ok(matriculaService.confirmar(id));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<MatriculaResponse> cancelar(@PathVariable UUID id) {
        return ResponseEntity.ok(matriculaService.cancelar(id));
    }
}
