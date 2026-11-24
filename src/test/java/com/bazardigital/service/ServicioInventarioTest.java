package com.bazardigital.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.bazardigital.domain.model.Categoria;
import com.bazardigital.domain.factory.FabricaProductoFisico;
import com.bazardigital.domain.model.Producto;
import com.bazardigital.repository.InMemoryProductoRepository;

class ServicioInventarioTest {
    @Test
    void administraStockYListaProductosAgotados() {
        Producto producto = new FabricaProductoFisico(Categoria.ROPA)
                .crearProducto("INV-1", "Camisa", "Camisa", 100, 3);
        InMemoryProductoRepository productos = new InMemoryProductoRepository();
        productos.save(producto);
        ServicioInventario inventario = new ServicioInventario(productos);

        inventario.disminuirStock(producto.getId(), 3);
        assertEquals(1, inventario.listarProductosAgotados().size());
        inventario.aumentarStock(producto.getId(), 2);
        assertEquals(2, producto.getStock());
        assertThrows(IllegalArgumentException.class,
                () -> inventario.disminuirStock(producto.getId(), 3));
    }

    @Test
    void noPermiteDesbordarStockAlAumentarlo() {
        Producto producto = new FabricaProductoFisico(Categoria.ROPA)
                .crearProducto("INV-2", "Camisa", "Camisa", 100, Integer.MAX_VALUE);
        InMemoryProductoRepository productos = new InMemoryProductoRepository();
        productos.save(producto);
        ServicioInventario inventario = new ServicioInventario(productos);

        assertThrows(ArithmeticException.class,
                () -> inventario.aumentarStock(producto.getId(), 1));
        assertEquals(Integer.MAX_VALUE, producto.getStock());
    }
}
