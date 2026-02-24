package com.bsortegon.tienda.tiendacamisetas.service.impl;

import com.bsortegon.tienda.tiendacamisetas.domain.*;
import com.bsortegon.tienda.tiendacamisetas.dto.orders.CreateOrderRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.orders.OrderResponse;
import com.bsortegon.tienda.tiendacamisetas.repository.AddressRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.CartRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.OrderRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.ProductVariantRepository;
import com.bsortegon.tienda.tiendacamisetas.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Override
    public OrderResponse createOrderFromCart(CreateOrderRequest request, User authenticatedUser) {

        // 1. Validar carrito
        Cart cart = cartRepository.findById(request.cartId())
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("El carrito debe contener al menos un item");
        }

        // 2. Validar dirección
        Address address = addressRepository.findById(request.addressId())
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        if (!address.getUser().getId().equals(authenticatedUser.getId())){
            throw new RuntimeException("La dirección no pertenece al usuario autenticado");
        }

        // 3. Crear orden
        Order order = new Order();
        order.setUser(authenticatedUser);
        order.setAddress(address);
        order.setCreatedAt(java.time.LocalDateTime.now());
        order.setStatus("PENDING");
        order.setItems(new java.util.ArrayList<>()); // Inicializar lista

        // 4. Convertir CartItems a OrderItems
        for(CartItem cartItem : cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductVariant(cartItem.getProductVariant());
            orderItem.setQuantity(cartItem.getAmount());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            order.getItems().add(orderItem);
        }

        // 5. Calcular total
        Double totalPrice = order.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();
        order.setTotal(totalPrice);

        // 6. Guardar orden
        Order savedOrder = orderRepository.save(order);

        // 7. Reducir stock
        for(OrderItem item : savedOrder.getItems()){
            ProductVariant variant = item.getProductVariant();
            Long newStock = variant.getStock() - item.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException("Stock insuficiente para: " + variant.getProduct().getName());
            }
            variant.setStock(newStock);
            productVariantRepository.save(variant);
        }

        // 8. Limpiar carrito
        cart.setStatus("COMPLETED");
        cart.getCartItems().clear();
        cartRepository.save(cart);

        // 9. Mapear a Response
        return mapToOrderResponse(savedOrder);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return mapToOrderResponse(order);
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public Page<Order> findByUser(User user, Pageable pageable) {
        return orderRepository.findByUser(user, pageable);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public OrderResponse updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return mapToOrderResponse(updatedOrder);
    }

    @Override
    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }

    // Métodos privados de mapeo
    private OrderResponse mapToOrderResponse(Order order) {
        List<com.bsortegon.tienda.tiendacamisetas.dto.orders.OrderItemResponse> items = order.getItems().stream()
                .map(this::mapToOrderItemResponse)
                .toList();

        String shippingAddress = formatAddress(order.getAddress());

        return new OrderResponse(
                order.getId(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getTotal(),
                items,
                shippingAddress
        );
    }

    private com.bsortegon.tienda.tiendacamisetas.dto.orders.OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        ProductVariant variant = item.getProductVariant();
        String attributeStr = variant.getAttribute().entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        
        return new com.bsortegon.tienda.tiendacamisetas.dto.orders.OrderItemResponse(
                item.getId(),
                variant.getProduct().getName(),
                attributeStr,
                item.getQuantity(),
                item.getUnitPrice(),
                item.getQuantity() * item.getUnitPrice()
        );
    }

    private String formatAddress(Address address) {
        return String.format("%s, %s, %s, %s",
                address.getCalle(),
                address.getBarrio(),
                address.getCiudad(),
                address.getDepartamento()
        );
    }
}
