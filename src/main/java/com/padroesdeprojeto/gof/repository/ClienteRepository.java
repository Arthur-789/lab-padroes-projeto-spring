package com.padroesdeprojeto.gof.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.padroesdeprojeto.gof.model.Cliente;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
}
