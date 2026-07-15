CREATE TABLE alunos (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    data_nascimento DATE NOT NULL,
    created_at DATETIME2 NOT NULL,
    updated_at DATETIME2 NOT NULL,
    CONSTRAINT uq_alunos_cpf UNIQUE (cpf),
    CONSTRAINT uq_alunos_email UNIQUE (email)
);
