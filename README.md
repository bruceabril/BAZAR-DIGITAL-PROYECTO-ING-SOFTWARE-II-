# Bazar Digital

Proyecto académico de Ingeniería de Software II, desarrollado en Java con Maven. Incluye un núcleo de negocio para catálogo, carrito, inventario y checkout; patrones de diseño; e interfaces sencillas por consola y Swing.

## Funcionalidades

- Catálogo en memoria con productos físicos y digitales de ejemplo.
- Carrito con validación de cantidades y stock disponible.
- Extras de producto con Decorator.
- Descuentos configurables y medios de pago simulados mediante Strategy.
- Inventario para consultar, actualizar, aumentar y disminuir existencias.
- Notificaciones Observer al cambiar el stock y al cambiar el estado de un pedido.
- Checkout con creación de pedido y factura en memoria.
- Interfaz de consola y GUI Swing sobre los mismos servicios del negocio.

El checkout no descuenta ni reserva existencias: la validación de stock se hace al agregar artículos al carrito. Los repositorios son en memoria y no se conectan a pasarelas de pago reales.

## Patrones implementados

- **Factory Method:** fábricas de productos y creadores de pedidos por modalidad de entrega.
- **Decorator:** agrega extras (garantía, empaque de regalo y envío express) a productos en tiempo de ejecución.
- **Strategy:** encapsula reglas de descuento y formas simuladas de pago.
- **Observer:** los productos notifican cambios de stock; el servicio de checkout notifica el cambio de estado del pedido al confirmarlo.

## Estructura

```text
src/main/java/com/bazardigital/
  domain/decorator/                 Extras de producto
  domain/factory/                   Fábricas de productos y pedidos
  domain/model/                     Entidades, modalidades y estado del pedido
  domain/observer/                  Observadores de stock y pedidos
  domain/strategy/descuento/        Reglas y cálculo de descuentos
  domain/strategy/pago/             Estrategias, datos y resultados de pago
  repository/                       Interfaces y repositorios en memoria
  service/                          Carrito, checkout e inventario
  ui/console/                       Interfaz por consola
  ui/gui/                           Interfaz gráfica Swing
  BazarDigitalApp.java              Punto de entrada
src/test/java/com/bazardigital/     Pruebas unitarias JUnit 5
docs/
  DISENO.md                         Arquitectura y decisiones de diseño
  uml/                              Diagramas PlantUML
```

## Requisitos

Se requiere **JDK 25 o posterior** y Apache Maven. El proyecto configura `maven.compiler.release` en 25.

```bash
mvn test
mvn package
```

## Ejecución

Inicia la interfaz de consola (predeterminada):

```bash
mvn compile
java -cp target/classes com.bazardigital.BazarDigitalApp
```

Inicia la GUI Swing:

```bash
java -cp target/classes com.bazardigital.BazarDigitalApp --gui
```

En Windows también se puede ejecutar `java -cp target\classes com.bazardigital.BazarDigitalApp --gui`.

Las interfaces utilizan el catálogo de ejemplo y mantienen pedidos/productos solo en memoria; cerrar la aplicación reinicia esos datos.

## Documentación

- [Documento de diseño](docs/DISENO.md)
- [Casos de uso](docs/uml/casos-de-uso.puml)
- [Diagrama general de clases y patrones](docs/uml/clases-generales-y-patrones.puml)
- [Secuencia de checkout](docs/uml/secuencia-checkout.puml)
- [Secuencia de inventario y notificación de stock](docs/uml/secuencia-stock.puml)

Los diagramas se mantienen como fuentes PlantUML legibles.
