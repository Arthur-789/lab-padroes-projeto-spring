package com.padroesdeprojeto.gof.factory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.padroesdeprojeto.gof.dto.ClienteRequestDTO;
import com.padroesdeprojeto.gof.model.Cliente;

class ClienteFactoryTest {

	private final ClienteFactory factory = new ClienteFactory();

	@Test
	void deveNormalizarCepSemMascara() {
		String normalizado = factory.normalizarCep("01310930");
		assertThat(normalizado).isEqualTo("01310-930");
	}

	@Test
	void deveManterCepJaFormatado() {
		String normalizado = factory.normalizarCep("01310-930");
		assertThat(normalizado).isEqualTo("01310-930");
	}

	@Test
	void deveCriarClienteComNomeSemEspacosEEnderecoComCepNormalizado() {
		ClienteRequestDTO dto = new ClienteRequestDTO("  Grace Hopper  ", "20040020");

		Cliente cliente = factory.criarNovo(dto);

		assertThat(cliente.getNome()).isEqualTo("Grace Hopper");
		assertThat(cliente.getEndereco().getCep()).isEqualTo("20040-020");
	}
}
