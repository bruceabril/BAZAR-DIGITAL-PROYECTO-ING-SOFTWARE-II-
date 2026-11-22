package com.bazardigital.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bazardigital.domain.factory.FabricaProductoDigital;
import com.bazardigital.domain.factory.FabricaProductoFisico;
import com.bazardigital.domain.model.Categoria;
import com.bazardigital.domain.model.Producto;

public class InMemoryProductoRepository implements ProductoRepository {
    private final Map<String, Producto> productos = new LinkedHashMap<>();

    public InMemoryProductoRepository() {
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        save(new FabricaProductoFisico(Categoria.ROPA, "M", "Bazar")
                .crearProducto("R001", "Camisa basica", "Camisa de algodon", 55000, 20));
        save(new FabricaProductoFisico(Categoria.TECNOLOGIA, "", "Nova")
                .crearProducto("T001", "Audifonos Bluetooth", "Audifonos con cancelacion de ruido",
                        180000, 15));
        save(new FabricaProductoFisico(Categoria.ALIMENTOS)
                .crearProducto("A001", "Caja de chocolates", "Chocolate artesanal",
                        42000, 30));
        save(new FabricaProductoDigital("PDF", "/descargas/manual-bazar.pdf")
                .crearProducto("D001", "Manual de emprendimiento", "Guia digital para emprender",
                        35000, 100));
    }

    @Override
    public List<Producto> findAll() {
        return new ArrayList<>(productos.values());
    }

    @Override
    public List<Producto> findByCategoria(Categoria categoria) {
        return productos.values().stream()
                .filter(producto -> producto.getCategoria() == categoria)
                .toList();
    }

    @Override
    public Optional<Producto> findById(String id) {
        return Optional.ofNullable(productos.get(id));
    }

    @Override
    public void save(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        productos.put(producto.getId(), producto);
    }
}
