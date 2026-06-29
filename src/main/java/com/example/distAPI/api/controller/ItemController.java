package com.example.distAPI.api.controller;

import com.example.distAPI.api.dto.ItemDTO;
import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Item;
import com.example.distAPI.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/itens")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ItemDTO dto) {
        try {
            Item item = converter(dto);
            item = service.salvar(item);
            ModelMapper modelMapper = new ModelMapper();
            return new ResponseEntity<>(modelMapper.map(item, ItemDTO.class), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> listar() {
        ModelMapper modelMapper = new ModelMapper();
        List<Item> itens = service.listar();
        List<ItemDTO> dtoList = itens.stream()
                .map(item -> modelMapper.map(item, ItemDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterPorId(@PathVariable("id") Long id) {
        ModelMapper modelMapper = new ModelMapper();
        return service.obterPorId(id)
                .map(item -> new ResponseEntity<>(modelMapper.map(item, ItemDTO.class), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        return service.obterPorId(id).map(item -> {
            try {
                service.excluir(item);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Item não encontrado.", HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody ItemDTO dto) {
        return service.obterPorId(id).map(itemExistente -> {
            try {
                Item itemAtualizado = converter(dto);
                itemAtualizado.setId(itemExistente.getId());
                service.atualizar(itemAtualizado);
                ModelMapper modelMapper = new ModelMapper();
                return ResponseEntity.ok(modelMapper.map(itemAtualizado, ItemDTO.class));
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Item não encontrado.", HttpStatus.NOT_FOUND));
    }

    private Item converter(ItemDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Item.class);
    }
}