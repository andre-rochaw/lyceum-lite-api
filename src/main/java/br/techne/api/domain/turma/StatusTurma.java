package br.techne.api.domain.turma;

public enum StatusTurma {
  ABERTA("Aberta"),
  CONCLUIDA("Concluída"),
  EM_AVALIACAO("Em avaliação");

  private final String descricao;

  StatusTurma(String descricao) {
      this.descricao = descricao;
  }

  public String getDescricao() {
      return descricao;
  }
}
