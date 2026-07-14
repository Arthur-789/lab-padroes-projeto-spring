package com.padroesdeprojeto.gof.service.cep;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.padroesdeprojeto.gof.model.Endereco;

@FeignClient(name = "viacep", url = "${padroesdeprojeto.viacep.url:https://viacep.com.br/ws}")
public interface ViaCepClient {

	@GetMapping("/{cep}/json/")
	Endereco consultarCep(@PathVariable("cep") String cep);
}
