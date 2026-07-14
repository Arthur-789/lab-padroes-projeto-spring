package com.padroesdeprojeto.gof.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.padroesdeprojeto.gof.service.notification.EstatisticaCadastroObserver;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/estatisticas")
@RequiredArgsConstructor
@Tag(name = "Estatísticas", description = "Métricas coletadas via padrão Observer a cada novo cadastro")
public class EstatisticasController {

	private final EstatisticaCadastroObserver estatisticaObserver;

	@GetMapping("/clientes-por-uf")
	@Operation(summary = "Quantidade de clientes cadastrados, agrupados por UF")
	public ResponseEntity<Map<String, Long>> porUf() {
		return ResponseEntity.ok(estatisticaObserver.obterEstatisticas());
	}
}
