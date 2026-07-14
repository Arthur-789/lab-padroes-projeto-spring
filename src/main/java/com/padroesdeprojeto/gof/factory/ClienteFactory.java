package com.padroesdeprojeto.gof.factory;

import org.springframework.stereotype.Component;

import com.padroesdeprojeto.gof.dto.ClienteRequestDTO;
import com.padroesdeprojeto.gof.model.Cliente;
import com.padroesdeprojeto.gof.model.Endereco;

@Component
public class ClienteFactory {

	public Cliente criarNovo(ClienteRequestDTO dto) {
		String cepNormalizado = normalizarCep(dto.getCep());
		return Cliente.builder()
				.nome(dto.getNome().trim())
				.endereco(Endereco.builder().cep(cepNormalizado).build())
				.build();
	}

	public String normalizarCep(String cepBruto) {
		String apenasDigitos = cepBruto.replaceAll("\\D", "");
		return apenasDigitos.substring(0, 5) + "-" + apenasDigitos.substring(5);
	}
}
