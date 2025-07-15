package upc.backend.opensource.controller;

import upc.backend.opensource.model.*;
import upc.backend.opensource.payload.request.CartItemRequest;
import upc.backend.opensource.payload.response.DetallePedidoResponse;
import upc.backend.opensource.payload.response.MessageResponse;
import upc.backend.opensource.payload.response.PedidoResponse;
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
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @PostMapping
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
            detalle.setPrecio(producto.getPrecio());

            pedido.getDetalles().add(detalle);
            totalPedido += (producto.getPrecio() * itemRequest.getQuantity());
        }

        pedido.setTotal(totalPedido);

        // 4. Guardar el pedido
        pedidoRepository.save(pedido);

        return ResponseEntity.ok(new MessageResponse("¡Pedido creado exitosamente!"));
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<PedidoResponse>> getMyOrders() {
        // 1. Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));

        // 2. Buscar los pedidos en la base de datos
        List<Pedido> pedidos = pedidoRepository.findByUserOrderByFechaCreacionDesc(currentUser);

        // 3. Convertir las entidades a DTOs
        List<PedidoResponse> pedidoResponses = pedidos.stream().map(pedido -> {
            PedidoResponse res = new PedidoResponse();
            res.setId(pedido.getId());
            res.setFechaCreacion(pedido.getFechaCreacion());
            res.setTotal(pedido.getTotal());

            List<DetallePedidoResponse> detalleResponses = pedido.getDetalles().stream().map(detalle -> {
                DetallePedidoResponse detRes = new DetallePedidoResponse();
                detRes.setProductoNombre(detalle.getProducto().getNombre());
                detRes.setCantidad(detalle.getCantidad());
                detRes.setPrecio(detalle.getPrecio());
                detRes.setProductoImagenUrl(detalle.getProducto().getImagenUrl());
                return detRes;
            }).collect(Collectors.toList());

            res.setDetalles(detalleResponses);
            return res;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(pedidoResponses);
    }
}