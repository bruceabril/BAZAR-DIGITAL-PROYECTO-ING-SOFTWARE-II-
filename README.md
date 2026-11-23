# Bazar Digital — Proyecto de Patrones de Diseño (Ingeniería de Software II)

Sistema de tienda en línea desarrollado en **Java** aplicando patrones de diseño, con **interfaz por consola** y **interfaz gráfica sencilla** construidas sobre el mismo núcleo de negocio.

## Integrantes
- Bruce Adrian Abril Ramírez
- Santiago Caballero Díaz
- Joseph David Gómez Argote

## Alcance y funcionalidades principales
- **Catálogo multicategoría:** Productos de categorías como ropa, tecnología y alimentos con atributos propios.
- **Tipos de productos y modalidades:** Soporte para distintos formatos/modalidades (por ejemplo, producto físico, producto digital, combo y modalidades de entrega).
- **Carrito y extras:** Cálculo dinámico de extras sobre productos (garantía extendida, empaque de regalo, envío express) sin multiplicar subclases.
- **Descuentos y pagos:** Reglas intercambiables para descuentos (cantidad, temporada, cliente frecuente, cupones, con evaluación de acumulabilidad) y simulación de medios de pago (tarjeta, transferencia, contra entrega).
- 
- **Alertas de stock y pedidos:** Notificaciones automáticas cuando un producto agotado vuelve a tener existencia o cambia el estado de un pedido, junto con historial y generación de factura.
- **Fuera de alcance:** Pasarelas de pago reales (Wompi, PayU, PSE), rastreo GPS en tiempo real y panel multiusuario con RBAC avanzado.

## Patrones de diseño aplicados
1. **Decorator (Estructural):** Agrega extras opcionales a los elementos del carrito modificando precio y descripción en tiempo de ejecución.
2. **Strategy (Comportamiento):** Encapsula reglas intercambiables de descuentos acumulables y validaciones/cobros por medio de pago.
3. **Factory Method (Creacional):** Desacopla la creación de distintos tipos de productos o modalidades de pedido de la lógica que los consume.
4. **Observer (Comportamiento):** Notifica automáticamente a los módulos interesados cuando cambia el stock o el estado de un pedido.

## Estructura del repositorio
- `src/main/java/com/bazardigital/domain/model`: Entidades y modelos del dominio.
- `src/main/java/com/bazardigital/domain/factory`: Implementación del patrón *Factory Method*.
- `src/main/java/com/bazardigital/domain/decorator`: Implementación del patrón *Decorator* para extras.
- `src/main/java/com/bazardigital/domain/strategy/descuento` y `pago`: Implementación del patrón *Strategy*.
- `src/main/java/com/bazardigital/domain/observer`: Implementación del patrón *Observer*.
- `src/main/java/com/bazardigital/repository`: Repositorios y manejo de datos/persistencia en memoria.
- `src/main/java/com/bazardigital/service`: Casos de uso y orquestación del núcleo de negocio.
- `src/main/java/com/bazardigital/ui/console`: Interfaz por consola.
- `src/main/java/com/bazardigital/ui/gui`: Interfaz gráfica sencilla.
- `src/test/java/com/bazardigital`: Pruebas unitarias asociadas a las clases donde se aplican los patrones.
- `docs/uml`: Diagramas UML y documentación de diseño del proyecto.

## Guía detallada de carpetas

- **`docs/uml/`**: Aquí se guardan los diagramas UML obligatorios del entregable (casos de uso, diagrama de clases general, un diagrama de clases por cada patrón con sus roles marcados sobre las clases reales y al menos un diagrama de secuencia del flujo principal), junto con el documento de diseño.
- **`src/main/java/com/bazardigital/domain/model/`**: Contiene las entidades principales del negocio (como productos físicos, digitales o combos, categorías, clientes, carrito, pedidos, estados y facturas). No debe tener código de consola ni de interfaz gráfica.
- **`src/main/java/com/bazardigital/domain/factory/`**: Implementa el patrón **Factory Method** (creacional) para construir los distintos tipos de productos o modalidades sin esparcir `new` ni condicionales por todo el proyecto.
- **`src/main/java/com/bazardigital/domain/decorator/`**: Implementa el patrón **Decorator** (estructural) para agregar extras opcionales (como garantía extendida, empaque de regalo o envío express) modificando precio y descripción sin multiplicar subclases.
- **`src/main/java/com/bazardigital/domain/strategy/descuento/`**: Implementa el patrón **Strategy** (comportamiento) para las reglas de descuento (cantidad, temporada, cliente frecuente, cupones y lógica de acumulabilidad).
- **`src/main/java/com/bazardigital/domain/strategy/pago/`**: Implementa el patrón **Strategy** (comportamiento) para simular los medios de pago (tarjeta, transferencia, contra entrega) con las validaciones propias de cada uno.
- **`src/main/java/com/bazardigital/domain/observer/`**: Implementa el patrón **Observer** (comportamiento) para que los módulos interesados reaccionen automáticamente cuando cambia el stock (por ejemplo, aviso al reabastecer un producto agotado) o el estado de un pedido.
- **`src/main/java/com/bazardigital/repository/`**: Gestiona el almacenamiento y consulta de datos (catálogo, inventario, historial de pedidos y facturas) manteniendo la consistencia del stock.
- **`src/main/java/com/bazardigital/service/`**: Coordina los casos de uso del sistema (catálogo, carrito, cierre de pedido/facturación e inventario) conectando el dominio con los repositorios.
- **`src/main/java/com/bazardigital/ui/console/`**: Contiene la interfaz por consola; solo captura entradas y muestra resultados usando los servicios del núcleo.
- **`src/main/java/com/bazardigital/ui/gui/`**: Contiene la interfaz gráfica sencilla (por ejemplo con Swing) sobre el mismo núcleo de negocio, mostrando con claridad extras, descuentos y totales.
- **`src/test/java/com/bazardigital/`**: Reúne las pruebas unitarias (JUnit 5) para validar las clases donde se aplica cada patrón de diseño.

## Carrito, descuentos y pago
- `ServicioCarrito` valida productos y cantidades contra el catálogo; los extras se aplican envolviendo el producto con `GarantiaExtendida`, `EmpaqueRegalo` o `EnvioExpress`. Cada extra recibe su costo al construirse.
- Las estrategias `DescuentoPorCantidad`, `DescuentoTemporada`, `DescuentoClienteFrecuente` y `DescuentoCupon` reciben porcentajes configurables. Los descuentos acumulables se suman sobre el subtotal original; si hay descuentos no acumulables, se compara el mejor individual con el total acumulable. El descuento final nunca supera el subtotal.
- `ServicioCheckout` procesa tarjeta, transferencia o contra entrega, registra el pedido en memoria y genera una `Factura`. Un pago inválido no guarda el pedido ni vacía el carrito. El checkout no modifica el inventario.

## Compilación y ejecución
1. Asegúrarse de tener **Java (JDK 25 o posterior)** y **Apache Maven** instalados.
2. Para ejecutar las pruebas unitarias:
   ```bash
   mvn test
