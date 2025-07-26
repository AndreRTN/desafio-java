package com.challenge.portfolio.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembroRequest {
    private Long pessoaId;
    private String atribuicao;

}