-- Ambientes que ja aplicaram V6 com UNIQUE absoluto (aluno_id, turma_id):
-- remove a constraint e passa a impedir apenas matriculas ativas.
IF EXISTS (
    SELECT 1
    FROM sys.key_constraints
    WHERE name = 'uq_matriculas_aluno_turma'
      AND parent_object_id = OBJECT_ID('matriculas')
)
BEGIN
    ALTER TABLE matriculas DROP CONSTRAINT uq_matriculas_aluno_turma;
END;

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'uq_matriculas_aluno_turma_ativas'
      AND object_id = OBJECT_ID('matriculas')
)
BEGIN
    CREATE UNIQUE INDEX uq_matriculas_aluno_turma_ativas
    ON matriculas(aluno_id, turma_id)
    WHERE status IN ('PENDENTE', 'CONFIRMADA');
END;
