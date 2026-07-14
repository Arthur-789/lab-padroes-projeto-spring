package com.padroesdeprojeto.gof.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErroResponseDTO {

	private LocalDateTime timestamp;
	private int status;
	private String erro;
	private String mensagem;
	private String caminho;
	private List<String> detalhes;
}
