package com.challenge.portfolio.repository;

import com.challenge.portfolio.model.Pessoa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends CrudRepository<Pessoa, Long> {

    List<Pessoa> findByFuncionarioTrue();
    List<Pessoa> findByGerenteTrue();

}