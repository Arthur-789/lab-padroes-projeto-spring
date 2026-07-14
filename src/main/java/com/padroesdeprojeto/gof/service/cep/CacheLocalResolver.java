package com.padroesdeprojeto.gof.service.cep;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.padroesdeprojeto.gof.exception.CepInvalidoException;
import com.padroesdeprojeto.gof.model.Endereco;
import com.padroesdeprojeto.gof.repository.EnderecoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheLocalResolver implements CepResolver {

	private final EnderecoRepository enderecoRepository;

	@Override
	public boolean suporta(String cep) {
		return !buscarPorPrefixo(cep).isEmpty();
	}

	@Override
	public int ordem() {
		return 10;
	}

	@Override
	public Endereco resolver(String cep) {
		Optional<Endereco> aproximado = buscarPorPrefixo(cep).stream().findFirst();
		Endereco base = aproximado.orElseThrow(() -> new CepInvalidoException(cep));

		log.info("ViaCEP indisponível: aproximando CEP {} a partir do cache local (prefixo de {})", cep,
				base.getCep());

		return Endereco.builder()
				.cep(cep)
				.logradouro(base.getLogradouro())
				.bairro(base.getBairro())
				.localidade(base.getLocalidade())
				.uf(base.getUf())
				.ibge(base.getIbge())
				.ddd(base.getDdd())
				.resolvidoPorFallback(true)
				.build();
	}

	private List<Endereco> buscarPorPrefixo(String cep) {
		String prefixo = cep.length() >= 5 ? cep.substring(0, 5) : cep;
		List<Endereco> todos = new ArrayList<>();
		enderecoRepository.findAll().forEach(todos::add);
		return todos.stream()
				.filter(endereco -> endereco.getCep() != null && endereco.getCep().startsWith(prefixo))
				.collect(Collectors.toList());
	}
}
