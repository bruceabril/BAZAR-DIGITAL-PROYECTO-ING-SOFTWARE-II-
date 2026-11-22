package com.bazardigital.domain.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.bazardigital.domain.model.Categoria;
import com.bazardigital.domain.model.Cliente;
import com.bazardigital.domain.model.ComboBazar;
import com.bazardigital.domain.model.EntregaDigital;
import com.bazardigital.domain.model.EntregaDomicilio;
import com.bazardigital.domain.model.EntregaRetiro;
import com.bazardigital.domain.model.Pedido;
import com.bazardigital.domain.model.Producto;
import com.bazardigital.domain.model.ProductoDigital;
import com.bazardigital.domain.model.ProductoFisico;

class FabricaProductoTest {
    @Test
    void fabricaDebeCrearProductoFisico() {
        FabricaProducto fabrica = new FabricaProductoFisico(Categoria.ROPA, "L", "Bazar");

        Producto producto = fabrica.crearProducto("R001", "Camisa", "Camisa de prueba",
                50000, 10);

        assertInstanceOf(ProductoFisico.class, producto);
        assertEquals(Categoria.ROPA, producto.getCategoria());
        assertEquals("L", ((ProductoFisico) producto).getTalla());
    }

    @Test
    void fabricaDebeCrearProductoDigital() {
        FabricaProducto fabrica = new FabricaProductoDigital("EPUB", "/libro.epub");

        Producto producto = fabrica.crearProducto("D001", "Ebook", "Libro digital",
                30000, 50);

        assertInstanceOf(ProductoDigital.class, producto);
        assertEquals("EPUB", ((ProductoDigital) producto).getFormato());
    }

    @Test
    void fabricaDebeCrearCombo() {
        Producto productoBase = new FabricaProductoFisico(Categoria.ROPA)
                .crearProducto("R002", "Gorra", "Gorra del bazar", 20000, 5);
        FabricaProducto fabrica = new FabricaComboBazar(List.of(productoBase));

        Producto producto = fabrica.crearProducto("C001", "Combo urbano",
                "Gorra con accesorios", 18000, 8);

        assertInstanceOf(ComboBazar.class, producto);
        assertEquals(Categoria.COMBOS, producto.getCategoria());
        assertEquals(1, ((ComboBazar) producto).getProductos().size());
    }

    @Test
    void creadoresDePedidoDebenAplicarLaModalidadCorrespondiente() {
        Cliente cliente = new Cliente("CL001", "Ana", "ana@correo.com");
        Producto producto = new FabricaProductoDigital()
                .crearProducto("D002", "Curso", "Curso virtual", 40000, 20);

        Pedido domicilio = new CreadorPedidoDomicilio()
                .crearPedido("P001", cliente, List.of(producto));
        Pedido retiro = new CreadorPedidoRetiro()
                .crearPedido("P002", cliente, List.of(producto));
        Pedido digital = new CreadorPedidoDigital()
                .crearPedido("P003", cliente, List.of(producto));

        assertInstanceOf(EntregaDomicilio.class, domicilio.getModalidadEntrega());
        assertInstanceOf(EntregaRetiro.class, retiro.getModalidadEntrega());
        assertInstanceOf(EntregaDigital.class, digital.getModalidadEntrega());
        assertTrue(domicilio.calcularTotal() > domicilio.calcularSubtotal());
    }
}
