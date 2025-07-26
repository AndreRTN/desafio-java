package com.challenge.portfolio.service;

import com.challenge.portfolio.model.Pessoa;
import com.challenge.portfolio.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServicoTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaServico pessoaServico;

    private Pessoa funcionario;
    private Pessoa gerente;

    @BeforeEach
    void setUp() {
        funcionario = new Pessoa();
        funcionario.setId(1L);
        funcionario.setNome("João");
        funcionario.setFuncionario(true);
        funcionario.setGerente(false);

        gerente = new Pessoa();
        gerente.setId(2L);
        gerente.setNome("Maria");
        gerente.setFuncionario(true);
        gerente.setGerente(true);
    }

    @Test
    void listarTodosFuncionarios_DeveRetornarListaDeFuncionarios() {
        // Arrange
        List<Pessoa> funcionarios = Arrays.asList(funcionario, gerente);
        when(pessoaRepository.findByFuncionarioTrue()).thenReturn(funcionarios);

        // Act
        List<Pessoa> resultado = pessoaServico.listarTodosFuncionarios();

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(funcionario));
        assertTrue(resultado.contains(gerente));
        verify(pessoaRepository).findByFuncionarioTrue();
    }

    @Test
    void listarTodosGerentes_DeveRetornarListaDeGerentes() {
        // Arrange
        List<Pessoa> gerentes = Arrays.asList(gerente);
        when(pessoaRepository.findByGerenteTrue()).thenReturn(gerentes);

        // Act
        List<Pessoa> resultado = pessoaServico.listarTodosGerentes();

        // Assert
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(gerente));
        verify(pessoaRepository).findByGerenteTrue();
    }

    @Test
    void criarFuncionario_DeveRetornarFuncionarioCriado() {
        // Arrange
        String nome = "Carlos";
        Pessoa funcionarioSalvo = new Pessoa();
        funcionarioSalvo.setId(3L);
        funcionarioSalvo.setNome(nome);
        funcionarioSalvo.setFuncionario(true);
        funcionarioSalvo.setGerente(false);

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(funcionarioSalvo);

        // Act
        Pessoa resultado = pessoaServico.criarFuncionario(nome);

        // Assert
        assertNotNull(resultado);
        assertEquals(nome, resultado.getNome());
        assertTrue(resultado.getFuncionario());
        assertFalse(resultado.getGerente());
        verify(pessoaRepository).save(any(Pessoa.class));
    }
}