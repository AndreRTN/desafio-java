package com.challenge.portfolio.service;

import com.challenge.portfolio.exception.ProjetoNaoPodeSerExcluidoException;
import com.challenge.portfolio.model.Projeto;
import com.challenge.portfolio.repository.ProjetoRepository;
import com.challenge.portfolio.types.RiscoProjeto;
import com.challenge.portfolio.types.StatusProjeto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjetoServico {

    @Autowired
    private ProjetoRepository projetoRepository;

    private static final Set<StatusProjeto> STATUS_NAO_PODEM_SER_EXCLUIDOS = Set.of(
            StatusProjeto.INICIADO,
            StatusProjeto.EM_ANDAMENTO,
            StatusProjeto.ENCERRADO
    );

    private static final float LIMITE_BAIXO_RISCO = 50000.0f;
    private static final float LIMITE_MEDIO_RISCO = 200000.0f;

    public Projeto salvarProjeto(Projeto projeto) {

        projeto.setRisco(calcularRisco(projeto));
        return projetoRepository.save(projeto);
    }

    public Optional<Projeto> buscarProjetoPorId(Long id) {
        return projetoRepository.findById(id);
    }

    public List<Projeto> listarTodosProjetos() {
        List<Projeto> projetos = (List<Projeto>) projetoRepository.findAll();
        // Sort projects by ID to maintain consistent order
        projetos.sort(Comparator.comparing(Projeto::getId));
        return projetos;
    }

    public void deletarProjeto(Long id) {
        Optional<Projeto> projetoOptional = projetoRepository.findById(id);

        if (projetoOptional.isEmpty()) {
            throw new RuntimeException("Projeto não encontrado com ID: " + id);
        }

        Projeto projeto = projetoOptional.get();

        if (podeSerExcluido(projeto)) {
            projetoRepository.deleteById(id);
        } else {
            throw new ProjetoNaoPodeSerExcluidoException(
                    String.format("Projeto com status '%s' não pode ser excluído. " +
                                    "Apenas projetos com status 'Em Análise', 'Análise Realizada', " +
                                    "'Análise Aprovada', 'Planejado' ou 'Cancelado' podem ser excluídos.",
                            projeto.getStatus().getDescricao())
            );
        }
    }

    public Projeto atualizarProjeto(Long id, Projeto projetoAtualizado) {
        return projetoRepository.findById(id)
                .map(projetoExistente -> {
                    RiscoProjeto risco = calcularRisco(projetoExistente);
                    BeanUtils.copyProperties(projetoAtualizado, projetoExistente, "id");
                    projetoExistente.setRisco(risco);
                    return projetoRepository.save(projetoExistente);
                }).orElse(null);
    }

    private RiscoProjeto calcularRisco(Projeto projeto) {
        if (projeto.getOrcamento() == null || projeto.getOrcamento() == 0) {
            return RiscoProjeto.BAIXO_RISCO;
        }

        float orcamento = projeto.getOrcamento();
        return orcamento <= LIMITE_BAIXO_RISCO ? RiscoProjeto.BAIXO_RISCO :
                orcamento <= LIMITE_MEDIO_RISCO ? RiscoProjeto.MEDIO_RISCO :
                        RiscoProjeto.ALTO_RISCO;
    }

    private boolean podeSerExcluido(Projeto projeto) {
        return projeto.getStatus() == null ||
                !STATUS_NAO_PODEM_SER_EXCLUIDOS.contains(projeto.getStatus());
    }

    public boolean podeSerExcluido(Long id) {
        return projetoRepository.findById(id)
                .map(this::podeSerExcluido)
                .orElse(false);
    }
}
