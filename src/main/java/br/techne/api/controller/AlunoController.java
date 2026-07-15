package br.techne.api.controller;

import br.techne.api.domain.aluno.dto.AlunoResponse;
import br.techne.api.domain.aluno.dto.CreateAlunoRequest;
import br.techne.api.domain.aluno.dto.UpdateAlunoRequest;
import br.techne.api.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public ResponseEntity<Page<AlunoResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(alunoService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(alunoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> criar(@RequestBody @Valid CreateAlunoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponse> editar(@PathVariable UUID id,
                                                @RequestBody @Valid UpdateAlunoRequest request) {
        return ResponseEntity.ok(alunoService.editar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        alunoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
