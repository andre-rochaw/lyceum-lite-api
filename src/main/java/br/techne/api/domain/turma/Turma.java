package br.techne.api.domain.turma;

import br.techne.api.domain.disciplina.Disciplina;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "turmas")
@Entity(name = "Turma")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "semestre", nullable = false)
    private Integer semestre;

    @Column(name = "limite_vagas", nullable = false)
    private Integer limiteVagas;

    @Column(name = "vagas_ocupadas", nullable = false)
    private Integer vagasOcupadas;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusTurma status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Turma(String nome,
                 Disciplina disciplina,
                 Integer ano,
                 Integer semestre,
                 Integer limiteVagas,
                 StatusTurma status) {
        this.nome = nome;
        this.disciplina = disciplina;
        this.ano = ano;
        this.semestre = semestre;
        this.limiteVagas = limiteVagas;
        this.vagasOcupadas = 0;
        this.status = status;
    }

    public void atualizar(String nome,
                          Disciplina disciplina,
                          Integer ano,
                          Integer semestre,
                          Integer limiteVagas,
                          StatusTurma status) {
        this.nome = nome;
        this.disciplina = disciplina;
        this.ano = ano;
        this.semestre = semestre;
        this.limiteVagas = limiteVagas;
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void consumirVaga() {
        this.vagasOcupadas = this.vagasOcupadas + 1;
        this.updatedAt = LocalDateTime.now();
    }

    public void liberarVaga() {
        if (this.vagasOcupadas > 0) {
            this.vagasOcupadas = this.vagasOcupadas - 1;
        }
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (vagasOcupadas == null) {
            vagasOcupadas = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
