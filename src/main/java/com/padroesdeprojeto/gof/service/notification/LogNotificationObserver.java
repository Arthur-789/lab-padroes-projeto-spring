package com.padroesdeprojeto.gof.service.notification;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogNotificationObserver {

	@EventListener
	public void aoCadastrarCliente(ClienteCadastradoEvent evento) {
		log.info("[Notificação] Novo cliente cadastrado: id={}, nome='{}', cep={}",
				evento.getCliente().getId(),
				evento.getCliente().getNome(),
				evento.getCliente().getEndereco().getCep());
	}
}
