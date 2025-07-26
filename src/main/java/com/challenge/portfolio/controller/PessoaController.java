package com.challenge.portfolio.controller;

import com.challenge.portfolio.dto.FuncionarioRequest;
import com.challenge.portfolio.model.Pessoa;
import com.challenge.portfolio.service.PessoaServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    @Autowired
    private PessoaServico pessoaServico;

    @GetMapping("/gerentes")
    public ResponseEntity<List<Pessoa>> listarGerentes() {
        List<Pessoa> gerentes = pessoaServico.listarTodosGerentes();
        return ResponseEntity.ok(gerentes);
    }

    @GetMapping("/funcionarios")
    public ResponseEntity<List<Pessoa>> listarFuncionarios() {
        List<Pessoa> funcionarios = pessoaServico.listarTodosFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }

    @PostMapping("/funcionarios")
    public ResponseEntity<?> criarFuncionario(@RequestBody FuncionarioRequest request) {
        try {
            String nome = request.getNome();
            if (nome == null || nome.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nome é obrigatório");
            }

            Pessoa funcionario = pessoaServico.criarFuncionario(nome);
            return ResponseEntity.status(HttpStatus.CREATED).body(funcionario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
