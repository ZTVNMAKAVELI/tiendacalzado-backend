package upc.backend.opensource.controller;

import upc.backend.opensource.model.Producto;
import upc.backend.opensource.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController // Combina @Controller y @ResponseBody, simplificando la creación de APIs REST.
@RequestMapping("/api/productos") // Todas las rutas en este controlador comenzarán con /api/productos.
@CrossOrigin(origins = "http://localhost:4200") // Permite peticiones desde tu frontend de Angular.
public class ProductoController {

    // @Autowired: Inyección de dependencias. Spring se encarga de crear una instancia
    // de ProductoRepository y proporcionárnosla aquí.
    @Autowired
    private ProductoRepository productoRepository;

    // @GetMapping: Mapea peticiones HTTP GET a este método.
    // Cuando alguien haga una petición GET a /api/productos, se ejecutará este código.
    @GetMapping
    public List<Producto> getAllProductos() {
        // Usa el método findAll() que nos regaló JpaRepository para obtener todos los productos.
        return productoRepository.findAll();
    }

    // Mapea peticiones GET a /api/productos/{id}, donde {id} es una variable.
    @GetMapping("/{id}")
    public ResponseEntity<Producto> findProductoById(@PathVariable Long id) {
        // @PathVariable extrae el valor de {id} de la URL.

        // Usamos el método findById del repositorio.
        // Devuelve un Optional, que es una forma segura de manejar casos donde el producto no exista.
        return productoRepository.findById(id)
                .map(producto -> ResponseEntity.ok().body(producto)) // Si se encuentra, devuelve 200 OK con el producto.
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, devuelve un error 404 Not Found.
    }
}