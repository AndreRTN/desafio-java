package com.challenge.portfolio.controller;

import com.challenge.portfolio.dto.ProjetoRequest;
import com.challenge.portfolio.model.Pessoa;
import com.challenge.portfolio.model.Projeto;
import com.challenge.portfolio.repository.PessoaRepository;
import com.challenge.portfolio.service.ProjetoServico;
import com.challenge.portfolio.types.StatusProjeto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProjetoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProjetoServico projetoServico;

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private ProjetoController projetoController;

    private ObjectMapper objectMapper;
    private Projeto projeto;
    private Pessoa gerente;
    private ProjetoRequest projetoRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(projetoController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // For LocalDate serialization

        // Create a manager
        gerente = new Pessoa();
        gerente.setId(1L);
        gerente.setNome("Gerente Teste");
        gerente.setGerente(true);

        // Create a project
        projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("Projeto Teste");
        projeto.setDataInicio(LocalDate.now());
        projeto.setDataPrevisaoFim(LocalDate.now().plusMonths(3));
        projeto.setDescricao("Descrição do projeto teste");
        projeto.setStatus(StatusProjeto.EM_ANALISE);
        projeto.setOrcamento(40000.0f);
        projeto.setGerente(gerente);

        // Create a project request
        projetoRequest = new ProjetoRequest();
        projetoRequest.setNome("Projeto Teste");
        projetoRequest.setDataInicio(LocalDate.now());
        projetoRequest.setDataPrevisaoFim(LocalDate.now().plusMonths(3));
        projetoRequest.setDescricao("Descrição do projeto teste");
        projetoRequest.setStatus(StatusProjeto.EM_ANALISE);
        projetoRequest.setOrcamento(40000.0f);
        projetoRequest.setGerenteId(1L);
    }

    @Test
    void criarProjeto_DeveRetornarProjetoCriado() throws Exception {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(gerente));
        when(projetoServico.salvarProjeto(any(Projeto.class))).thenReturn(projeto);

        mockMvc.perform(post("/projetos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projetoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Projeto Teste"));

        verify(projetoServico, times(1)).salvarProjeto(any(Projeto.class));
    }

    @Test
    void criarProjeto_DeveRetornarBadRequestQuandoGerenteNaoEncontrado() throws Exception {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/projetos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projetoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Gerente não encontrado"));

        verify(projetoServico, never()).salvarProjeto(any(Projeto.class));
    }

    @Test
    void listarTodosProjetos_DeveRetornarListaDeProjetos() throws Exception {
        List<Projeto> projetos = Arrays.asList(projeto);
        when(projetoServico.listarTodosProjetos()).thenReturn(projetos);

        mockMvc.perform(get("/projetos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Projeto Teste"));

        verify(projetoServico, times(1)).listarTodosProjetos();
    }

    @Test
    void buscarProjetoPorId_DeveRetornarProjeto() throws Exception {
        when(projetoServico.buscarProjetoPorId(1L)).thenReturn(Optional.of(projeto));

        mockMvc.perform(get("/projetos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Projeto Teste"));

        verify(projetoServico, times(1)).buscarProjetoPorId(1L);
    }

    @Test
    void buscarProjetoPorId_DeveRetornarNotFoundQuandoProjetoNaoExiste() throws Exception {
        when(projetoServico.buscarProjetoPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/projetos/1"))
                .andExpect(status().isNotFound());

        verify(projetoServico, times(1)).buscarProjetoPorId(1L);
    }

    @Test
    void atualizarProjeto_DeveRetornarProjetoAtualizado() throws Exception {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(gerente));
        when(projetoServico.atualizarProjeto(eq(1L), any(Projeto.class))).thenReturn(projeto);

        mockMvc.perform(put("/projetos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projetoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Projeto Teste"));

        verify(projetoServico, times(1)).atualizarProjeto(eq(1L), any(Projeto.class));
    }

    @Test
    void atualizarProjeto_DeveRetornarBadRequestQuandoGerenteNaoEncontrado() throws Exception {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/projetos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projetoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Gerente não encontrado"));

        verify(projetoServico, never()).atualizarProjeto(anyLong(), any(Projeto.class));
    }

    @Test
    void atualizarProjeto_DeveRetornarNotFoundQuandoProjetoNaoExiste() throws Exception {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(gerente));
        when(projetoServico.atualizarProjeto(eq(1L), any(Projeto.class))).thenReturn(null);

        mockMvc.perform(put("/projetos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projetoRequest)))
                .andExpect(status().isNotFound());

        verify(projetoServico, times(1)).atualizarProjeto(eq(1L), any(Projeto.class));
    }

    @Test
    void deletarProjeto_DeveRetornarNoContent() throws Exception {
        doNothing().when(projetoServico).deletarProjeto(1L);

        mockMvc.perform(delete("/projetos/1"))
                .andExpect(status().isNoContent());

        verify(projetoServico, times(1)).deletarProjeto(1L);
    }

    @Test
    void deletarProjeto_DeveRetornarBadRequestQuandoProjetoNaoPodeSerExcluido() throws Exception {
        doThrow(new RuntimeException("Projeto não pode ser excluído")).when(projetoServico).deletarProjeto(1L);

        mockMvc.perform(delete("/projetos/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Projeto não pode ser excluído"));

        verify(projetoServico, times(1)).deletarProjeto(1L);
    }

    @Test
    void verificarSePodeSerExcluido_DeveRetornarTrue() throws Exception {
        when(projetoServico.podeSerExcluido(1L)).thenReturn(true);

        mockMvc.perform(get("/projetos/1/pode-ser-excluido"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(projetoServico, times(1)).podeSerExcluido(1L);
    }

    @Test
    void verificarSePodeSerExcluido_DeveRetornarFalse() throws Exception {
        when(projetoServico.podeSerExcluido(1L)).thenReturn(false);

        mockMvc.perform(get("/projetos/1/pode-ser-excluido"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(projetoServico, times(1)).podeSerExcluido(1L);
    }
}