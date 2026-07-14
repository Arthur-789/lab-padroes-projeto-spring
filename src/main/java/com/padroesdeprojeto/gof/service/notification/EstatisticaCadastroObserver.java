package com.padroesdeprojeto.gof.service.notification;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EstatisticaCadastroObserver {

	private final Map<String, AtomicLong> cadastrosPorUf = new ConcurrentHashMap<>();

	@EventListener
	public void aoCadastrarCliente(ClienteCadastradoEvent evento) {
		String uf = evento.getCliente().getEndereco().getUf();
		String chave = (uf == null || uf.isBlank()) ? "DESCONHECIDO" : uf;
		cadastrosPorUf.computeIfAbsent(chave, k -> new AtomicLong()).incrementAndGet();
	}

	public Map<String, Long> obterEstatisticas() {
		Map<String, Long> resultado = new ConcurrentHashMap<>();
		cadastrosPorUf.forEach((uf, contador) -> resultado.put(uf, contador.get()));
		return resultado;
	}
}
