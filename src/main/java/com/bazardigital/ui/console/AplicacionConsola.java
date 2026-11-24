package com.bazardigital.ui.console;

import java.util.List;
import java.util.Scanner;

import com.bazardigital.domain.model.Carrito;
import com.bazardigital.domain.model.Cliente;
import com.bazardigital.domain.model.EntregaDigital;
import com.bazardigital.domain.model.EntregaDomicilio;
import com.bazardigital.domain.model.EntregaRetiro;
import com.bazardigital.domain.model.ModalidadEntrega;
import com.bazardigital.domain.model.Producto;
import com.bazardigital.domain.observer.ObservadorStock;
import com.bazardigital.domain.strategy.pago.AutorizacionTarjeta;
import com.bazardigital.domain.strategy.pago.DatosPago;
import com.bazardigital.domain.strategy.pago.EstrategiaPago;
import com.bazardigital.domain.strategy.pago.PagoContraEntrega;
import com.bazardigital.domain.strategy.pago.PagoTarjeta;
import com.bazardigital.domain.strategy.pago.PagoTransferencia;
import com.bazardigital.domain.strategy.pago.ReferenciaTransferencia;
import com.bazardigital.repository.InMemoryPedidoRepository;
import com.bazardigital.repository.InMemoryProductoRepository;
import com.bazardigital.service.ServicioCarrito;
import com.bazardigital.service.ServicioCheckout;
import com.bazardigital.service.ServicioInventario;

public final class AplicacionConsola {
    private AplicacionConsola() {
    }

    public static void iniciar() {
        InMemoryProductoRepository productos = new InMemoryProductoRepository();
        ServicioInventario inventario = new ServicioInventario(productos);
        ServicioCarrito servicioCarrito = new ServicioCarrito(productos);
        ServicioCheckout checkout = new ServicioCheckout(new InMemoryPedidoRepository());
        Carrito carrito = new Carrito();
        Scanner entrada = new Scanner(System.in);

        ObservadorStock observadorStock = (producto, anterior, nuevo) ->
                System.out.printf("Aviso de inventario: %s cambió de %d a %d unidades.%n",
                        producto.getNombre(), anterior, nuevo);
        productos.findAll().forEach(producto ->
                inventario.agregarObservador(producto.getId(), observadorStock));
        checkout.agregarObservadorPedido((pedido, anterior, nuevo) ->
                System.out.printf("Pedido %s: %s -> %s.%n", pedido.getId(), anterior, nuevo));

        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== Bazar Digital ===");
            System.out.println("1. Ver catalogo");
            System.out.println("2. Agregar producto al carrito");
            System.out.println("3. Ver carrito");
            System.out.println("4. Ajustar inventario");
            System.out.println("5. Finalizar compra");
            System.out.println("0. Salir");
            String opcion = leer(entrada, "Opcion: ");
            try {
                switch (opcion) {
                    case "1" -> mostrarCatalogo(inventario.listarProductos());
                    case "2" -> agregarAlCarrito(entrada, productos, servicioCarrito, carrito);
                    case "3" -> mostrarCarrito(carrito);
                    case "4" -> ajustarInventario(entrada, inventario);
                    case "5" -> finalizarCompra(entrada, checkout, carrito);
                    case "0" -> continuar = false;
                    default -> System.out.println("Opcion no valida.");
                }
            } catch (RuntimeException error) {
                System.out.println("No se pudo completar la operacion: " + error.getMessage());
            }
        }
        System.out.println("Gracias por visitar Bazar Digital.");
    }

    private static void mostrarCatalogo(List<Producto> productos) {
        System.out.println("\n--- Catalogo ---");
        productos.forEach(producto -> System.out.printf("%s | %s | $%.2f | stock: %d%n",
                producto.getId(), producto.getNombre(), producto.getPrecio(), producto.getStock()));
    }

    private static void agregarAlCarrito(Scanner entrada, InMemoryProductoRepository productos,
                                        ServicioCarrito servicio, Carrito carrito) {
        mostrarCatalogo(productos.findAll());
        String id = leer(entrada, "Id del producto: ");
        int cantidad = Integer.parseInt(leer(entrada, "Cantidad: "));
        servicio.agregarProducto(carrito, id, cantidad);
        System.out.println("Producto agregado.");
    }

    private static void mostrarCarrito(Carrito carrito) {
        System.out.println("\n--- Carrito ---");
        carrito.getLineas().forEach(linea -> System.out.printf("%s x%d | $%.2f%n",
                linea.getProducto().getNombre(), linea.getCantidad(), linea.calcularSubtotal()));
        System.out.printf("Subtotal: $%.2f%n", carrito.calcularSubtotal());
    }

    private static void ajustarInventario(Scanner entrada, ServicioInventario inventario) {
        String id = leer(entrada, "Id del producto: ");
        int nuevoStock = Integer.parseInt(leer(entrada, "Nuevo stock: "));
        inventario.actualizarStock(id, nuevoStock);
        System.out.println("Inventario actualizado.");
    }

    private static void finalizarCompra(Scanner entrada, ServicioCheckout checkout, Carrito carrito) {
        String nombre = leer(entrada, "Nombre del cliente: ");
        String correo = leer(entrada, "Correo: ");
        ModalidadEntrega entrega = elegirEntrega(entrada);
        EstrategiaPago pago = elegirPago(entrada);
        DatosPago datos = switch (pago.getNombre()) {
            case "Tarjeta" -> new AutorizacionTarjeta(
                    leer(entrada, "Codigo de autorizacion simulado: "), true);
            case "Transferencia" -> new ReferenciaTransferencia(
                    leer(entrada, "Referencia de transferencia: "));
            default -> null;
        };
        var factura = checkout.cerrarPedido(new Cliente("CLI-" + System.currentTimeMillis(),
                nombre, correo), carrito, entrega, List.of(), pago, datos);
        System.out.printf("Compra confirmada. Factura %s, total $%.2f, pago: %s.%n",
                factura.getId(), factura.getTotal(), factura.getPago().getMedioPago());
    }

    private static ModalidadEntrega elegirEntrega(Scanner entrada) {
        System.out.println("Entrega: 1) Domicilio 2) Retiro 3) Digital");
        return switch (leer(entrada, "Opcion: ")) {
            case "1" -> new EntregaDomicilio();
            case "2" -> new EntregaRetiro();
            case "3" -> new EntregaDigital();
            default -> throw new IllegalArgumentException("La modalidad de entrega no es valida");
        };
    }

    private static EstrategiaPago elegirPago(Scanner entrada) {
        System.out.println("Pago: 1) Tarjeta 2) Transferencia 3) Contra entrega");
        return switch (leer(entrada, "Opcion: ")) {
            case "1" -> new PagoTarjeta();
            case "2" -> new PagoTransferencia();
            case "3" -> new PagoContraEntrega();
            default -> throw new IllegalArgumentException("El medio de pago no es valido");
        };
    }

    private static String leer(Scanner entrada, String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }
}
