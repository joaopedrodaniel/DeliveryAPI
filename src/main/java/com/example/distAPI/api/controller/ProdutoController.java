package com.example.distAPI.api.controller;

import com.example.distAPI.api.dto.ProdutoDTO;
import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Produto;
import com.example.distAPI.model.entity.StatusProduto;
import com.example.distAPI.service.ProdutoService;
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
@RequestMapping("/api/v1/produtos")
@Tag(name = "Produtos", description = "Operações para cadastro, consulta e gerenciamento de produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @Operation(summary = "Cadastrar produto", description = "Cria um novo produto com todos os dados necessários.")
    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ProdutoDTO dto) {
        try {
            Produto produto = converter(dto);
            produto = service.salvar(produto);
            ModelMapper modelMapper = new ModelMapper();
            return new ResponseEntity<>(modelMapper.map(produto, ProdutoDTO.class), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listar() {
        ModelMapper modelMapper = new ModelMapper();
        List<Produto> produtos = service.listar();

        List<ProdutoDTO> dtoList = produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterPorId(@PathVariable("id") Long id) {
        ModelMapper modelMapper = new ModelMapper();
        return service.obterPorId(id)
                .map(produto -> new ResponseEntity<>(modelMapper.map(produto, ProdutoDTO.class), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoDTO>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) StatusProduto status) {
        ModelMapper modelMapper = new ModelMapper();
        List<Produto> produtos = service.buscar(nome, status);

        List<ProdutoDTO> dtoList = produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/{id}")
        public ResponseEntity<?> atualizar(
                @PathVariable Long id,
                @RequestBody ProdutoDTO dto) {

            return service.obterPorId(id)
                    .map(entity -> {

                        try {

                            Produto produto = converter(dto);
                            produto.setId(entity.getId());

                            service.salvar(produto);

                            return ResponseEntity.ok(
                                    ProdutoDTO.create(produto));

                        } catch (RegraNegocioException e) {
                            return ResponseEntity
                                    .badRequest()
                                    .body(e.getMessage());
                        }

                    }).orElseGet(() ->
                            new ResponseEntity<>(
                                    "Produto não encontrado",
                                    HttpStatus.NOT_FOUND));
        }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        return service.obterPorId(id).map(produto -> {
            try {
                service.excluir(produto);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Produto não encontrado.", HttpStatus.NOT_FOUND));
    }

    private Produto converter(ProdutoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Produto.class);
    }
}