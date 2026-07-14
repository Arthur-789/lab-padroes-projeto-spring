package com.padroesdeprojeto.gof.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.padroesdeprojeto.gof.dto.ClienteRequestDTO;
import com.padroesdeprojeto.gof.service.ClienteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile("!test")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

	private final ClienteService clienteService;

	@Override
	public void run(String... args) {
		try {
			clienteService.inserir(new ClienteRequestDTO("Alan Turing", "01310-930"));
			clienteService.inserir(new ClienteRequestDTO("João Silva", "20040-020"));
			log.info("Dados de demonstração carregados com sucesso.");
		} catch (Exception e) {
			log.warn("Não foi possível carregar os dados de demonstração (provável indisponibilidade do ViaCEP): {}",
					e.getMessage());
		}
	}
}
