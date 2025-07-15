package upc.backend.opensource.repository;

import upc.backend.opensource.model.Pedido;
import upc.backend.opensource.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUserOrderByFechaCreacionDesc(User user);
}
