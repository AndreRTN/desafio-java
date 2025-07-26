package com.challenge.portfolio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projeto_membro", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"idprojeto", "idpessoa"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoMembro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idprojeto", nullable = false)
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "idpessoa", nullable = false)
    private Pessoa pessoa;

    @Column(name = "atribuicao", nullable = false, length = 100)
    private String atribuicao;
}