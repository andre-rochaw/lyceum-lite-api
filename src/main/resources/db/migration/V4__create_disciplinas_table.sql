CREATE TABLE disciplinas (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    descricao VARCHAR(500) NULL,
    carga_horaria INT NOT NULL,
    creditos INT NOT NULL,
    semestre_recomendado INT NOT NULL,
    ementa VARCHAR(2000) NOT NULL,
    curso_id UNIQUEIDENTIFIER NOT NULL,
    created_at DATETIME2 NOT NULL,
    updated_at DATETIME2 NOT NULL,
    CONSTRAINT fk_disciplinas_curso FOREIGN KEY (curso_id) REFERENCES cursos(id)
);
