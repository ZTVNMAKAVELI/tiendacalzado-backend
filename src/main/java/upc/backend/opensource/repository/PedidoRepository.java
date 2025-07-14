package upc.backend.opensource.repository;

import upc.backend.opensource.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
