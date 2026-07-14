package com.padroesdeprojeto.gof.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.padroesdeprojeto.gof.exception.CepInvalidoException;
import com.padroesdeprojeto.gof.model.Endereco;
import com.padroesdeprojeto.gof.service.cep.CepResolver;
import com.padroesdeprojeto.gof.service.cep.CepResolverChain;

class CepResolverChainTest {

	@Test
	void devePriorizarEstrategiaDeMenorOrdem() {
		CepResolver primaria = fakeResolver(0, true, "01310-930");
		CepResolver fallback = fakeResolver(10, true, "00000-000");

		CepResolverChain chain = new CepResolverChain(List.of(fallback, primaria));

		Endereco resolvido = chain.resolver("01310-930");

		assertThat(resolvido.getCep()).isEqualTo("01310-930");
	}

	@Test
	void deveCairParaFallbackQuandoEstrategiaPrimariaFalha() {
		CepResolver primaria = new CepResolver() {
			public boolean suporta(String cep) { return true; }
			public int ordem() { return 0; }
			public Endereco resolver(String cep) { throw new CepInvalidoException(cep); }
		};
		CepResolver fallback = fakeResolver(10, true, "20040-020");

		CepResolverChain chain = new CepResolverChain(List.of(primaria, fallback));

		Endereco resolvido = chain.resolver("20040-020");

		assertThat(resolvido.getCep()).isEqualTo("20040-020");
	}

	@Test
	void deveLancarExcecaoQuandoNenhumaEstrategiaResolve() {
		CepResolver semSuporte = fakeResolver(0, false, null);

		CepResolverChain chain = new CepResolverChain(List.of(semSuporte));

		assertThatThrownBy(() -> chain.resolver("99999-999"))
				.isInstanceOf(CepInvalidoException.class);
	}

	private CepResolver fakeResolver(int ordem, boolean suporta, String cepRetornado) {
		return new CepResolver() {
			public boolean suporta(String cep) { return suporta; }
			public int ordem() { return ordem; }
			public Endereco resolver(String cep) { return Endereco.builder().cep(cepRetornado).build(); }
		};
	}
}
