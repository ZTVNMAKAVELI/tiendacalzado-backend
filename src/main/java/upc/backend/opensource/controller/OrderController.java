package upc.backend.opensource.controller;

import upc.backend.opensource.model.*;
import upc.backend.opensource.payload.request.CartItemRequest;
import upc.backend.opensource.payload.response.MessageResponse;
import upc.backend.opensource.repository.PedidoRepository;
import upc.backend.opensource.repository.ProductoRepository;
import upc.backend.opensource.repository.UserRepository;
import upc.backend.opensource.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @PostMapping("/orders")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Protegemos el endpoint
    public ResponseEntity<?> createOrder(@RequestBody List<CartItemRequest> cartItems) {
        // 1. Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));

        // 2. Crear objeto Pedido
        Pedido pedido = new Pedido();
        pedido.setUser(currentUser);
        pedido.setFechaCreacion(LocalDateTime.now());

        double totalPedido = 0.0;

        // 3. Crear los Detalles del Pedido a partir del carrito
        for (CartItemRequest itemRequest : cartItems) {
            Producto producto = productoRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Error: Producto no encontrado con id: " + itemRequest.getProductId()));

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(itemRequest.getQuantity());
            detalle.setPrecio(producto.getPrecio()); // Guardamos el precio actual

            pedido.getDetalles().add(detalle);
            totalPedido += (producto.getPrecio() * itemRequest.getQuantity());
        }

        pedido.setTotal(totalPedido);

        // 4. Guardar el pedido
        pedidoRepository.save(pedido);

        return ResponseEntity.ok(new MessageResponse("¡Pedido creado exitosamente!"));
    }
}