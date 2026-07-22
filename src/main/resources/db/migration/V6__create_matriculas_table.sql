CREATE TABLE matriculas (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    aluno_id UNIQUEIDENTIFIER NOT NULL,
    turma_id UNIQUEIDENTIFIER NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME2 NOT NULL,
    updated_at DATETIME2 NOT NULL,
    CONSTRAINT fk_matriculas_aluno FOREIGN KEY (aluno_id) REFERENCES alunos(id),
    CONSTRAINT fk_matriculas_turma FOREIGN KEY (turma_id) REFERENCES turmas(id),
    CONSTRAINT uq_matriculas_aluno_turma UNIQUE (aluno_id, turma_id)
);
