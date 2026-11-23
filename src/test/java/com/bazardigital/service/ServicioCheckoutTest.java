package com.bazardigital.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.bazardigital.domain.factory.FabricaProductoFisico;
import com.bazardigital.domain.model.Carrito;
import com.bazardigital.domain.model.Categoria;
import com.bazardigital.domain.model.Cliente;
import com.bazardigital.domain.model.EntregaDomicilio;
import com.bazardigital.domain.model.Producto;
import com.bazardigital.domain.strategy.descuento.DescuentoTemporada;
import com.bazardigital.domain.strategy.pago.AutorizacionTarjeta;
import com.bazardigital.domain.strategy.pago.PagoContraEntrega;
import com.bazardigital.domain.strategy.pago.PagoTarjeta;
import com.bazardigital.repository.InMemoryPedidoRepository;
import com.bazardigital.repository.InMemoryProductoRepository;
import com.bazardigital.repository.PedidoRepository;

class ServicioCheckoutTest {
    @Test
    void cierreGeneraFacturaYNoModificaElStock() {
        Producto producto = new FabricaProductoFisico(Categoria.ROPA)
                .crearProducto("R200", "Camisa", "Camisa", 50000, 5);
        InMemoryProductoRepository productos = new InMemoryProductoRepository();
        productos.save(producto);
        ServicioCarrito servicioCarrito = new ServicioCarrito(productos);
        Carrito carrito = new Carrito();
        servicioCarrito.agregarProducto(carrito, producto.getId(), 2);
        PedidoRepository pedidos = new InMemoryPedidoRepository();
        ServicioCheckout checkout = new ServicioCheckout(pedidos);

        var factura = checkout.cerrarPedido(
                new Cliente("C1", "Ana", "ana@example.com"),
                carrito,
                new EntregaDomicilio(10000),
                List.of(new DescuentoTemporada(10)),
                new PagoContraEntrega(),
                null);

        assertEquals(100000, factura.getSubtotal());
        assertEquals(10000, factura.getDescuento());
        assertEquals(100000, factura.getTotal());
        assertEquals(2, pedidos.findById(factura.getPedido().getId()).orElseThrow()
                .getProductos().size());
        assertEquals(5, producto.getStock());
        assertEquals(0, carrito.getCantidadTotal());
        assertFalse(checkout.buscarFacturaPorPedido(factura.getPedido().getId()).isEmpty());
    }

    @Test
    void pagoRechazadoNoGuardaPedidoNiVacíaElCarrito() {
        Producto producto = new FabricaProductoFisico(Categoria.ROPA)
                .crearProducto("R201", "Camisa", "Camisa", 50000, 5);
        InMemoryProductoRepository productos = new InMemoryProductoRepository();
        productos.save(producto);
        Carrito carrito = new Carrito();
        new ServicioCarrito(productos).agregarProducto(carrito, producto.getId(), 1);
        PedidoRepository pedidos = new InMemoryPedidoRepository();
        ServicioCheckout checkout = new ServicioCheckout(pedidos);

        assertThrows(IllegalArgumentException.class, () -> checkout.cerrarPedido(
                new Cliente("C1", "Ana", "ana@example.com"),
                carrito,
                new EntregaDomicilio(0),
                List.of(),
                new PagoTarjeta(),
                new AutorizacionTarjeta("AUTH-1", false)));

        assertEquals(0, pedidos.findAll().size());
        assertEquals(1, carrito.getCantidadTotal());
    }
}
