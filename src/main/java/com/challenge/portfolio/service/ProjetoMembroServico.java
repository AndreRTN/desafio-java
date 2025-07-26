package com.challenge.portfolio.service;

import com.challenge.portfolio.model.Pessoa;
import com.challenge.portfolio.model.Projeto;
import com.challenge.portfolio.model.ProjetoMembro;
import com.challenge.portfolio.repository.PessoaRepository;
import com.challenge.portfolio.repository.ProjetoRepository;
import com.challenge.portfolio.repository.ProjetoMembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetoMembroServico {

    @Autowired
    private ProjetoMembroRepository projetoMembroRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public ProjetoMembro adicionarMembro(Long projetoId, Long pessoaId, String atribuicao) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        if (!Boolean.TRUE.equals(pessoa.getFuncionario())) {
            throw new RuntimeException("Apenas funcionários podem ser associados como membros do projeto");
        }

        if (projetoMembroRepository.existsByProjetoIdAndPessoaId(projetoId, pessoaId)) {
            throw new RuntimeException("Pessoa já é membro deste projeto");
        }

        ProjetoMembro projetoMembro = new ProjetoMembro();
        projetoMembro.setProjeto(projeto);
        projetoMembro.setPessoa(pessoa);
        projetoMembro.setAtribuicao(atribuicao);

        return projetoMembroRepository.save(projetoMembro);
    }

    public List<ProjetoMembro> listarMembrosPorProjeto(Long projetoId) {
        return projetoMembroRepository.findByProjetoId(projetoId);
    }

    public void removerMembro(Long projetoId, Long pessoaId) {
        ProjetoMembro projetoMembro = projetoMembroRepository.findByProjetoIdAndPessoaId(projetoId, pessoaId)
                .orElseThrow(() -> new RuntimeException("Associação não encontrada"));

        projetoMembroRepository.delete(projetoMembro);
    }
}