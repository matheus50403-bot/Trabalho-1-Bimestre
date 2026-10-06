package org.example.minhaapi.repository;

import org.example.minhaapi.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("""
        SELECT p FROM Produto p
        WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%')))
          AND (:categoria IS NULL OR LOWER(p.categoria) = LOWER(CAST(:categoria AS string)))
          AND (:precoMin IS NULL OR p.preco >= CAST(:precoMin AS double))
          AND (:precoMax IS NULL OR p.preco <= CAST(:precoMax AS double))
        """)
    List<Produto> filtrar(@Param("nome") String nome,
                          @Param("categoria") String categoria,
                          @Param("precoMin") Double precoMin,
                          @Param("precoMax") Double precoMax);
}