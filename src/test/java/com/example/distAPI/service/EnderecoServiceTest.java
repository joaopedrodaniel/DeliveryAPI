package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Endereco;
import com.example.distAPI.model.repository.EnderecoRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class EnderecoServiceTest {

    private final EnderecoRepository repository = mock(EnderecoRepository.class);
    private final EnderecoLookupService lookupService = mock(EnderecoLookupService.class);
    private final EnderecoService service = new EnderecoService(repository, lookupService);

    @Test
    void deveNormalizarCepAntesDeSalvar() {
        Endereco endereco = new Endereco();
        endereco.setCep("01001-000");
        endereco.setLogradouro("Praça da Sé");
        endereco.setNumero("100");

        when(lookupService.preencherPorCep("01001000", endereco)).thenReturn(endereco);
        when(repository.save(endereco)).thenReturn(endereco);

        service.salvar(endereco);

        assertEquals("01001000", endereco.getCep());
        verify(lookupService).preencherPorCep("01001000", endereco);
        verify(repository).save(endereco);
    }

    @Test
    void deveRecusarCepComQuantidadeInvalidaDeDigitos() {
        Endereco endereco = new Endereco();
        endereco.setCep("123");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("10");

        assertThrows(RegraNegocioException.class, () -> service.salvar(endereco));
        verifyNoInteractions(lookupService, repository);
    }
}
