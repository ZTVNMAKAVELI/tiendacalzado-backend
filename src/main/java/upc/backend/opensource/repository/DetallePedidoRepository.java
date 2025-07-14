package upc.backend.opensource.repository;

import upc.backend.opensource.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}