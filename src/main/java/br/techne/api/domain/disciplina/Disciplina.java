package br.techne.api.domain.disciplina;

import br.techne.api.domain.curso.Curso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Table(name = "disciplinas")
@Entity(name = "Disciplina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria;

    @Column(name = "creditos", nullable = false)
    private Integer creditos;

    @Column(name = "semestre_recomendado", nullable = false)
    private Integer semestreRecomendado;

    @Column(name = "ementa", nullable = false, length = 2000)
    private String ementa;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Disciplina(String nome,
                      String descricao,
                      Integer cargaHoraria,
                      Integer creditos,
                      Integer semestreRecomendado,
                      String ementa,
                      Curso curso) {
        this.nome = nome;
        this.descricao = descricao;
        this.cargaHoraria = cargaHoraria;
        this.creditos = creditos;
        this.semestreRecomendado = semestreRecomendado;
        this.ementa = ementa;
        this.curso = curso;
    }

    public void atualizar(String nome,
                          String descricao,
                          Integer cargaHoraria,
                          Integer creditos,
                          Integer semestreRecomendado,
                          String ementa,
                          Curso curso) {
        this.nome = nome;
        this.descricao = descricao;
        this.cargaHoraria = cargaHoraria;
        this.creditos = creditos;
        this.semestreRecomendado = semestreRecomendado;
        this.ementa = ementa;
        this.curso = curso;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
