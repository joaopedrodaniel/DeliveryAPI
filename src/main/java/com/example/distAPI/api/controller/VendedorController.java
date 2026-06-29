package com.example.distAPI.api.controller;

import com.example.distAPI.api.dto.VendedorDTO;
import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Vendedor;
import com.example.distAPI.service.VendedorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/vendedores")
@RequiredArgsConstructor
public class VendedorController {

    private final VendedorService service;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody VendedorDTO dto) {
        try {
            Vendedor vendedor = converter(dto);
            vendedor = service.salvar(vendedor);
            ModelMapper modelMapper = new ModelMapper();
            return new ResponseEntity<>(modelMapper.map(vendedor, VendedorDTO.class), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody VendedorDTO dto) {
        return service.obterPorId(id).map(entity -> {
            try {
                Vendedor vendedor = converter(dto);
                vendedor.setId(entity.getId());
                service.atualizar(vendedor);
                ModelMapper modelMapper = new ModelMapper();
                return ResponseEntity.ok(modelMapper.map(vendedor, VendedorDTO.class));
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Vendedor não encontrado.", HttpStatus.NOT_FOUND));
    }
            
    @GetMapping
    public ResponseEntity<List<VendedorDTO>> listar() {
        ModelMapper modelMapper = new ModelMapper();
        List<Vendedor> vendedores = service.listar();
        List<VendedorDTO> dtoList = vendedores.stream()
                .map(vendedor -> modelMapper.map(vendedor, VendedorDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterPorId(@PathVariable("id") Long id) {
        ModelMapper modelMapper = new ModelMapper();
        return service.obterPorId(id)
                .map(vendedor -> new ResponseEntity<>(modelMapper.map(vendedor, VendedorDTO.class), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        return service.obterPorId(id).map(vendedor -> {
            try {
                service.excluir(vendedor);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Vendedor não encontrado.", HttpStatus.NOT_FOUND));
    }

    private Vendedor converter(VendedorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Vendedor.class);
    }
}