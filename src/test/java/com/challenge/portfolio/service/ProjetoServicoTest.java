package com.challenge.portfolio.service;

import com.challenge.portfolio.exception.ProjetoNaoPodeSerExcluidoException;
import com.challenge.portfolio.model.Projeto;
import com.challenge.portfolio.repository.ProjetoRepository;
import com.challenge.portfolio.types.RiscoProjeto;
import com.challenge.portfolio.types.StatusProjeto;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetoServicoTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private ProjetoServico projetoServico;

    private Projeto projeto;

    @BeforeEach
    void setUp() {
        projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("Projeto Teste");
        projeto.setOrcamento(100000.0f);
        projeto.setStatus(StatusProjeto.EM_ANALISE);
    }

    @Test
    void salvarProjeto_DeveCalcularRiscoERetornarProjetoSalvo() {
        // Arrange
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        // Act
        Projeto resultado = projetoServico.salvarProjeto(projeto);

        // Assert
        assertNotNull(resultado);
        assertEquals(RiscoProjeto.MEDIO_RISCO, resultado.getRisco());
        verify(projetoRepository).save(projeto);
    }

    @Test
    void salvarProjeto_ComOrcamentoBaixo_DeveCalcularBaixoRisco() {
        // Arrange
        projeto.setOrcamento(30000.0f);
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        // Act
        Projeto resultado = projetoServico.salvarProjeto(projeto);

        // Assert
        assertEquals(RiscoProjeto.BAIXO_RISCO, resultado.getRisco());
    }

    @Test
    void salvarProjeto_ComOrcamentoAlto_DeveCalcularAltoRisco() {
        // Arrange
        projeto.setOrcamento(300000.0f);
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        // Act
        Projeto resultado = projetoServico.salvarProjeto(projeto);

        // Assert
        assertEquals(RiscoProjeto.ALTO_RISCO, resultado.getRisco());
    }

    @Test
    void salvarProjeto_ComOrcamentoNulo_DeveCalcularBaixoRisco() {
        // Arrange
        projeto.setOrcamento(null);
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        // Act
        Projeto resultado = projetoServico.salvarProjeto(projeto);

        // Assert
        assertEquals(RiscoProjeto.BAIXO_RISCO, resultado.getRisco());
    }

    @Test
    void buscarProjetoPorId_DeveRetornarOptionalComProjeto() {
        // Arrange
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        // Act
        Optional<Projeto> resultado = projetoServico.buscarProjetoPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(projeto, resultado.get());
        verify(projetoRepository).findById(1L);
    }

    @Test
    void buscarProjetoPorId_QuandoNaoEncontrado_DeveRetornarOptionalVazio() {
        // Arrange
        when(projetoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Projeto> resultado = projetoServico.buscarProjetoPorId(1L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(projetoRepository).findById(1L);
    }

    @Test
    void listarTodosProjetos_DeveRetornarListaOrdenadaPorId() {
        // Arrange
        Projeto projeto2 = new Projeto();
        projeto2.setId(2L);
        
        List<Projeto> projetos = Arrays.asList(projeto2, projeto);
        when(projetoRepository.findAll()).thenReturn(projetos);

        // Act
        List<Projeto> resultado = projetoServico.listarTodosProjetos();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
        verify(projetoRepository).findAll();
    }

    @Test
    void deletarProjeto_ComStatusPermitido_DeveDeletarProjeto() {
        // Arrange
        projeto.setStatus(StatusProjeto.EM_ANALISE);
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        // Act
        projetoServico.deletarProjeto(1L);

        // Assert
        verify(projetoRepository).deleteById(1L);
    }

    @Test
    void deletarProjeto_ComStatusNaoPermitido_DeveLancarExcecao() {
        // Arrange
        projeto.setStatus(StatusProjeto.INICIADO);
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        // Act & Assert
        ProjetoNaoPodeSerExcluidoException exception = assertThrows(
                ProjetoNaoPodeSerExcluidoException.class,
                () -> projetoServico.deletarProjeto(1L)
        );

        assertTrue(exception.getMessage().contains("não pode ser excluído"));
        verify(projetoRepository, never()).deleteById(anyLong());
    }

    @Test
    void deletarProjeto_QuandoProjetoNaoEncontrado_DeveLancarRuntimeException() {
        // Arrange
        when(projetoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> projetoServico.deletarProjeto(1L)
        );

        assertEquals("Projeto não encontrado com ID: 1", exception.getMessage());
        verify(projetoRepository, never()).deleteById(anyLong());
    }

    @Test
    void atualizarProjeto_QuandoProjetoExiste_DeveAtualizarERetornar() {
        // Arrange
        Projeto projetoAtualizado = new Projeto();
        projetoAtualizado.setNome("Projeto Atualizado");
        projetoAtualizado.setOrcamento(150000.0f);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        // Act
        Projeto resultado = projetoServico.atualizarProjeto(1L, projetoAtualizado);

        // Assert
        assertNotNull(resultado);
        verify(projetoRepository).findById(1L);
        verify(projetoRepository).save(any(Projeto.class));
    }

    @Test
    void atualizarProjeto_QuandoProjetoNaoExiste_DeveRetornarNull() {
        // Arrange
        when(projetoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Projeto resultado = projetoServico.atualizarProjeto(1L, new Projeto());

        // Assert
        assertNull(resultado);
        verify(projetoRepository).findById(1L);
        verify(projetoRepository, never()).save(any(Projeto.class));
    }

    @Test
    void podeSerExcluido_ComStatusPermitido_DeveRetornarTrue() {
        // Arrange
        projeto.setStatus(StatusProjeto.EM_ANALISE);
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        // Act
        boolean resultado = projetoServico.podeSerExcluido(1L);

        // Assert
        assertTrue(resultado);
        verify(projetoRepository).findById(1L);
    }

    @Test
    void podeSerExcluido_ComStatusNaoPermitido_DeveRetornarFalse() {
        // Arrange
        projeto.setStatus(StatusProjeto.INICIADO);
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        // Act
        boolean resultado = projetoServico.podeSerExcluido(1L);

        // Assert
        assertFalse(resultado);
        verify(projetoRepository).findById(1L);
    }

    @Test
    void podeSerExcluido_QuandoProjetoNaoEncontrado_DeveRetornarFalse() {
        // Arrange
        when(projetoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean resultado = projetoServico.podeSerExcluido(1L);

        // Assert
        assertFalse(resultado);
        verify(projetoRepository).findById(1L);
    }
}