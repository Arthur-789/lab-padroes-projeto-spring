package com.padroesdeprojeto.gof.exception;

public class ClienteNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClienteNaoEncontradoException(Long id) {
		super("Cliente não encontrado para o id: " + id);
	}
}
