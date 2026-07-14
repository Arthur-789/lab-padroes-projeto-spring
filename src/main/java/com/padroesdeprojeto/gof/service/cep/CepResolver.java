package com.padroesdeprojeto.gof.service.cep;

import com.padroesdeprojeto.gof.model.Endereco;

public interface CepResolver {

	boolean suporta(String cep);

	Endereco resolver(String cep);

	int ordem();
}
