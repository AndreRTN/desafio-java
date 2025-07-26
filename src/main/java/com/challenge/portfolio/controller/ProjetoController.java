package com.challenge.portfolio.controller;

import com.challenge.portfolio.dto.ProjetoRequest;
import com.challenge.portfolio.model.Pessoa;
import com.challenge.portfolio.model.Projeto;
import com.challenge.portfolio.repository.PessoaRepository;
import com.challenge.portfolio.service.ProjetoServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoServico projetoServico;

    @Autowired
    private PessoaRepository pessoaRepository;

    @PostMapping
    public ResponseEntity<?> criarProjeto(@RequestBody ProjetoRequest projetoRequest) {
        Projeto projeto = converterParaProjeto(projetoRequest);
        if (projeto == null) {
            return ResponseEntity.badRequest().body("Gerente não encontrado");
        }
        Projeto projetoSalvo = projetoServico.salvarProjeto(projeto);
        return ResponseEntity.status(HttpStatus.CREATED).body(projetoSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Projeto>> listarTodosProjetos() {
        List<Projeto> projetos = projetoServico.listarTodosProjetos();
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projeto> buscarProjetoPorId(@PathVariable Long id) {
        Optional<Projeto> projeto = projetoServico.buscarProjetoPorId(id);
        return projeto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProjeto(@PathVariable Long id, @RequestBody ProjetoRequest projetoRequest) {
        Projeto projeto = converterParaProjeto(projetoRequest);
        if (projeto == null) {
            return ResponseEntity.

                    badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Gerente não encontrado");
        }
        Projeto projetoAtualizado = projetoServico.atualizarProjeto(id, projeto);
        if (projetoAtualizado != null) {
            return ResponseEntity.ok(projetoAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarProjeto(@PathVariable Long id) {
        try {
            projetoServico.deletarProjeto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}/pode-ser-excluido")
    public ResponseEntity<Boolean> verificarSePodeSerExcluido(@PathVariable Long id) {
        boolean podeSerExcluido = projetoServico.podeSerExcluido(id);
        return ResponseEntity.ok(podeSerExcluido);
    }

    private Projeto converterParaProjeto(ProjetoRequest projetoRequest) {
        if (projetoRequest.getGerenteId() == null) {
            return null;
        }

        Optional<Pessoa> gerenteOptional = pessoaRepository.findById(projetoRequest.getGerenteId());
        if (gerenteOptional.isEmpty()) {
            return null;
        }

        Projeto projeto = new Projeto();
        projeto.setNome(projetoRequest.getNome());
        projeto.setDataInicio(projetoRequest.getDataInicio());
        projeto.setDataPrevisaoFim(projetoRequest.getDataPrevisaoFim());
        projeto.setDataFim(projetoRequest.getDataFim());
        projeto.setDescricao(projetoRequest.getDescricao());
        projeto.setStatus(projetoRequest.getStatus());
        projeto.setOrcamento(projetoRequest.getOrcamento());
        projeto.setGerente(gerenteOptional.get());

        return projeto;
    }
}