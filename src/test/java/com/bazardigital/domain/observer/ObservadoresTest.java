package com.bazardigital.domain.observer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.bazardigital.domain.decorator.GarantiaExtendida;
import com.bazardigital.domain.factory.FabricaProductoFisico;
import com.bazardigital.domain.model.Carrito;
import com.bazardigital.domain.model.Categoria;
import com.bazardigital.domain.model.Cliente;
import com.bazardigital.domain.model.EntregaRetiro;
import com.bazardigital.domain.model.EstadoPedido;
import com.bazardigital.domain.model.Producto;
import com.bazardigital.domain.strategy.pago.PagoContraEntrega;
import com.bazardigital.repository.InMemoryPedidoRepository;
import com.bazardigital.repository.InMemoryProductoRepository;
import com.bazardigital.service.ServicioCarrito;
import com.bazardigital.service.ServicioCheckout;
import com.bazardigital.service.ServicioInventario;

class ObservadoresTest {
    @Test
    void observadorDeStockRecibeCambiosYNoSeInvocaSiNoHayCambio() {
        Producto producto = new FabricaProductoFisico(Categoria.ROPA)
                .crearProducto("OBS-1", "Camisa", "Camisa", 100, 2);
        InMemoryProductoRepository productos = new InMemoryProductoRepository();
        productos.save(producto);
        ServicioInventario inventario = new ServicioInventario(productos);
        List<String> cambios = new ArrayList<>();
        ObservadorStock observador = (actualizado, anterior, nuevo) ->
                cambios.add(actualizado.getId() + ":" + anterior + "->" + nuevo);
        inventario.agregarObservador(producto.getId(), observador);

        inventario.disminuirStock(producto.getId(), 2);
        inventario.actualizarStock(producto.getId(), 0);
        inventario.aumentarStock(producto.getId(), 4);

        assertEquals(4, producto.getStock());
        assertEquals(List.of("OBS-1:2->0", "OBS-1:0->4"), cambios);
    }

    @Test
    void observadorDeStockEnUnDecoradorSeNotificaDesdeElProductoBase() {
        Producto producto = new FabricaProductoFisico(Categoria.ROPA)
                .crearProducto("OBS-4", "Camisa", "Camisa", 100, 1);
        Producto decorado = new GarantiaExtendida(producto, 20);
        List<Integer> cambios = new ArrayList<>();
        decorado.agregarObservadorStock((actualizado, anterior, nuevo) -> cambios.add(nuevo));

        decorado.actualizarStock(0);

        assertEquals(0, producto.getStock());
        assertEquals(List.of(0), cambios);
    }

    @Test
    void observadorDePedidoRecibeLaConfirmacionDelCheckout() {
        Producto producto = new FabricaProductoFisico(Categoria.ROPA)
                .crearProducto("OBS-2", "Camisa", "Camisa", 100, 2);
        InMemoryProductoRepository productos = new InMemoryProductoRepository();
        productos.save(producto);
        Carrito carrito = new Carrito();
        new ServicioCarrito(productos).agregarProducto(carrito, producto.getId(), 1);
        ServicioCheckout checkout = new ServicioCheckout(new InMemoryPedidoRepository());
        List<String> estados = new ArrayList<>();
        checkout.agregarObservadorPedido((pedido, anterior, nuevo) ->
                estados.add(anterior + "->" + nuevo));

        var factura = checkout.cerrarPedido(new Cliente("C-1", "Ana", "ana@example.com"),
                carrito, new EntregaRetiro(), List.of(), new PagoContraEntrega(), null);

        assertEquals(EstadoPedido.CONFIRMADO, factura.getPedido().getEstado());
        assertEquals(List.of("CREADO->CONFIRMADO"), estados);
    }

    @Test
    void observadorPuedeDesuscribirseYTransicionesTerminalesNoSeRepiten() {
        Producto producto = new FabricaProductoFisico(Categoria.ROPA)
                .crearProducto("OBS-3", "Camisa", "Camisa", 100, 1);
        List<Integer> stocks = new ArrayList<>();
        ObservadorStock observador = (actualizado, anterior, nuevo) -> stocks.add(nuevo);
        producto.agregarObservadorStock(observador);
        producto.actualizarStock(0);
        producto.quitarObservadorStock(observador);
        producto.actualizarStock(1);
        assertEquals(List.of(0), stocks);

        InMemoryProductoRepository productos = new InMemoryProductoRepository();
        productos.save(producto);
        Carrito carrito = new Carrito();
        new ServicioCarrito(productos).agregarProducto(carrito, producto.getId(), 1);
        ServicioCheckout checkout = new ServicioCheckout(new InMemoryPedidoRepository());
        var pedido = checkout.cerrarPedido(new Cliente("C-2", "Luis", "luis@example.com"),
                carrito, new EntregaRetiro(), List.of(), new PagoContraEntrega(), null).getPedido();
        assertThrows(IllegalStateException.class, () -> pedido.cambiarEstado(EstadoPedido.CANCELADO));
    }
}
