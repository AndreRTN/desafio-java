package com.challenge.portfolio.controller;

import com.challenge.portfolio.model.Pessoa;
import com.challenge.portfolio.service.PessoaServico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
public class PessoaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PessoaServico pessoaServico;

    @InjectMocks
    private PessoaController pessoaController;

    private Pessoa funcionario;
    private Pessoa gerente;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pessoaController).build();

        // Create a funcionario
        funcionario = new Pessoa();
        funcionario.setId(1L);
        funcionario.setNome("Funcionário Teste");
        funcionario.setDataNascimento(LocalDate.of(1990, 1, 1));
        funcionario.setCpf("111.111.111-11");
        funcionario.setFuncionario(true);
        funcionario.setGerente(false);

        // Create a gerente
        gerente = new Pessoa();
        gerente.setId(2L);
        gerente.setNome("Gerente Teste");
        gerente.setDataNascimento(LocalDate.of(1985, 5, 5));
        gerente.setCpf("222.222.222-22");
        gerente.setFuncionario(false);
        gerente.setGerente(true);
    }

    @Test
    void listarGerentes_DeveRetornarListaDeGerentes() throws Exception {
        List<Pessoa> gerentes = Arrays.asList(gerente);
        when(pessoaServico.listarTodosGerentes()).thenReturn(gerentes);

        mockMvc.perform(get("/api/pessoas/gerentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].nome").value("Gerente Teste"))
                .andExpect(jsonPath("$[0].gerente").value(true));

        verify(pessoaServico, times(1)).listarTodosGerentes();
    }

    @Test
    void listarGerentes_DeveRetornarListaVaziaQuandoNaoHaGerentes() throws Exception {
        when(pessoaServico.listarTodosGerentes()).thenReturn(List.of());

        mockMvc.perform(get("/api/pessoas/gerentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(pessoaServico, times(1)).listarTodosGerentes();
    }

    @Test
    void listarFuncionarios_DeveRetornarListaDeFuncionarios() throws Exception {
        List<Pessoa> funcionarios = Arrays.asList(funcionario);
        when(pessoaServico.listarTodosFuncionarios()).thenReturn(funcionarios);

        mockMvc.perform(get("/api/pessoas/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Funcionário Teste"))
                .andExpect(jsonPath("$[0].funcionario").value(true));

        verify(pessoaServico, times(1)).listarTodosFuncionarios();
    }

    @Test
    void listarFuncionarios_DeveRetornarListaVaziaQuandoNaoHaFuncionarios() throws Exception {
        when(pessoaServico.listarTodosFuncionarios()).thenReturn(List.of());

        mockMvc.perform(get("/api/pessoas/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(pessoaServico, times(1)).listarTodosFuncionarios();
    }

    @Test
    void criarFuncionario_DeveRetornarFuncionarioCriado() throws Exception {
        Pessoa novoFuncionario = new Pessoa();
        novoFuncionario.setId(3L);
        novoFuncionario.setNome("Novo Funcionário");
        novoFuncionario.setFuncionario(true);
        novoFuncionario.setGerente(false);

        when(pessoaServico.criarFuncionario("Novo Funcionário")).thenReturn(novoFuncionario);

        mockMvc.perform(post("/api/pessoas/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\": \"Novo Funcionário\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nome").value("Novo Funcionário"))
                .andExpect(jsonPath("$.funcionario").value(true))
                .andExpect(jsonPath("$.gerente").value(false));

        verify(pessoaServico, times(1)).criarFuncionario("Novo Funcionário");
    }

    @Test
    void criarFuncionario_DeveRetornarBadRequestQuandoNomeVazio() throws Exception {
        mockMvc.perform(post("/api/pessoas/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome é obrigatório"));

        verify(pessoaServico, never()).criarFuncionario(anyString());
    }
}
