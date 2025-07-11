package upc.backend.opensource.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data // Anotación de Lombok para generar automáticamente Getters, Setters, toString, etc.
@Entity // Le dice a JPA que esta clase es una entidad que se mapeará a una tabla en la BD.
@Table(name = "categorias") // Especifica el nombre de la tabla en la base de datos.
public class Categoria {

    @Id // Marca este campo como la clave primaria (Primary Key).
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Le dice a PostgreSQL que genere el valor del ID automáticamente.
    private Long id;

    @Column(length = 50, nullable = false) // Define propiedades de la columna en la BD.
    private String nombre;

    // @JsonManagedReference: Es el lado "padre" de la relación.
    // Se serializará normalmente (mostrará la lista de productos).
    // Esta anotación es la clave para romper el bucle.
    @JsonIgnore // <-- REEMPLAZA @JsonManagedReference CON ESTO
    // @OneToMany: Define una relación de "uno a muchos". Una categoría puede tener muchos productos.
    // mappedBy="categoria": Indica que la relación es gestionada por el campo "categoria" en la clase Producto.
    // cascade=CascadeType.ALL: Si se elimina una categoría, se eliminarán todos sus productos asociados.
    // fetch=FetchType.LAZY: Los productos no se cargarán de la BD hasta que se acceda explícitamente a ellos.
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producto> productos;
}