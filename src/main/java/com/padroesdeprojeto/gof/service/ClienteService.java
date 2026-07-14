package com.padroesdeprojeto.gof.service;

import com.padroesdeprojeto.gof.dto.ClienteRequestDTO;
import com.padroesdeprojeto.gof.model.Cliente;

public interface ClienteService {

	Iterable<Cliente> buscarTodos();

	Cliente buscarPorId(Long id);

	Cliente inserir(ClienteRequestDTO dto);

	Cliente atualizar(Long id, ClienteRequestDTO dto);

	void deletar(Long id);
}
