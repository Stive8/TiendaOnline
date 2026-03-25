package com.bsortegon.tienda.tiendacamisetas.service.impl;

import com.bsortegon.tienda.tiendacamisetas.domain.*;
import com.bsortegon.tienda.tiendacamisetas.domain.status.OrderStatus;
import com.bsortegon.tienda.tiendacamisetas.dto.orders.CreateOrderRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.orders.OrderResponse;
import com.bsortegon.tienda.tiendacamisetas.repository.AddressRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.CartRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.OrderRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.ProductVariantRepository;
import com.bsortegon.tienda.tiendacamisetas.service.OrderService;
import jakarta.transaction.Transactional;
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

    @Transactional
    @Override
    public OrderResponse createOrderFromCart(CreateOrderRequest request, User authenticatedUser) {

        // 0. IDEMPOTENCIA: Verificar si ya existe una orden con esta idempotency key
        Optional<Order> existingOrder = orderRepository.findByIdempotencyKey(request.idempotencyKey());
        if (existingOrder.isPresent()) {
            // Ya se procesó esta orden, retornar el resultado existente
            return mapToOrderResponse(existingOrder.get());
        }

        // 1. Validar carrito
        Cart cart = cartRepository.findById(request.cartId())
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("El carrito debe contener al menos un item");
        }

        if (!"ACTIVE".equals(cart.getStatus())) {
            throw new IllegalStateException("El carrito ya fue procesado");
        }

        if (cart.getUser() != null && !cart.getUser().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("El carrito no pertenece al usuario autenticado");
        }

        // 2. Validar dirección
        Address address = addressRepository.findById(request.addressId())
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        if (!address.getUser().getId().equals(authenticatedUser.getId())){
            throw new RuntimeException("La dirección no pertenece al usuario autenticado");
        }

        // 3. Validar y bloquear stock ANTES de crear la orden
        for(CartItem cartItem : cart.getCartItems()){
            ProductVariant variant = productVariantRepository.findByIdWithLock(cartItem.getProductVariant().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            if (variant.getStock() < cartItem.getAmount()) {
                throw new RuntimeException("Stock insuficiente para: " + variant.getProduct().getName() + 
                    ". Disponible: " + variant.getStock() + ", Solicitado: " + cartItem.getAmount());
            }
        }

        // 4. Crear orden
        Order order = new Order();
        order.setUser(authenticatedUser);
        order.setAddress(address);
        order.setCreatedAt(java.time.LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);
        order.setIdempotencyKey(request.idempotencyKey());
        order.setItems(new java.util.ArrayList<>());

        // 5. Convertir CartItems a OrderItems y calcular total
        double totalPrice = 0.0;
        for(CartItem cartItem : cart.getCartItems()){
            ProductVariant variant = cartItem.getProductVariant();
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductVariant(variant);
            orderItem.setQuantity(cartItem.getAmount());
            orderItem.setUnitPrice(variant.getPrice());
            
            // SNAPSHOT: Guardar datos del producto en el momento de la compra
            orderItem.setProductName(variant.getProduct().getName());
            orderItem.setProductAttributes(new java.util.HashMap<>(variant.getAttribute()));
            
            order.getItems().add(orderItem);
            totalPrice += cartItem.getAmount() * variant.getPrice();
        }
        order.setTotal(totalPrice);

        // 6. Reducir stock
        for(OrderItem item : order.getItems()){
            ProductVariant variant = item.getProductVariant();
            variant.setStock(variant.getStock() - item.getQuantity());
            productVariantRepository.save(variant);
        }

        // 7. Guardar orden
        Order savedOrder = orderRepository.save(order);

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
        Order order = orderRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return mapToOrderResponse(order);
    }

    @Override
    public OrderResponse getOrderByIdForUser(Long id, User user) {
        Order order = orderRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Validar que la orden pertenece al usuario
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Order does not belong to authenticated user");
        }
        
        return mapToOrderResponse(order);
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUserWithDetails(user);
    }

    @Override
    public Page<OrderResponse> findByUser(User user, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUser(user, pageable);
        return orders.map(this::mapToOrderResponse);
    }

    @Override
    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    @Transactional
    @Override
    public OrderResponse updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findByIdWithLock(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        
        validateStatusTransition(order.getStatus(), newStatus);
        
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        return mapToOrderResponse(updatedOrder);
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // CREATED -> PAID, CANCELLED
        if (currentStatus == OrderStatus.CREATED) {
            if (newStatus != OrderStatus.PAID && newStatus != OrderStatus.CANCELLED) {
                throw new IllegalStateException("Can only change from CREATED to PAID or CANCELLED");
            }
        }
        // PAID -> SHIPPED, CANCELLED
        else if (currentStatus == OrderStatus.PAID) {
            if (newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELLED) {
                throw new IllegalStateException("Can only change from PAID to SHIPPED or CANCELLED");
            }
        }
        // SHIPPED -> DELIVERED
        else if (currentStatus == OrderStatus.SHIPPED) {
            if (newStatus != OrderStatus.DELIVERED) {
                throw new IllegalStateException("Can only change from SHIPPED to DELIVERED");
            }
        }
        // DELIVERED and CANCELLED are final states
        else if (currentStatus == OrderStatus.DELIVERED || currentStatus == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot change status of " + currentStatus + " order");
        }
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
                order.getStatus().name(),
                order.getTotal(),
                items,
                shippingAddress
        );
    }

    private com.bsortegon.tienda.tiendacamisetas.dto.orders.OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        // Usar snapshot de datos en lugar de la referencia al variant
        String attributeStr = item.getProductAttributes().entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        
        return new com.bsortegon.tienda.tiendacamisetas.dto.orders.OrderItemResponse(
                item.getId(),
                item.getProductName(),
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
