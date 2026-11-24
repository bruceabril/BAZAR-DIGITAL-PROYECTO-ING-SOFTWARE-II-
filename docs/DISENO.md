# Diseño del sistema

## Propósito y límites

Bazar Digital es una tienda de demostración en Java que permite consultar un catálogo, usar un carrito, administrar existencias, simular pagos y generar pedidos y facturas en memoria. Ofrece interfaz de consola y una GUI sencilla con Swing.

No implementa persistencia duradera, control de usuarios, pasarelas de pago reales ni reserva o descuento de inventario durante el checkout. El stock se valida al agregar productos al carrito y se puede modificar a través del servicio de inventario.

## Arquitectura

1. **Dominio (`domain/model`)**: productos, carrito, cliente, pedido, estado del pedido, factura y modalidades de entrega. `Producto` expone cambios observables de stock; `Pedido` expone cambios observables de estado.
2. **Observadores (`domain/observer`)**: contratos `ObservadorStock` y `ObservadorPedido`, invocados sincrónicamente cuando cambia el valor observado.
3. **Servicios (`service`)**:
   - `ServicioCarrito` valida referencias, cantidades y stock al agregar artículos.
   - `ServicioInventario` consulta productos, actualiza stock y ofrece operaciones de aumento/disminución con validación.
   - `ServicioCheckout` calcula descuentos, procesa el pago, confirma el pedido, guarda pedido y factura, y vacía el carrito solo al finalizar correctamente.
4. **Repositorios (`repository`)**: interfaces para productos y pedidos con implementaciones en memoria.
5. **Interfaces (`ui/console`, `ui/gui`)**: ambas usan los mismos servicios. `BazarDigitalApp` elige consola de forma predeterminada o Swing si recibe `--gui`.

## Observer e inventario

`Producto.agregarObservadorStock` y `quitarObservadorStock` administran observadores sin duplicar la misma instancia. `actualizarStock` valida que el valor no sea negativo y notifica el stock anterior y el nuevo únicamente si hubo un cambio. Los decoradores delegan registro y actualización al producto envuelto para mantener un único origen de stock.

`ServicioInventario` permite listar productos y agotados, fijar un stock, aumentarlo o disminuirlo. Las cantidades de aumento y disminución deben ser positivas; una disminución que supere la existencia actual se rechaza. Los incrementos/decrementos son atómicos por producto y el desbordamiento de enteros se rechaza. Los observadores se asocian a productos concretos mediante su id.

`Pedido` inicia en `CREADO` y permite una transición a `CONFIRMADO` o `CANCELADO`; los estados terminales no se vuelven a cambiar. `ServicioCheckout` permite registrar observadores para nuevos pedidos. Después de que el medio de pago procesa correctamente el monto, el checkout confirma el pedido y los observadores reciben el cambio antes de guardar el pedido.

Las notificaciones son síncronas; los callbacks forman parte de la operación que causa el cambio. El checkout no reduce el stock, por lo que varios carritos simultáneos pueden reflejar existencias iguales antes de que el inventario se administre por separado.

## Patrones de diseño

- **Factory Method:** `FabricaProducto` y fábricas concretas crean productos; `CreadorPedido` delega la modalidad de entrega a sus subclases.
- **Decorator:** `ProductoDecorado` envuelve un `Producto` y agrega precio y descripción mediante extras componibles.
- **Strategy:** `EstrategiaDescuento` y `EstrategiaPago` encapsulan algoritmos intercambiables. `CalculadoraDescuentos` coordina las reglas de descuento y `ServicioCheckout` procesa la estrategia de pago seleccionada.
- **Observer:** los observadores de stock y de pedidos reaccionan a mutaciones del dominio sin ser invocados directamente por la interfaz.

## Interfaz de usuario

La consola presenta opciones para consultar catálogo, añadir productos, ver el carrito, ajustar existencias y finalizar compra. La GUI Swing expone un selector de productos, agregado al carrito, ajustes de inventario y diálogos para datos del cliente, entrega y pago.

Ambas interfaces muestran errores de validación y usan los repositorios en memoria. Los medios de pago son simulados. La autorización de tarjeta en la demo se ingresa como referencia y se marca aprobada por la interfaz; no hay comunicación con una entidad financiera.

## Ejecución y pruebas

Se requiere JDK 25 o posterior y Maven:

```bash
mvn test
mvn package
mvn compile
java -cp target/classes com.bazardigital.BazarDigitalApp
java -cp target/classes com.bazardigital.BazarDigitalApp --gui
```

La suite JUnit 5 cubre fábricas, decoradores, descuentos, pagos, repositorios, checkout, inventario y notificaciones Observer.

## Diagramas

Las fuentes PlantUML en `docs/uml/` incluyen casos de uso, clases generales con relaciones de patrones, secuencia de checkout y secuencia de actualización de stock y notificación.

## Fuentes de requisitos

En `docs/` se conservan los PDFs entregados con el proyecto. El contenido de este documento refleja el código y las pruebas; los PDFs no se pudieron extraer textualmente en este entorno, por lo que no se afirma trazabilidad detallada contra su contenido.
