package com.padroesdeprojeto.gof.service.cep;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.padroesdeprojeto.gof.exception.CepInvalidoException;
import com.padroesdeprojeto.gof.exception.ServicoExternoIndisponivelException;
import com.padroesdeprojeto.gof.model.Endereco;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CepResolverChain {

	private final List<CepResolver> estrategiasOrdenadas;

	public CepResolverChain(List<CepResolver> resolvers) {
		this.estrategiasOrdenadas = resolvers.stream()
				.sorted(Comparator.comparingInt(CepResolver::ordem))
				.collect(Collectors.toList());
	}

	public Endereco resolver(String cep) {
		RuntimeException ultimaFalha = null;
		for (CepResolver estrategia : estrategiasOrdenadas) {
			if (!estrategia.suporta(cep)) {
				continue;
			}
			try {
				return estrategia.resolver(cep);
			} catch (CepInvalidoException | ServicoExternoIndisponivelException e) {
				log.debug("Estratégia {} não conseguiu resolver o CEP {}: {}",
						estrategia.getClass().getSimpleName(), cep, e.getMessage());
				ultimaFalha = e;
			}
		}
		throw ultimaFalha != null ? ultimaFalha : new CepInvalidoException(cep);
	}
}
