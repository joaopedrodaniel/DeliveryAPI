package com.example.distAPI.api.controller;

import com.example.distAPI.api.dto.ClienteDTO;
import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Cliente;
import com.example.distAPI.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ClienteDTO dto) {
        try {
            Cliente cliente = converter(dto);
            cliente = service.salvar(cliente);
            ModelMapper modelMapper = new ModelMapper();
            return new ResponseEntity<>(modelMapper.map(cliente, ClienteDTO.class), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listar() {
        ModelMapper modelMapper = new ModelMapper();
        List<Cliente> clientes = service.listar();
        List<ClienteDTO> dtoList = clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterPorId(@PathVariable("id") Long id) {
        ModelMapper modelMapper = new ModelMapper();
        return service.obterPorId(id)
                .map(cliente -> new ResponseEntity<>(modelMapper.map(cliente, ClienteDTO.class), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        return service.obterPorId(id).map(cliente -> {
            try {
                service.excluir(cliente);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Cliente não encontrado.", HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody ClienteDTO dto) {

        return service.obterPorId(id)
                .map(entity -> {

                    try {

                        Cliente cliente = converter(dto);
                        cliente.setId(entity.getId());

                        service.salvar(cliente);

                        return ResponseEntity.ok(
                                ClienteDTO.create(cliente));

                    } catch (RegraNegocioException e) {

                        return ResponseEntity
                                .badRequest()
                                .body(e.getMessage());
                    }

                }).orElseGet(() ->
                        new ResponseEntity<>(
                                "Cliente não encontrado",
                                HttpStatus.NOT_FOUND));
    }

    private Cliente converter(ClienteDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Cliente.class);
    }
}