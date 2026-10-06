package org.example.minhaapi.controller;

import org.example.minhaapi.model.Produto;
import org.example.minhaapi.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository repository;

    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax) {
        List<Produto> resultado = repository.filtrar(nome, categoria, precoMin, precoMax);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Optional<Produto> produto = repository.findById(id);

        if (produto.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.ok(produto.get()); // 200
    }

    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto novo) {
        novo.setId(null); // garante que o ID é gerado pelo banco
        Produto salvo = repository.save(novo);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo); // 201
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto dados) {
        Optional<Produto> encontrado = repository.findById(id);

        if (encontrado.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }
        Produto existente = encontrado.get();
        existente.setNome(dados.getNome());
        existente.setCategoria(dados.getCategoria());
        existente.setPreco(dados.getPreco());
        existente.setEstoque(dados.getEstoque());
        return ResponseEntity.ok(repository.save(existente)); // 200
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Optional<Produto> existente = repository.findById(id);

        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }
        repository.delete(existente.get());
        return ResponseEntity.noContent().build(); // 204
    }
}