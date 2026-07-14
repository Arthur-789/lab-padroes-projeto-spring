package com.padroesdeprojeto.gof.exception;

public class CepInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CepInvalidoException(String cep) {
		super("CEP inválido ou não encontrado: " + cep);
	}

	public CepInvalidoException(String cep, Throwable causa) {
		super("CEP inválido ou não encontrado: " + cep, causa);
	}
}
