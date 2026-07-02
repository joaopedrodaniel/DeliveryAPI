package com.example.distAPI.api.controller;

import com.example.distAPI.api.dto.ItemDTO;
import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Item;
import com.example.distAPI.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/itens")
@Tag(name = "Itens", description = "Operações para gerenciamento dos itens de pedido")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @Operation(summary = "Adicionar item", description = "Adiciona um item ao pedido especificado com produto e quantidade.")
    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ItemDTO dto) {
        try {
            Item item = converter(dto);
            item = service.salvar(item);
            return new ResponseEntity<>(ItemDTO.create(item), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> listar() {
        List<Item> itens = service.listar();
        List<ItemDTO> dtoList = itens.stream()
                .map(ItemDTO::create)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterPorId(@PathVariable("id") Long id) {
        return service.obterPorId(id)
                .map(item -> new ResponseEntity<>(ItemDTO.create(item), HttpStatus.OK))
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
                itemAtualizado = service.atualizar(itemAtualizado);
                return ResponseEntity.ok(ItemDTO.create(itemAtualizado));
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Item não encontrado.", HttpStatus.NOT_FOUND));
    }

    private Item converter(ItemDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Item item = modelMapper.map(dto, Item.class);

        if (dto.getIdProduto() != null) {
            com.example.distAPI.model.entity.Produto produto = new com.example.distAPI.model.entity.Produto();
            produto.setId(dto.getIdProduto());
            item.setProduto(produto);
        } else if (dto.getProduto() != null && dto.getProduto().getId() != null) {
            com.example.distAPI.model.entity.Produto produto = new com.example.distAPI.model.entity.Produto();
            produto.setId(dto.getProduto().getId());
            item.setProduto(produto);
        }

        if (dto.getIdPedido() != null) {
            com.example.distAPI.model.entity.Pedido pedido = new com.example.distAPI.model.entity.Pedido();
            pedido.setId(dto.getIdPedido());
            item.setPedido(pedido);
        }

        return item;
    }
}