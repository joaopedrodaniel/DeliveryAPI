package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Endereco;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EnderecoLookupService {

    private final ViaCepClient viaCepClient;

    public EnderecoLookupService(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    public Endereco preencherPorCep(String cep, Endereco endereco) {
        Map<String, Object> res = viaCepClient.buscarPorCep(cep);
        if (res == null) {
            throw new RegraNegocioException("Não foi possível consultar o CEP informado.");
        }
        if (res.containsKey("erro")) {
            throw new RegraNegocioException("CEP não encontrado.");
        }

        Object logradouro = res.get("logradouro");
        Object bairro = res.get("bairro");
        Object localidade = res.get("localidade");
        Object uf = res.get("uf");

        preencherSeVazio(endereco, "logradouro", logradouro);
        preencherSeVazio(endereco, "bairro", bairro);
        preencherSeVazio(endereco, "cidade", localidade);
        preencherSeVazio(endereco, "uf", uf);
        
        return endereco;
    }

    private void preencherSeVazio(Endereco endereco, String campo, Object valor) {
        if (valor == null || valor.toString().isBlank()) {
            return;
        }

        switch (campo) {
            case "logradouro":
                if (endereco.getLogradouro() == null || endereco.getLogradouro().isBlank()) {
                    endereco.setLogradouro(valor.toString());
                }
                break;
            case "bairro":
                if (endereco.getBairro() == null || endereco.getBairro().isBlank()) {
                    endereco.setBairro(valor.toString());
                }
                break;
            case "cidade":
                if (endereco.getCidade() == null || endereco.getCidade().isBlank()) {
                    endereco.setCidade(valor.toString());
                }
                break;
            case "uf":
                if (endereco.getUf() == null || endereco.getUf().isBlank()) {
                    endereco.setUf(valor.toString());
                }
                break;
            default:
                break;
        }
    }
}
