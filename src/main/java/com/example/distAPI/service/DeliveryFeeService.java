package com.example.distAPI.service;

import com.example.distAPI.model.entity.Endereco;
import org.springframework.stereotype.Service;

@Service
public class DeliveryFeeService {

    public Double calcularTaxa(Endereco endereco) {
        if (endereco == null || endereco.getCep() == null) {
            return 0.0;
        }
        String cep = endereco.getCep().replaceAll("\\D", "");
        if (cep.length() < 2) return 5.0;

        char first = cep.charAt(0);
        switch (first) {
            case '0': // São Paulo / RS etc
            case '1':
            case '2':
                return 5.0;
            case '3':
            case '4':
                return 7.5;
            case '5':
            case '6':
                return 10.0;
            default:
                return 12.0;
        }
    }
}
