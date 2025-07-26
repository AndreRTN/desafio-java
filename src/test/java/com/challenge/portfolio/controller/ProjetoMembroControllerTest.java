package com.challenge.portfolio.controller;

import com.challenge.portfolio.model.Pessoa;
import com.challenge.portfolio.model.Projeto;
import com.challenge.portfolio.model.ProjetoMembro;
import com.challenge.portfolio.service.ProjetoMembroServico;
import com.challenge.portfolio.types.StatusProjeto;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProjetoMembroControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProjetoMembroServico projetoMembroServico;

    @InjectMocks
    private ProjetoMembroController projetoMembroController;

    private Projeto projeto;
    private Pessoa funcionario;
    private ProjetoMembro projetoMembro;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(projetoMembroController).build();

        // Create a manager
        Pessoa gerente = new Pessoa();
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

        // Create a funcionario
        funcionario = new Pessoa();
        funcionario.setId(2L);
        funcionario.setNome("Funcionário Teste");
        funcionario.setFuncionario(true);

        // Create a projeto membro
        projetoMembro = new ProjetoMembro();
        projetoMembro.setId(1L);
        projetoMembro.setProjeto(projeto);
        projetoMembro.setPessoa(funcionario);
        projetoMembro.setAtribuicao("Desenvolvedor");
    }

    @Test
    void adicionarMembro_DeveRetornarMembroAdicionado() throws Exception {
        when(projetoMembroServico.adicionarMembro(1L, 2L, "Desenvolvedor")).thenReturn(projetoMembro);

        mockMvc.perform(post("/projetos/1/membros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pessoaId\": 2, \"atribuicao\": \"Desenvolvedor\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.atribuicao").value("Desenvolvedor"));

        verify(projetoMembroServico, times(1)).adicionarMembro(1L, 2L, "Desenvolvedor");
    }

    @Test
    void adicionarMembro_DeveRetornarBadRequestQuandoOcorreErro() throws Exception {
        when(projetoMembroServico.adicionarMembro(anyLong(), anyLong(), anyString()))
                .thenThrow(new RuntimeException("Pessoa não encontrada"));

        mockMvc.perform(post("/projetos/1/membros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pessoaId\": 2, \"atribuicao\": \"Desenvolvedor\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Pessoa não encontrada"));

        verify(projetoMembroServico, times(1)).adicionarMembro(1L, 2L, "Desenvolvedor");
    }

    @Test
    void listarMembros_DeveRetornarListaDeMembros() throws Exception {
        List<ProjetoMembro> membros = Arrays.asList(projetoMembro);
        when(projetoMembroServico.listarMembrosPorProjeto(1L)).thenReturn(membros);

        mockMvc.perform(get("/projetos/1/membros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].atribuicao").value("Desenvolvedor"));

        verify(projetoMembroServico, times(1)).listarMembrosPorProjeto(1L);
    }

    @Test
    void listarMembros_DeveRetornarListaVaziaQuandoNaoHaMembros() throws Exception {
        when(projetoMembroServico.listarMembrosPorProjeto(1L)).thenReturn(List.of());

        mockMvc.perform(get("/projetos/1/membros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(projetoMembroServico, times(1)).listarMembrosPorProjeto(1L);
    }

    @Test
    void removerMembro_DeveRetornarNoContent() throws Exception {
        doNothing().when(projetoMembroServico).removerMembro(1L, 2L);

        mockMvc.perform(delete("/projetos/1/membros/2"))
                .andExpect(status().isNoContent());

        verify(projetoMembroServico, times(1)).removerMembro(1L, 2L);
    }

    @Test
    void removerMembro_DeveRetornarBadRequestQuandoOcorreErro() throws Exception {
        doThrow(new RuntimeException("Membro não encontrado")).when(projetoMembroServico).removerMembro(1L, 2L);

        mockMvc.perform(delete("/projetos/1/membros/2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Membro não encontrado"));

        verify(projetoMembroServico, times(1)).removerMembro(1L, 2L);
    }
}
