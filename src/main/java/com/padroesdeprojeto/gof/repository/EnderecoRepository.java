package com.padroesdeprojeto.gof.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.padroesdeprojeto.gof.model.Endereco;

@Repository
public interface EnderecoRepository extends CrudRepository<Endereco, String> {
}
