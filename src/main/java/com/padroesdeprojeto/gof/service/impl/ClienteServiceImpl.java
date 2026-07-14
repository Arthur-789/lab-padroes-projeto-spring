package com.padroesdeprojeto.gof.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.padroesdeprojeto.gof.dto.ClienteRequestDTO;
import com.padroesdeprojeto.gof.exception.ClienteNaoEncontradoException;
import com.padroesdeprojeto.gof.factory.ClienteFactory;
import com.padroesdeprojeto.gof.model.Cliente;
import com.padroesdeprojeto.gof.model.Endereco;
import com.padroesdeprojeto.gof.repository.ClienteRepository;
import com.padroesdeprojeto.gof.repository.EnderecoRepository;
import com.padroesdeprojeto.gof.service.ClienteService;
import com.padroesdeprojeto.gof.service.cep.CepResolverChain;
import com.padroesdeprojeto.gof.service.notification.ClienteCadastradoEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository clienteRepository;
	private final EnderecoRepository enderecoRepository;
	private final CepResolverChain cepResolverChain;
	private final ClienteFactory clienteFactory;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new ClienteNaoEncontradoException(id));
	}

	@Override
	@Transactional
	public Cliente inserir(ClienteRequestDTO dto) {
		Cliente cliente = clienteFactory.criarNovo(dto);
		vincularEndereco(cliente);
		Cliente salvo = clienteRepository.save(cliente);
		eventPublisher.publishEvent(new ClienteCadastradoEvent(this, salvo));
		return salvo;
	}

	@Override
	@Transactional
	public Cliente atualizar(Long id, ClienteRequestDTO dto) {
		Cliente existente = buscarPorId(id);
		String cepNormalizado = clienteFactory.normalizarCep(dto.getCep());

		existente.setNome(dto.getNome().trim());
		if (!cepNormalizado.equals(existente.getEndereco().getCep())) {
			existente.setEndereco(Endereco.builder().cep(cepNormalizado).build());
			vincularEndereco(existente);
		}
		return clienteRepository.save(existente);
	}

	@Override
	@Transactional
	public void deletar(Long id) {
		if (!clienteRepository.existsById(id)) {
			throw new ClienteNaoEncontradoException(id);
		}
		clienteRepository.deleteById(id);
	}

	private void vincularEndereco(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco resolvido = cepResolverChain.resolver(cep);
			return enderecoRepository.save(resolvido);
		});
		cliente.setEndereco(endereco);
	}
}
