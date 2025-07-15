package upc.backend.opensource.payload.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponse {
    private Long id;
    private LocalDateTime fechaCreacion;
    private Double total;
    private List<DetallePedidoResponse> detalles;
}