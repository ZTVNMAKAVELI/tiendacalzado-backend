package upc.backend.opensource.controller;

import upc.backend.opensource.model.Producto;
import upc.backend.opensource.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // Combina @Controller y @ResponseBody, simplificando la creación de APIs REST.
@RequestMapping("/api/productos") // Todas las rutas en este controlador comenzarán con /api/productos.
@CrossOrigin(origins = "http://localhost:15840") // Permite peticiones desde tu frontend de Angular.
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
}