package com.padroesdeprojeto.gof.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.padroesdeprojeto.gof.dto.ClienteRequestDTO;
import com.padroesdeprojeto.gof.exception.ClienteNaoEncontradoException;
import com.padroesdeprojeto.gof.factory.ClienteFactory;
import com.padroesdeprojeto.gof.model.Cliente;
import com.padroesdeprojeto.gof.model.Endereco;
import com.padroesdeprojeto.gof.repository.ClienteRepository;
import com.padroesdeprojeto.gof.repository.EnderecoRepository;
import com.padroesdeprojeto.gof.service.cep.CepResolverChain;
import com.padroesdeprojeto.gof.service.impl.ClienteServiceImpl;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

	@Mock
	private ClienteRepository clienteRepository;
	@Mock
	private EnderecoRepository enderecoRepository;
	@Mock
	private CepResolverChain cepResolverChain;
	@Mock
	private ApplicationEventPublisher eventPublisher;

	private final ClienteFactory clienteFactory = new ClienteFactory();

	@InjectMocks
	private ClienteServiceImpl clienteService;

	@Test
	void deveLancarExcecaoAoBuscarClienteInexistente() {
		when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> clienteService.buscarPorId(99L))
				.isInstanceOf(ClienteNaoEncontradoException.class);
	}

	@Test
	void deveReutilizarEnderecoJaExistenteAoInserirCliente() {
		Endereco enderecoExistente = Endereco.builder().cep("01310-930").localidade("São Paulo").build();
		when(enderecoRepository.findById("01310-930")).thenReturn(Optional.of(enderecoExistente));
		when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

		Cliente salvo = clienteService.inserir(new ClienteRequestDTO("Katherine Johnson", "01310-930"));

		assertThat(salvo.getEndereco().getLocalidade()).isEqualTo("São Paulo");
		verify(cepResolverChain, org.mockito.Mockito.never()).resolver(any());
		verify(eventPublisher).publishEvent(any());
	}

	@Test
	void deveResolverNovoEnderecoQuandoCepNaoEstaEmCache() {
		when(enderecoRepository.findById("20040-020")).thenReturn(Optional.empty());
		Endereco resolvido = Endereco.builder().cep("20040-020").localidade("Rio de Janeiro").build();
		when(cepResolverChain.resolver("20040-020")).thenReturn(resolvido);
		when(enderecoRepository.save(resolvido)).thenReturn(resolvido);
		when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

		Cliente salvo = clienteService.inserir(new ClienteRequestDTO("Margaret Hamilton", "20040-020"));

		assertThat(salvo.getEndereco().getLocalidade()).isEqualTo("Rio de Janeiro");
	}
}
