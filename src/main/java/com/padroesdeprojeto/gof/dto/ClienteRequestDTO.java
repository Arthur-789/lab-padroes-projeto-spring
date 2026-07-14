package com.padroesdeprojeto.gof.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDTO {

	@NotBlank(message = "O nome do cliente é obrigatório")
	@Size(min = 2, max = 120, message = "O nome deve ter entre 2 e 120 caracteres")
	private String nome;

	@NotBlank(message = "O CEP é obrigatório")
	@Pattern(regexp = "\\d{5}-?\\d{3}", message = "O CEP deve estar no formato 00000-000 ou 00000000")
	private String cep;
}
