package upc.backend.opensource.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // <-- AÑADE ESTA IMPORTACIÓN
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity // <--- ¡ESTA ES LA LÍNEA MÁS IMPORTANTE! Asegúrate de que exista.
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT") // Permite una descripción más larga.
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "imagen_url") // Especifica el nombre de la columna en la BD.
    private String imagenUrl;

    // @JsonBackReference: Es el lado "hijo" de la relación.
    // Este campo NO se incluirá en la serialización JSON cuando se acceda desde el padre,
    // evitando así el bucle infinito. Sin embargo, el objeto Categoria sí se mostrará
    // con su ID y nombre, lo cual es perfecto para la lista de productos.
    @JsonBackReference // <-- AÑADE ESTA ANOTACIÓN
    // @ManyToOne: Define una relación de "muchos a uno". Muchos productos pueden pertenecer a una categoría.
    // @JoinColumn: Especifica la columna de clave foránea (Foreign Key) en la tabla "productos".
    @ManyToOne(fetch = FetchType.EAGER) // EAGER: Carga la categoría junto con el producto.
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
}
