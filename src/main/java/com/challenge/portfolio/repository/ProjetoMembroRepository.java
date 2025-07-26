package com.challenge.portfolio.repository;

import com.challenge.portfolio.model.ProjetoMembro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetoMembroRepository extends CrudRepository<ProjetoMembro, Long> {

    List<ProjetoMembro> findByProjetoId(Long projetoId);

    Optional<ProjetoMembro> findByProjetoIdAndPessoaId(Long projetoId, Long pessoaId);

    boolean existsByProjetoIdAndPessoaId(Long projetoId, Long pessoaId);
}