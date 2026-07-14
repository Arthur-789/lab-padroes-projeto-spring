package com.padroesdeprojeto.gof.exception;

public class ServicoExternoIndisponivelException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServicoExternoIndisponivelException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
