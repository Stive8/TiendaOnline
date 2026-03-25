# Backend — Documentación para el Frontend

## Roles de Usuario

- **CUSTOMER**: asignado automáticamente al registrarse
- **ADMIN**: permisos completos de gestión

---

## Mapa completo de Endpoints

### Autenticación (público)

```
POST /api/auth/register
Body: { "username", "email", "password", "phoneNumbers" }
Response: { token, type, userId, username, email, role }

POST /api/auth/login
Body: { "email", "password" }
Response: { token, type, userId, username, email, role }
```

> El `role` devuelto será `"CUSTOMER"` o `"ADMIN"`. Guardarlo en localStorage.

---

### Categorías

```
GET  /api/categories              → público, sin token
POST /api/categories              → solo ADMIN
DELETE /api/categories/{id}       → solo ADMIN
```

**GET /api/categories — Response:**
```json
[
  { "id": 1, "name": "Camiseta" },
  { "id": 2, "name": "Gorra" },
  { "id": 3, "name": "Llavero" }
]
```

**POST /api/categories — Body:**
```json
{ "name": "Bufanda" }
```

> IMPORTANTE: Las categorías NO son texto libre. El frontend debe:
> 1. Llamar `GET /api/categories` para obtener la lista
> 2. Mostrar un dropdown con esas opciones
> 3. Tener un botón "Agregar categoría" (solo visible para ADMIN) que llame `POST /api/categories`
> 4. Nunca permitir escribir la categoría a mano

---

### Atributos y Valores

Los atributos estandarizan los valores flexibles de las variantes (talla, color, tipo, etc.).
Nunca son texto libre — siempre se seleccionan de listas predefinidas.

```
GET    /api/attributes                        → público, lista todos los atributos
POST   /api/attributes                        → solo ADMIN, crear atributo
DELETE /api/attributes/{id}                   → solo ADMIN

GET    /api/attributes/{attributeId}/values   → público, lista valores de un atributo
POST   /api/attributes/{attributeId}/values   → solo ADMIN, agregar valor
DELETE /api/attributes/{attributeId}/values/{valueId} → solo ADMIN
```

**GET /api/attributes — Response:**
```json
[
  {
    "id": 1,
    "name": "talla",
    "values": [
      { "id": 1, "value": "S" },
      { "id": 2, "value": "M" },
      { "id": 3, "value": "L" },
      { "id": 4, "value": "XL" }
    ]
  },
  {
    "id": 2,
    "name": "color",
    "values": [
      { "id": 5, "value": "Azul" },
      { "id": 6, "value": "Rojo" }
    ]
  }
]
```

**POST /api/attributes — Body:**
```json
{ "name": "material" }
```

**POST /api/attributes/{attributeId}/values — Body:**
```json
{ "value": "XL" }
```

**Flujo en el formulario de crear variante:**
```
1. GET /api/attributes → obtener todos los atributos con sus valores
2. Por cada atributo, mostrar un dropdown con sus valores
3. Admin selecciona los que aplican a esa variante
4. Si falta un atributo → botón "Agregar atributo" → POST /api/attributes
5. Si falta un valor → botón "Agregar valor" → POST /api/attributes/{id}/values
6. Al guardar, los atributos seleccionados se envían como Map en la variante:
```
```json
"attributes": {
  "talla": "M",
  "color": "Azul",
  "tipo": "Local"
}
```

> IMPORTANTE: Los valores del mapa `attributes` deben coincidir EXACTAMENTE con los valores
> registrados en la tabla `attribute_values`. El frontend debe tomar el valor del dropdown,
> nunca permitir escritura libre en estos campos.

---

### Productos

```
GET  /api/products                → público
GET  /api/products/{id}           → público
GET  /api/products/category/{id}  → público (buscar por categoryId)
GET  /api/products/search         → público (?attributeName=talla&attributeValue=M)
GET  /api/products/search/advanced → público (?category=1&attributeName=talla&attributeValue=M)
POST /api/products                → solo ADMIN
DELETE /api/products/{id}         → solo ADMIN
```

**GET /api/products/{id} — Response:**
```json
{
  "id": 1,
  "name": "Camiseta Barcelona",
  "categoryId": 1,
  "categoryName": "Camiseta",
  "description": "Camiseta oficial del Barcelona FC",
  "variants": [
    {
      "id": 1,
      "price": 89.99,
      "stock": 15,
      "imageUrl": "https://xxxx.supabase.co/storage/v1/object/public/productos/uuid.jpg",
      "attributes": {
        "año": "2024",
        "talla": "M",
        "tipo": "Local"
      }
    }
  ]
}
```

**POST /api/products — Body:**
```json
{
  "name": "Camiseta Barcelona",
  "categoryId": 1,
  "description": "Camiseta oficial del Barcelona FC",
  "variants": [
    {
      "price": 89.99,
      "stock": 15,
      "imageUrl": "https://xxxx.supabase.co/storage/v1/object/public/productos/uuid.jpg",
      "attributes": {
        "año": "2024",
        "talla": "M",
        "tipo": "Local"
      }
    }
  ]
}
```

> IMPORTANTE: Se envía `categoryId` (número), NO el nombre de la categoría como texto.

---

### Imágenes — Supabase Storage

Las imágenes se almacenan en Supabase Storage. El backend las sube y devuelve la URL pública.

```
POST /api/images/upload   → solo ADMIN
Content-Type: multipart/form-data
form-data: file = [archivo]
```

**Response:**
```json
{ "imageUrl": "https://xxxx.supabase.co/storage/v1/object/public/productos/uuid-nombre.jpg" }
```

**Flujo para crear un producto con imagen:**
```
1. Admin selecciona imagen → POST /api/images/upload
2. Backend sube a Supabase → devuelve { imageUrl }
3. Frontend guarda esa imageUrl
4. Admin completa el formulario → POST /api/products (incluyendo imageUrl en cada variante)
```

> Son dos llamadas separadas. Primero la imagen, luego el producto.

---

### Variantes — Atributos estandarizados

Las variantes usan un mapa `attributes` flexible, pero los valores deben venir
siempre de la tabla `attribute_values`. No hay campos obligatorios — cada producto
tiene los atributos que le aplican:

```json
Camiseta:  { "talla": "M", "año": "2024", "tipo": "Local" }
Gorra:     { "color": "Azul", "tipo": "Snapback" }
Llavero:   { "material": "Metal", "color": "Dorado" }
Bufanda:   { "color": "Rojo y Azul" }
```

> El frontend NO debe asumir que siempre habrá "talla". Renderizar dinámicamente
> los atributos que tenga cada variante.
> Los valores SIEMPRE deben venir de `GET /api/attributes`, nunca de texto libre.

---

### Carrito (requiere autenticación)

```
POST   /api/cart/items          → agregar item
GET    /api/cart                → ver carrito
PUT    /api/cart/items/{id}     → actualizar cantidad
DELETE /api/cart/items/{id}     → eliminar item
```

---

### Órdenes (requiere autenticación)

```
POST /api/orders                → crear orden
GET  /api/orders                → mis órdenes
GET  /api/orders/{id}           → detalle de orden
```

---

### Usuarios (requiere autenticación)

```
POST /api/users/me/addresses         → agregar dirección
PUT  /api/users/{id}/promote-admin   → solo ADMIN, promover a admin
```

---

## Reglas de Autenticación

Incluir el token en todas las peticiones protegidas:
```javascript
headers: {
  'Authorization': `Bearer ${localStorage.getItem('token')}`,
  'Content-Type': 'application/json'
}
```

Manejar errores:
- `401 Unauthorized` → token expirado o inválido → redirigir al login
- `403 Forbidden` → no tiene el rol necesario → mostrar mensaje de acceso denegado

---

## Qué ve cada rol en el frontend

**CUSTOMER:**
- Catálogo de productos
- Detalle de producto con variantes e imágenes
- Carrito de compras
- Mis órdenes
- Mi perfil y direcciones

**ADMIN (todo lo anterior +):**
- Botón "Agregar producto" → formulario con subida de imagen
- Botón "Eliminar producto"
- Botón "Agregar categoría" → campo de texto + llamada a POST /api/categories
- Panel de gestión de órdenes
- Botón "Promover a admin" en gestión de usuarios

---

## Cómo crear el primer Admin

```sql
UPDATE users SET rol = 'ADMIN' WHERE email = 'tu-email@example.com';
```

O usando el endpoint (requiere ya tener un admin):
```
PUT /api/users/{id}/promote-admin
Authorization: Bearer {token-de-admin}
```
