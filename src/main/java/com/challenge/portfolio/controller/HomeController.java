package com.challenge.portfolio.controller;

import com.challenge.portfolio.model.Pessoa;
import com.challenge.portfolio.model.Projeto;
import com.challenge.portfolio.repository.PessoaRepository;
import com.challenge.portfolio.service.ProjetoServico;
import com.challenge.portfolio.types.StatusProjeto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    
    @Autowired
    private ProjetoServico projetoServico;
    
    @Autowired
    private PessoaRepository pessoaRepository;
    
    @RequestMapping("")
    public String home(Model model){
        List<Projeto> projetos = projetoServico.listarTodosProjetos();
        List<Pessoa> gerentes = (List<Pessoa>) pessoaRepository.findAll();

        model.addAttribute("projetos", projetos);
        model.addAttribute("gerentes", gerentes);
        model.addAttribute("statusOptions", StatusProjeto.values());
        model.addAttribute("novoProjeto", new Projeto());

        return "home";
    }
    
    @PostMapping("/projeto")
    public String salvarProjeto(@ModelAttribute Projeto projeto) {
        projetoServico.salvarProjeto(projeto);
        return "redirect:/home";
    }
    
    @PostMapping("/projeto/deletar/{id}")
    public String deletarProjeto(@PathVariable Long id, Model model) {
        try {
            projetoServico.deletarProjeto(id);
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return home(model);
        }
        return "redirect:/home";
    }
}