package com.challenge.portfolio.types;

public enum RiscoProjeto {
    BAIXO_RISCO("Baixo Risco"),
    MEDIO_RISCO("Médio Risco"),
    ALTO_RISCO("Alto Risco");

    private final String descricao;

    RiscoProjeto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
