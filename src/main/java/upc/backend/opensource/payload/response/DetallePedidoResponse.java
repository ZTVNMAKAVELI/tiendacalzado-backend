package upc.backend.opensource.payload.response;

import lombok.Data;

@Data
public class DetallePedidoResponse {
    private String productoNombre;
    private int cantidad;
    private double precio;
    private String productoImagenUrl;
}