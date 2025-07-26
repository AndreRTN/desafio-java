package com.challenge.portfolio.service;

import com.challenge.portfolio.model.Pessoa;
import com.challenge.portfolio.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaServico {

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Pessoa> listarTodosFuncionarios() {
        return pessoaRepository.findByFuncionarioTrue();
    }
    public List<Pessoa> listarTodosGerentes() {
        return pessoaRepository.findByGerenteTrue();
    }

    public Pessoa criarFuncionario(String nome) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setFuncionario(true);
        pessoa.setGerente(false);
        return pessoaRepository.save(pessoa);
    }

}
