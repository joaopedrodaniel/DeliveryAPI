package com.example.distAPI.api.controller;

import com.example.distAPI.api.dto.PedidoDTO;
import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.OrderStatus;
import com.example.distAPI.model.entity.Pedido;
import com.example.distAPI.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody PedidoDTO dto) {
        try {
            Pedido pedido = converter(dto);
            pedido = service.salvar(pedido);
            ModelMapper modelMapper = new ModelMapper();
            return new ResponseEntity<>(modelMapper.map(pedido, PedidoDTO.class), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listar() {
        ModelMapper modelMapper = new ModelMapper();
        List<Pedido> pedidos = service.listar();
        List<PedidoDTO> dtoList = pedidos.stream()
                .map(pedido -> modelMapper.map(pedido, PedidoDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterPorId(@PathVariable("id") Long id) {
        ModelMapper modelMapper = new ModelMapper();
        return service.obterPorId(id)
                .map(pedido -> new ResponseEntity<>(modelMapper.map(pedido, PedidoDTO.class), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        return service.obterPorId(id).map(pedido -> {
            try {
                service.excluir(pedido);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Pedido não encontrado.", HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        try {

            Pedido pedido = service.atualizarStatus(
                    id,
                    Enum.valueOf(OrderStatus.class, status));

            return ResponseEntity.ok(PedidoDTO.create(pedido));

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(
            @PathVariable Long id) {

        try {

            Pedido pedido = service.cancelar(id);

            return ResponseEntity.ok(
                    PedidoDTO.create(pedido));

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<PedidoDTO>> listarPorCliente(
            @PathVariable Long id) {

        List<PedidoDTO> pedidos = service
                .listarPorCliente(id)
                .stream()
                .map(PedidoDTO::create)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/vendedor/{id}")
    public ResponseEntity<List<PedidoDTO>> listarPorVendedor(
            @PathVariable Long id) {

        List<PedidoDTO> pedidos = service
                .listarPorVendedor(id)
                .stream()
                .map(PedidoDTO::create)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pedidos);
    }

    private Pedido converter(PedidoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Pedido.class);
    }
}