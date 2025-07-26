package com.challenge.portfolio.controller;

import com.challenge.portfolio.dto.MembroRequest;
import com.challenge.portfolio.model.ProjetoMembro;
import com.challenge.portfolio.service.ProjetoMembroServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos/{projetoId}/membros")
public class ProjetoMembroController {

    @Autowired
    private ProjetoMembroServico projetoMembroServico;

    @PostMapping
    public ResponseEntity<?> adicionarMembro(
            @PathVariable Long projetoId,
            @RequestBody MembroRequest request) {
        try {
            if (request.getPessoaId() == null) {
                return ResponseEntity.badRequest().body("pessoaId é obrigatório");
            }
            if (request.getAtribuicao() == null) {
                return ResponseEntity.badRequest().body("atribuicao é obrigatória");
            }

            Long pessoaId = request.getPessoaId();
            String atribuicao = request.getAtribuicao();

            ProjetoMembro projetoMembro = projetoMembroServico.adicionarMembro(projetoId, pessoaId, atribuicao);
            return ResponseEntity.status(HttpStatus.CREATED).body(projetoMembro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjetoMembro>> listarMembros(@PathVariable Long projetoId) {
        List<ProjetoMembro> membros = projetoMembroServico.listarMembrosPorProjeto(projetoId);
        return ResponseEntity.ok(membros);
    }

    @DeleteMapping("/{pessoaId}")
    public ResponseEntity<?> removerMembro(
            @PathVariable Long projetoId,
            @PathVariable Long pessoaId) {
        try {
            projetoMembroServico.removerMembro(projetoId, pessoaId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }
}
