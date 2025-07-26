package com.challenge.portfolio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pessoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "datanascimento")
    private LocalDate dataNascimento;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "funcionario")
    private Boolean funcionario;

    @Column(name = "gerente")
    private Boolean gerente;
}