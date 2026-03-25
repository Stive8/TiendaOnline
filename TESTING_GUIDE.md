# 🧪 GUÍA DE PRUEBAS - TIENDA CAMISETAS

## 📋 PREREQUISITOS

Antes de empezar, asegúrate de tener:
- Base de datos corriendo (MySQL/PostgreSQL)
- Aplicación Spring Boot iniciada
- Herramienta para hacer requests (Postman, Insomnia, curl, etc.)

---

## 🔐 PASO 1: AUTENTICACIÓN (Si ya tienes implementado)

### Registrar Usuario
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "firstName": "Test",
  "lastName": "User"
}
```

### Login
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "testuser"
}
```

**⚠️ IMPORTANTE:** Guarda el token, lo necesitarás en todas las siguientes peticiones.

**Header para requests autenticados:**
```
Authorization: Bearer {tu-token-aqui}
```

---

## 🛍️ PASO 2: CREAR PRODUCTOS (ADMIN)

### 2.1 Crear Producto con Variantes
```http
POST http://localhost:8080/api/products
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "name": "Camiseta Básica",
  "category": "Camisetas",
  "description": "Camiseta de algodón 100%",
  "variants": [
    {
      "stock": 50,
      "price": 29.99,
      "attributes": {
        "size": "S",
        "color": "Rojo"
      }
    },
    {
      "stock": 30,
      "price": 29.99,
      "attributes": {
        "size": "M",
        "color": "Rojo"
      }
    },
    {
      "stock": 20,
      "price": 29.99,
      "attributes": {
        "size": "L",
        "color": "Azul"
      }
    }
  ]
}
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "name": "Camiseta Básica",
  "category": "Camisetas",
  "description": "Camiseta de algodón 100%",
  "variants": [...]
}
```

**📝 Nota:** Guarda los IDs de las variantes (variant IDs) para los siguientes pasos.

### 2.2 Ver Productos
```http
GET http://localhost:8080/api/products
```

### 2.3 Ver Producto por ID
```http
GET http://localhost:8080/api/products/1
```

---

## 🛒 PASO 3: CARRITO DE COMPRAS

### 3.1 Crear Carrito
```http
POST http://localhost:8080/api/cart
Authorization: Bearer {token}
```

**Respuesta esperada:**
```json
{
  "id": 1
}
```

**📝 Nota:** Guarda el cartId.

### 3.2 Agregar Producto al Carrito
```http
POST http://localhost:8080/api/cart/1/items
Authorization: Bearer {token}
Content-Type: application/json

{
  "variantId": 1,
  "quantity": 2
}
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "items": [
    {
      "id": 1,
      "variantId": 1,
      "productName": "Camiseta Básica",
      "attributes": {
        "size": "S",
        "color": "Rojo"
      },
      "quantity": 2,
      "unitPrice": 29.99,
      "subtotal": 59.98
    }
  ],
  "total": 59.98
}
```

### 3.3 Agregar Más Productos
```http
POST http://localhost:8080/api/cart/1/items
Authorization: Bearer {token}
Content-Type: application/json

{
  "variantId": 2,
  "quantity": 1
}
```

### 3.4 Ver Carrito
```http
GET http://localhost:8080/api/cart/1
Authorization: Bearer {token}
```

### 3.5 Actualizar Cantidad
```http
PUT http://localhost:8080/api/cart/1/items/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "quantity": 3
}
```

### 3.6 Eliminar Item del Carrito
```http
DELETE http://localhost:8080/api/cart/1/items/1
Authorization: Bearer {token}
```

### 3.7 Limpiar Carrito Completo
```http
DELETE http://localhost:8080/api/cart/1
Authorization: Bearer {token}
```

---

## 🏠 PASO 4: CREAR DIRECCIÓN

```http
POST http://localhost:8080/api/users/me/addresses
Authorization: Bearer {token}
Content-Type: application/json

{
  "calle": "Calle Principal 123",
  "barrio": "Centro",
  "ciudad": "Bogotá",
  "departamento": "Cundinamarca",
  "codigoPostal": "110111"
}
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "calle": "Calle Principal 123",
  "barrio": "Centro",
  "ciudad": "Bogotá",
  "departamento": "Cundinamarca",
  "codigoPostal": "110111"
}
```

**📝 Nota:** Guarda el addressId.

---

## 📦 PASO 5: CREAR ORDEN

### 5.1 Crear Orden desde Carrito
```http
POST http://localhost:8080/api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "cartId": 1,
  "addressId": 1,
  "idempotencyKey": "order-550e8400-e29b-41d4-a716-446655440000"
}
```

**⚠️ IMPORTANTE:** El `idempotencyKey` debe ser único por cada intento de crear orden. Usa un UUID.

**Respuesta esperada:**
```json
{
  "id": 1,
  "createdAt": "2024-01-15T10:30:00",
  "status": "CREATED",
  "total": 89.97,
  "items": [
    {
      "id": 1,
      "productName": "Camiseta Básica",
      "attributes": "size: S, color: Rojo",
      "quantity": 2,
      "unitPrice": 29.99,
      "subtotal": 59.98
    },
    {
      "id": 2,
      "productName": "Camiseta Básica",
      "attributes": "size: M, color: Rojo",
      "quantity": 1,
      "unitPrice": 29.99,
      "subtotal": 29.99
    }
  ],
  "shippingAddress": "Calle Principal 123, Centro, Bogotá, Cundinamarca"
}
```

**📝 Nota:** Guarda el orderId.

### 5.2 Probar Idempotencia (Doble Clic)
Ejecuta la misma petición de nuevo con el MISMO idempotencyKey:

```http
POST http://localhost:8080/api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "cartId": 1,
  "addressId": 1,
  "idempotencyKey": "order-550e8400-e29b-41d4-a716-446655440000"
}
```

**✅ Resultado esperado:** Debe retornar la MISMA orden sin crear una nueva.

### 5.3 Ver Orden
```http
GET http://localhost:8080/api/orders/1
Authorization: Bearer {token}
```

### 5.4 Ver Mis Órdenes
```http
GET http://localhost:8080/api/orders/my-orders?page=0&size=10
Authorization: Bearer {token}
```

---

## 💳 PASO 6: PROCESAR PAGO

### 6.1 Pagar Orden
```http
POST http://localhost:8080/api/payments/process
Authorization: Bearer {token}
Content-Type: application/json

{
  "orderId": 1,
  "paymentMethod": "CREDIT_CARD",
  "idempotencyKey": "payment-660e8400-e29b-41d4-a716-446655440001"
}
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "orderId": 1,
  "amount": 89.97,
  "paymentMethod": "CREDIT_CARD",
  "status": "COMPLETED",
  "transactionId": "TXN-A1B2C3D4",
  "paymentDate": "2024-01-15T10:35:00"
}
```

### 6.2 Probar Idempotencia de Pago
Ejecuta la misma petición con el MISMO idempotencyKey:

```http
POST http://localhost:8080/api/payments/process
Authorization: Bearer {token}
Content-Type: application/json

{
  "orderId": 1,
  "paymentMethod": "CREDIT_CARD",
  "idempotencyKey": "payment-660e8400-e29b-41d4-a716-446655440001"
}
```

**✅ Resultado esperado:** Debe retornar el MISMO pago sin cobrar de nuevo.

### 6.3 Verificar Estado de Orden
```http
GET http://localhost:8080/api/orders/1
Authorization: Bearer {token}
```

**✅ Resultado esperado:** El status debe ser "PAID".

---

## 🚚 PASO 7: CREAR ENVÍO (ADMIN)

### 7.1 Crear Envío
```http
POST http://localhost:8080/api/shipments
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "orderId": 1,
  "carrier": "FedEx",
  "estimatedDeliveryDate": "2024-01-20T18:00:00"
}
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "orderId": 1,
  "trackingNumber": "TRK-A1B2C3D4E5F6",
  "status": "PENDING",
  "carrier": "FedEx",
  "shippingAddress": "Calle Principal 123, Centro, Bogotá, Cundinamarca",
  "estimatedDeliveryDate": "2024-01-20T18:00:00",
  "actualDeliveryDate": null,
  "createdAt": "2024-01-15T10:40:00",
  "updatedAt": "2024-01-15T10:40:00"
}
```

**📝 Nota:** Guarda el shipmentId y trackingNumber.

### 7.2 Verificar Estado de Orden
```http
GET http://localhost:8080/api/orders/1
Authorization: Bearer {token}
```

**✅ Resultado esperado:** El status debe ser "SHIPPED".

---

## 📍 PASO 8: ACTUALIZAR ESTADO DE ENVÍO (ADMIN)

### 8.1 Marcar como Despachado
```http
PUT http://localhost:8080/api/shipments/1/status
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "status": "DISPATCHED"
}
```

### 8.2 Marcar como En Tránsito
```http
PUT http://localhost:8080/api/shipments/1/status
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "status": "IN_TRANSIT"
}
```

### 8.3 Marcar como En Reparto
```http
PUT http://localhost:8080/api/shipments/1/status
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "status": "OUT_FOR_DELIVERY"
}
```

### 8.4 Marcar como Entregado
```http
PUT http://localhost:8080/api/shipments/1/status
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "status": "DELIVERED"
}
```

**✅ Resultado esperado:** 
- El shipment debe tener `actualDeliveryDate` con la fecha actual
- La orden debe cambiar a estado "DELIVERED"

### 8.5 Verificar Orden Final
```http
GET http://localhost:8080/api/orders/1
Authorization: Bearer {token}
```

**✅ Resultado esperado:** El status debe ser "DELIVERED".

---

## 🔍 PASO 9: RASTREAR ENVÍO (PÚBLICO)

### 9.1 Rastrear por Tracking Number
```http
GET http://localhost:8080/api/shipments/tracking/TRK-A1B2C3D4E5F6
```

**✅ No requiere autenticación** - Cualquiera puede rastrear con el tracking number.

### 9.2 Ver Envío por Orden
```http
GET http://localhost:8080/api/shipments/order/1
Authorization: Bearer {token}
```

---

## 🧪 PASO 10: PRUEBAS DE VALIDACIÓN

### 10.1 Intentar Pagar Orden Ya Pagada
```http
POST http://localhost:8080/api/payments/process
Authorization: Bearer {token}
Content-Type: application/json

{
  "orderId": 1,
  "paymentMethod": "CREDIT_CARD",
  "idempotencyKey": "payment-NEW-KEY-12345"
}
```

**❌ Resultado esperado:** Error 409 - "Order must be in CREATED status to process payment"

### 10.2 Intentar Crear Envío para Orden No Pagada
Primero crea una nueva orden sin pagar, luego:

```http
POST http://localhost:8080/api/shipments
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "orderId": 2,
  "carrier": "DHL",
  "estimatedDeliveryDate": "2024-01-25T18:00:00"
}
```

**❌ Resultado esperado:** Error 409 - "Order must be in PAID status to create shipment"

### 10.3 Intentar Agregar al Carrito Sin Stock
```http
POST http://localhost:8080/api/cart/1/items
Authorization: Bearer {token}
Content-Type: application/json

{
  "variantId": 1,
  "quantity": 999999
}
```

**❌ Resultado esperado:** Error 409 - "Insufficient stock. Available: X, Requested: 999999"

### 10.4 Intentar Transición de Estado Inválida
```http
PUT http://localhost:8080/api/shipments/1/status
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "status": "PENDING"
}
```

**❌ Resultado esperado:** Error 409 - "Cannot change status of DELIVERED shipment"

---

## 📊 RESUMEN DE FLUJO COMPLETO

```
1. Registrar usuario
2. Login (obtener token)
3. Crear productos (admin)
4. Crear carrito
5. Agregar productos al carrito
6. Crear dirección
7. Crear orden (con idempotencyKey) → CREATED
8. Pagar orden (con idempotencyKey) → PAID
9. Crear envío (admin) → SHIPPED
10. Actualizar estado envío (admin) → DISPATCHED → IN_TRANSIT → OUT_FOR_DELIVERY → DELIVERED
11. Orden final → DELIVERED
```

---

## 🎯 CASOS DE PRUEBA CRÍTICOS

### ✅ Idempotencia
- [ ] Crear orden 2 veces con mismo idempotencyKey → Solo 1 orden creada
- [ ] Pagar 2 veces con mismo idempotencyKey → Solo 1 pago procesado

### ✅ Concurrencia (Requiere 2 usuarios simultáneos)
- [ ] 2 usuarios intentan comprar el último producto → Solo 1 tiene éxito

### ✅ Validaciones de Estado
- [ ] No se puede pagar orden que no está en CREATED
- [ ] No se puede crear envío para orden que no está en PAID
- [ ] No se puede cambiar estado de envío entregado

### ✅ Validaciones de Stock
- [ ] No se puede agregar al carrito más de lo disponible
- [ ] No se puede crear orden si no hay stock suficiente

### ✅ Validaciones de Ownership
- [ ] Usuario no puede ver órdenes de otro usuario
- [ ] Usuario no puede pagar órdenes de otro usuario

### ✅ Snapshot de Datos
- [ ] Cambiar precio de producto después de crear orden
- [ ] Verificar que la orden mantiene el precio original

---

## 🐛 ERRORES COMUNES

### Error: 401 Unauthorized
**Causa:** Token inválido o expirado
**Solución:** Hacer login de nuevo y obtener nuevo token

### Error: 403 Forbidden
**Causa:** Usuario no tiene permisos (requiere ADMIN)
**Solución:** Usar token de usuario con rol ADMIN

### Error: 404 Not Found
**Causa:** Recurso no existe (ID inválido)
**Solución:** Verificar que el ID existe

### Error: 409 Conflict
**Causa:** Validación de negocio falló (stock, estado, etc.)
**Solución:** Revisar el mensaje de error y corregir

---

## 📝 NOTAS PARA COMMIT

```bash
git add .
git commit -m "feat: Implement complete shipment system with concurrency protection

- Add ShipmentStatus enum with 7 states
- Implement Shipment entity with versioning and snapshot
- Add pessimistic locking for shipment operations
- Implement state transition validations
- Add tracking number generation
- Sync shipment status with order status
- Create ShipmentService with full business logic
- Add ShipmentController with 5 endpoints
- Implement GlobalExceptionHandler for centralized error handling
- Add custom exceptions (ResourceNotFoundException, BusinessValidationException, InsufficientStockException)
- Optimize JPA queries with JOIN FETCH to prevent N+1 problem
- Add idempotency key to Order creation
- Add comprehensive test guide with all endpoints

Features:
- Pessimistic + Optimistic locking
- Idempotency for orders and payments
- State validation for orders, payments, and shipments
- Snapshot of shipping address
- Error handling with standardized responses
- JPA optimization with JOIN FETCH
- Ownership validation
- Audit timestamps

Tested:
- Complete order flow (create → pay → ship → deliver)
- Idempotency (double click protection)
- State transitions
- Stock validation
- Concurrent operations"
```

---

## 🚀 LISTO PARA PROBAR

Sigue los pasos en orden y verifica que cada respuesta sea la esperada. Si encuentras algún error, revisa los logs de la aplicación.

**¡Buena suerte con las pruebas!** 🎉
