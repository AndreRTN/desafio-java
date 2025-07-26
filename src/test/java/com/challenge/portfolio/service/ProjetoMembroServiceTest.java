package com.challenge.portfolio.service;

import com.challenge.portfolio.model.Pessoa;
import com.challenge.portfolio.model.Projeto;
import com.challenge.portfolio.model.ProjetoMembro;
import com.challenge.portfolio.repository.PessoaRepository;
import com.challenge.portfolio.repository.ProjetoRepository;
import com.challenge.portfolio.repository.ProjetoMembroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetoMembroServicoTest {

    @Mock
    private ProjetoMembroRepository projetoMembroRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private ProjetoMembroServico projetoMembroServico;

    private Projeto projeto;
    private Pessoa funcionario;
    private Pessoa naoFuncionario;
    private ProjetoMembro projetoMembro;

    @BeforeEach
    void setUp() {
        projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("Projeto Teste");

        funcionario = new Pessoa();
        funcionario.setId(1L);
        funcionario.setNome("João");
        funcionario.setFuncionario(true);

        naoFuncionario = new Pessoa();
        naoFuncionario.setId(2L);
        naoFuncionario.setNome("Maria");
        naoFuncionario.setFuncionario(false);

        projetoMembro = new ProjetoMembro();
        projetoMembro.setId(1L);
        projetoMembro.setProjeto(projeto);
        projetoMembro.setPessoa(funcionario);
        projetoMembro.setAtribuicao("Desenvolvedor");
    }

    @Test
    void adicionarMembro_ComDadosValidos_DeveRetornarProjetoMembroSalvo() {
        // Arrange
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(projetoMembroRepository.existsByProjetoIdAndPessoaId(1L, 1L)).thenReturn(false);
        when(projetoMembroRepository.save(any(ProjetoMembro.class))).thenReturn(projetoMembro);

        // Act
        ProjetoMembro resultado = projetoMembroServico.adicionarMembro(1L, 1L, "Desenvolvedor");

        // Assert
        assertNotNull(resultado);
        assertEquals(projeto, resultado.getProjeto());
        assertEquals(funcionario, resultado.getPessoa());
        assertEquals("Desenvolvedor", resultado.getAtribuicao());
        verify(projetoMembroRepository).save(any(ProjetoMembro.class));
    }

    @Test
    void adicionarMembro_QuandoProjetoNaoEncontrado_DeveLancarRuntimeException() {
        // Arrange
        when(projetoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> projetoMembroServico.adicionarMembro(1L, 1L, "Desenvolvedor")
        );

        assertEquals("Projeto não encontrado", exception.getMessage());
        verify(projetoMembroRepository, never()).save(any(ProjetoMembro.class));
    }

    @Test
    void adicionarMembro_QuandoPessoaNaoEncontrada_DeveLancarRuntimeException() {
        // Arrange
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> projetoMembroServico.adicionarMembro(1L, 1L, "Desenvolvedor")
        );

        assertEquals("Pessoa não encontrada", exception.getMessage());
        verify(projetoMembroRepository, never()).save(any(ProjetoMembro.class));
    }

    @Test
    void adicionarMembro_QuandoPessoaNaoEhFuncionario_DeveLancarRuntimeException() {
        // Arrange
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(pessoaRepository.findById(2L)).thenReturn(Optional.of(naoFuncionario));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> projetoMembroServico.adicionarMembro(1L, 2L, "Desenvolvedor")
        );

        assertEquals("Apenas funcionários podem ser associados como membros do projeto", exception.getMessage());
        verify(projetoMembroRepository, never()).save(any(ProjetoMembro.class));
    }

    @Test
    void adicionarMembro_QuandoPessoaJaEhMembro_DeveLancarRuntimeException() {
        // Arrange
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(projetoMembroRepository.existsByProjetoIdAndPessoaId(1L, 1L)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> projetoMembroServico.adicionarMembro(1L, 1L, "Desenvolvedor")
        );

        assertEquals("Pessoa já é membro deste projeto", exception.getMessage());
        verify(projetoMembroRepository, never()).save(any(ProjetoMembro.class));
    }

    @Test
    void listarMembrosPorProjeto_DeveRetornarListaDeMembros() {
        // Arrange
        List<ProjetoMembro> membros = Arrays.asList(projetoMembro);
        when(projetoMembroRepository.findByProjetoId(1L)).thenReturn(membros);

        // Act
        List<ProjetoMembro> resultado = projetoMembroServico.listarMembrosPorProjeto(1L);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(projetoMembro, resultado.get(0));
        verify(projetoMembroRepository).findByProjetoId(1L);
    }

    @Test
    void removerMembro_ComAssociacaoExistente_DeveRemoverMembro() {
        // Arrange
        when(projetoMembroRepository.findByProjetoIdAndPessoaId(1L, 1L))
                .thenReturn(Optional.of(projetoMembro));

        // Act
        projetoMembroServico.removerMembro(1L, 1L);

        // Assert
        verify(projetoMembroRepository).delete(projetoMembro);
    }

    @Test
    void removerMembro_QuandoAssociacaoNaoEncontrada_DeveLancarRuntimeException() {
        // Arrange
        when(projetoMembroRepository.findByProjetoIdAndPessoaId(1L, 1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> projetoMembroServico.removerMembro(1L, 1L)
        );

        assertEquals("Associação não encontrada", exception.getMessage());
        verify(projetoMembroRepository, never()).delete(any(ProjetoMembro.class));
    }
}