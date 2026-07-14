package com.padroesdeprojeto.gof.service.notification;

import org.springframework.context.ApplicationEvent;

import com.padroesdeprojeto.gof.model.Cliente;

import lombok.Getter;

@Getter
public class ClienteCadastradoEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private final Cliente cliente;

	public ClienteCadastradoEvent(Object source, Cliente cliente) {
		super(source);
		this.cliente = cliente;
	}
}
