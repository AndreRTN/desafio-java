package com.challenge.portfolio.model;

import com.challenge.portfolio.types.RiscoProjeto;
import com.challenge.portfolio.types.StatusProjeto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "projeto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_previsao_fim")
    private LocalDate dataPrevisaoFim;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "descricao", length = 5000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 45)
    private StatusProjeto status;

    @Column(name = "orcamento")
    private Float orcamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "risco", length = 45)
    private RiscoProjeto risco;

    @ManyToOne
    @JoinColumn(name = "idgerente", nullable = false)
    private Pessoa gerente;
}