package com.padroesdeprojeto.gof.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI padoresDeProjetoOpenApi() {
		return new OpenAPI().info(new Info()
				.title("PadroesDeProjeto API")
				.description("API de cadastro de clientes com resolução automática de endereço via ViaCEP. "
						+ "Projeto desenvolvido a partir do Lab \"Explorando Padrões de Projetos na Prática com Java\" (DIO).")
				.version("1.0.0"));
	}
}
