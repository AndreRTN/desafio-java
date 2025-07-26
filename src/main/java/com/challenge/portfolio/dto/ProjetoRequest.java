package com.challenge.portfolio.dto;

import com.challenge.portfolio.types.StatusProjeto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoRequest {
    
    private String nome;
    private LocalDate dataInicio;
    private Long gerenteId;
    private LocalDate dataPrevisaoFim;
    private LocalDate dataFim;
    private Float orcamento;
    private String descricao;
    private StatusProjeto status;
}