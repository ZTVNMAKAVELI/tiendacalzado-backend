package upc.backend.opensource.payload.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private int quantity;
}