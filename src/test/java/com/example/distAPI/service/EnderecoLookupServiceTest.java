package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Endereco;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnderecoLookupServiceTest {

    private final ViaCepClient viaCepClient = mock(ViaCepClient.class);
    private final EnderecoLookupService service = new EnderecoLookupService(viaCepClient);

    @Test
    void devePreencherEnderecoComDadosDoViaCep() {
        when(viaCepClient.buscarPorCep("01001000")).thenReturn(Map.of(
                "logradouro", "Praça da Sé",
                "bairro", "Sé",
                "localidade", "São Paulo",
                "uf", "SP"
        ));

        Endereco endereco = new Endereco();
        endereco.setComplemento("Apto 10");

        service.preencherPorCep("01001000", endereco);

        assertEquals("Praça da Sé", endereco.getLogradouro());
        assertEquals("Sé", endereco.getBairro());
        assertEquals("São Paulo", endereco.getCidade());
        assertEquals("SP", endereco.getUf());
        assertEquals("Apto 10", endereco.getComplemento());
    }

    @Test
    void deveLancarErroQuandoCepNaoForEncontrado() {
        when(viaCepClient.buscarPorCep("00000000")).thenReturn(Map.of("erro", true));

        assertThrows(
                RegraNegocioException.class,
                () -> service.preencherPorCep("00000000", new Endereco())
        );
    }
}
