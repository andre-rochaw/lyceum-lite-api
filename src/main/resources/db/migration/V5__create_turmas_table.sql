CREATE TABLE turmas (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    disciplina_id UNIQUEIDENTIFIER NOT NULL,
    ano INT NOT NULL,
    semestre INT NOT NULL,
    limite_vagas INT NOT NULL,
    vagas_ocupadas INT NOT NULL CONSTRAINT df_turmas_vagas_ocupadas DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME2 NOT NULL,
    updated_at DATETIME2 NOT NULL,
    CONSTRAINT fk_turmas_disciplina FOREIGN KEY (disciplina_id) REFERENCES disciplinas(id)
);
