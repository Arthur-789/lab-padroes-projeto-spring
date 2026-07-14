package com.padroesdeprojeto.gof.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.padroesdeprojeto.gof.dto.ClienteRequestDTO;
import com.padroesdeprojeto.gof.model.Cliente;
import com.padroesdeprojeto.gof.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operações de cadastro de clientes e resolução automática de endereço via CEP")
public class ClienteRestController {

	private final ClienteService clienteService;

	@GetMapping
	@Operation(summary = "Lista todos os clientes cadastrados")
	public ResponseEntity<Iterable<Cliente>> buscarTodos() {
		return ResponseEntity.ok(clienteService.buscarTodos());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um cliente pelo id")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(clienteService.buscarPorId(id));
	}

	@PostMapping
	@Operation(summary = "Cadastra um novo cliente, resolvendo o endereço automaticamente pelo CEP")
	public ResponseEntity<Cliente> inserir(@Valid @RequestBody ClienteRequestDTO dto) {
		Cliente criado = clienteService.inserir(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(criado);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualiza os dados de um cliente existente")
	public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
		return ResponseEntity.ok(clienteService.atualizar(id, dto));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Remove um cliente pelo id")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		clienteService.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
