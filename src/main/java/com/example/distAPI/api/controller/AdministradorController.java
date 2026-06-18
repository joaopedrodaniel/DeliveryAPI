package com.example.distAPI.api.controller;

import com.example.distAPI.api.dto.AdministradorDTO;
import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Administrador;
import com.example.distAPI.service.AdministradorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/administradores")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService service;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody AdministradorDTO dto) {
        try {
            Administrador administrador = converter(dto);
            administrador = service.salvar(administrador);
            ModelMapper modelMapper = new ModelMapper();
            return new ResponseEntity<>(modelMapper.map(administrador, AdministradorDTO.class), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AdministradorDTO>> listar() {
        ModelMapper modelMapper = new ModelMapper();
        List<Administrador> administradores = service.listar();
        List<AdministradorDTO> dtoList = administradores.stream()
                .map(administrador -> modelMapper.map(administrador, AdministradorDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterPorId(@PathVariable("id") Long id) {
        ModelMapper modelMapper = new ModelMapper();
        return service.obterPorId(id)
                .map(administrador -> new ResponseEntity<>(modelMapper.map(administrador, AdministradorDTO.class), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        return service.obterPorId(id).map(administrador -> {
            try {
                service.excluir(administrador);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Administrador não encontrado.", HttpStatus.NOT_FOUND));
    }

    private Administrador converter(AdministradorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Administrador.class);
    }
}