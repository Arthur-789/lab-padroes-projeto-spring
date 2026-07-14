package com.padroesdeprojeto.gof.service.cep;

import org.springframework.stereotype.Component;

import com.padroesdeprojeto.gof.exception.CepInvalidoException;
import com.padroesdeprojeto.gof.exception.ServicoExternoIndisponivelException;
import com.padroesdeprojeto.gof.model.Endereco;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViaCepResolver implements CepResolver {

	private final ViaCepClient viaCepClient;

	@Override
	public boolean suporta(String cep) {
		return true;
	}

	@Override
	public int ordem() {
		return 0;
	}

	@Override
	public Endereco resolver(String cep) {
		try {
			Endereco endereco = viaCepClient.consultarCep(cep);
			if (endereco == null || endereco.getCep() == null) {
				throw new CepInvalidoException(cep);
			}
			log.info("CEP {} resolvido com sucesso via ViaCEP", cep);
			return endereco;
		} catch (FeignException.NotFound e) {
			log.warn("ViaCEP não encontrou o CEP {}", cep);
			throw new CepInvalidoException(cep, e);
		} catch (FeignException e) {
			log.error("ViaCEP indisponível ao consultar o CEP {}: {}", cep, e.getMessage());
			throw new ServicoExternoIndisponivelException("Serviço ViaCEP indisponível no momento", e);
		}
	}
}
