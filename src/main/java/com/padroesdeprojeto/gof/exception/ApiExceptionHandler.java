package com.padroesdeprojeto.gof.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.padroesdeprojeto.gof.dto.ErroResponseDTO;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ClienteNaoEncontradoException.class)
	public ResponseEntity<ErroResponseDTO> handleClienteNaoEncontrado(ClienteNaoEncontradoException ex,
			HttpServletRequest request) {
		return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
	}

	@ExceptionHandler(CepInvalidoException.class)
	public ResponseEntity<ErroResponseDTO> handleCepInvalido(CepInvalidoException ex, HttpServletRequest request) {
		return construirResposta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request, null);
	}

	@ExceptionHandler(ServicoExternoIndisponivelException.class)
	public ResponseEntity<ErroResponseDTO> handleServicoExterno(ServicoExternoIndisponivelException ex,
			HttpServletRequest request) {
		return construirResposta(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), request, null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResponseDTO> handleValidacao(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
				.map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
				.collect(Collectors.toList());
		return construirResposta(HttpStatus.BAD_REQUEST, "Dados inválidos", request, detalhes);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErroResponseDTO> handleGenerico(Exception ex, HttpServletRequest request) {
		return construirResposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado", request, null);
	}

	private ResponseEntity<ErroResponseDTO> construirResposta(HttpStatus status, String mensagem,
			HttpServletRequest request, List<String> detalhes) {
		ErroResponseDTO corpo = ErroResponseDTO.builder()
				.timestamp(LocalDateTime.now())
				.status(status.value())
				.erro(status.getReasonPhrase())
				.mensagem(mensagem)
				.caminho(request.getRequestURI())
				.detalhes(detalhes)
				.build();
		return ResponseEntity.status(status).body(corpo);
	}
}
