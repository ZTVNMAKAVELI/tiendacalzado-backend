package upc.backend.opensource.repository;

import upc.backend.opensource.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Al extender JpaRepository, Spring Data nos regala todos los métodos CRUD (findAll, findById, save, delete, etc.)
// sin que tengamos que escribir una sola línea de implementación SQL.
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}