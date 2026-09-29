package org.example.minhaapi.controller;

import org.example.minhaapi.model.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final List<Produto> produtos = new ArrayList<>();
    private Long proximoId = 1L;

    @GetMapping
    public ResponseEntity<List<Produto>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax) {
        List<Produto> resultado = new ArrayList<>();
        for (Produto p : produtos) {
            boolean atende = true;

            if (nome != null && !p.getNome().toLowerCase().contains(nome.toLowerCase())) {
                atende = false;
            }
            if (categoria != null && !p.getCategoria().equalsIgnoreCase(categoria)) {
                atende = false;
            }
            if (precoMin != null && p.getPreco() < precoMin) {
                atende = false;
            }
            if (precoMax != null && p.getPreco() > precoMax) {
                atende = false;
            }
            if (atende) {
                resultado.add(p);
            }
        }
        return ResponseEntity.ok(resultado);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Produto produto = encontrarPorId(id);

        if (produto == null) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.ok(produto); // 200
    }

    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto novo) {
        novo.setId(proximoId++);
        produtos.add(novo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo); // 201
    }
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto dados) {
        Produto existente = encontrarPorId(id);

        if (existente == null) {
            return ResponseEntity.notFound().build(); // 404
        }
        existente.setNome(dados.getNome());
        existente.setCategoria(dados.getCategoria());
        existente.setPreco(dados.getPreco());
        existente.setEstoque(dados.getEstoque());
        return ResponseEntity.ok(existente); // 200
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Produto existente = encontrarPorId(id);
        if (existente == null) {
            return ResponseEntity.notFound().build(); // 404
        }
        produtos.remove(existente);
        return ResponseEntity.noContent().build(); // 204
    }
    private Produto encontrarPorId(Long id) {
        for (Produto p : produtos) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }
}