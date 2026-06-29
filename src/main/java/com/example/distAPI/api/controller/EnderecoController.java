package com.example.distAPI.api.controller;

import com.example.distAPI.api.dto.EnderecoDTO;
import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Endereco;
import com.example.distAPI.service.EnderecoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService service;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody EnderecoDTO dto) {
        try {
            Endereco endereco = converter(dto);
            endereco = service.salvar(endereco);
            ModelMapper modelMapper = new ModelMapper();
            return new ResponseEntity<>(modelMapper.map(endereco, EnderecoDTO.class), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<EnderecoDTO>> listar() {
        ModelMapper modelMapper = new ModelMapper();
        List<Endereco> enderecos = service.listar();
        List<EnderecoDTO> dtoList = enderecos.stream()
                .map(endereco -> modelMapper.map(endereco, EnderecoDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterPorId(@PathVariable("id") Long id) {
        ModelMapper modelMapper = new ModelMapper();
        return service.obterPorId(id)
                .map(endereco -> new ResponseEntity<>(modelMapper.map(endereco, EnderecoDTO.class), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        return service.obterPorId(id).map(endereco -> {
            try {
                service.excluir(endereco);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Endereço não encontrado.", HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody EnderecoDTO dto) {
        return service.obterPorId(id).map(endereco -> {
            try {
                Endereco enderecoAtualizado = converter(dto);
                enderecoAtualizado.setId(endereco.getId());
                service.atualizar(enderecoAtualizado);
                ModelMapper modelMapper = new ModelMapper();
                return new ResponseEntity<>(modelMapper.map(enderecoAtualizado, EnderecoDTO.class), HttpStatus.OK);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Endereço não encontrado.", HttpStatus.NOT_FOUND));
    }

    private Endereco converter(EnderecoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Endereco.class);
    }
}