package upc.backend.opensource.config;

import upc.backend.opensource.model.Categoria;
import upc.backend.opensource.model.Producto;
import upc.backend.opensource.model.Role;
import upc.backend.opensource.model.User;
import upc.backend.opensource.repository.CategoriaRepository;
import upc.backend.opensource.repository.ProductoRepository;
import upc.backend.opensource.repository.RoleRepository;
import upc.backend.opensource.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

        // --- INICIALIZAR ROLES ---
        // Verificamos si ya hay roles para no duplicarlos
        if (roleRepository.count() == 0) {
            System.out.println("Creando roles iniciales...");
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
            System.out.println("Roles creados.");
        }

        // --- INICIALIZAR USUARIO ADMIN POR DEFECTO ---
        if (userRepository.count() == 0) {
            System.out.println("Creando usuario ADMIN por defecto...");
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Error: Rol ADMIN no encontrado."));

            User adminUser = new User(
                    "Administrador",
                    "admin@bananitos.com",
                    encoder.encode("admin123") // ¡Importante encriptar la contraseña!
            );

            adminUser.setRoles(Set.of(adminRole));
            userRepository.save(adminUser);
            System.out.println("Usuario ADMIN creado. Usuario: admin, Contraseña: admin123");
        }

        // Verificamos si ya hay categorías para no duplicar los datos en cada reinicio.
        if (categoriaRepository.count() == 0) {
            System.out.println("No hay datos. Inicializando base de datos con datos de prueba...");

            // --- 1. CREAR CATEGORÍAS ---
            Categoria deportivo = new Categoria();
            deportivo.setNombre("Deportivo");

            Categoria formal = new Categoria();
            formal.setNombre("Formal");

            Categoria casual = new Categoria();
            casual.setNombre("Casual");

            // Guardamos las categorías en la base de datos.

            categoriaRepository.saveAll(Arrays.asList(deportivo, formal, casual));

            // --- 2. CREAR PRODUCTOS ---
            Producto p1 = new Producto();
            p1.setNombre("Zapatillas Urban Runner");
            p1.setDescripcion("Zapatillas ligeras para correr por la ciudad.");
            p1.setPrecio(189.90);
            p1.setStock(50);
            p1.setImagenUrl("https://placehold.co/400x400/3498db/ffffff?text=Urbano");
            p1.setCategoria(deportivo); // Asociamos con la categoría guardada.

            Producto p2 = new Producto();
            p2.setNombre("Botines de Cuero 'Elegance'");
            p2.setDescripcion("Botines de cuero genuino para ocasiones especiales.");
            p2.setPrecio(320.00);
            p2.setStock(30);
            p2.setImagenUrl("https://placehold.co/400x400/2c3e50/ffffff?text=Elegance");
            p2.setCategoria(formal);

            Producto p3 = new Producto();
            p3.setNombre("Sandalias de Verano 'Sol'");
            p3.setDescripcion("Sandalias frescas y cómodas para el día a día.");
            p3.setPrecio(79.50);
            p3.setStock(100);
            p3.setImagenUrl("https://placehold.co/400x400/f1c40f/ffffff?text=Sol");
            p3.setCategoria(casual);

            Producto p4 = new Producto();
            p4.setNombre("Zapatos de Trekking 'Aventura'");
            p4.setDescripcion("Calzado resistente para tus aventuras al aire libre.");
            p4.setPrecio(250.00);
            p4.setStock(40);
            p4.setImagenUrl("https://placehold.co/400x400/27ae60/ffffff?text=Aventura");
            p4.setCategoria(deportivo);

            // Guardamos los productos en la base de datos.
            productoRepository.saveAll(Arrays.asList(p1, p2, p3, p4));

            System.out.println("¡Datos de prueba cargados exitosamente!");
        } else {
            System.out.println("La base de datos ya contiene datos. No se requiere inicialización.");
        }
    }
}