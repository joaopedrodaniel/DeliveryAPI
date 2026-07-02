package com.example.distAPI.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ViaCepClient {

    private final RestTemplate restTemplate;

    @Value("${viacep.url:https://viacep.com.br/ws}")
    private String viaCepUrl;

    public ViaCepClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> buscarPorCep(String cep) {
        if (cep == null) {
            return null;
        }
        String cleaned = cep.replaceAll("\\D", "");
        String url = String.format("%s/%s/json/", viaCepUrl, cleaned);
        try {
            return restTemplate.getForObject(url, Map.class);
        } catch (RestClientException ex) {
            return null;
        }
    }
}
