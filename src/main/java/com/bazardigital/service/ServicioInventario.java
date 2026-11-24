package com.bazardigital.service;

import java.util.List;
import java.util.Objects;

import com.bazardigital.domain.model.Producto;
import com.bazardigital.domain.observer.ObservadorStock;
import com.bazardigital.repository.ProductoRepository;

public class ServicioInventario {
    private final ProductoRepository productoRepository;

    public ServicioInventario(ProductoRepository productoRepository) {
        this.productoRepository = Objects.requireNonNull(productoRepository);
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarProductosAgotados() {
        return productoRepository.findAll().stream()
                .filter(producto -> producto.getStock() == 0)
                .toList();
    }

    public void agregarObservador(String productoId, ObservadorStock observador) {
        buscarProducto(productoId).agregarObservadorStock(observador);
    }

    public void quitarObservador(String productoId, ObservadorStock observador) {
        buscarProducto(productoId).quitarObservadorStock(observador);
    }

    public void actualizarStock(String productoId, int nuevoStock) {
        buscarProducto(productoId).actualizarStock(nuevoStock);
    }

    public void aumentarStock(String productoId, int cantidad) {
        buscarProducto(productoId).aumentarStock(cantidad);
    }

    public void disminuirStock(String productoId, int cantidad) {
        buscarProducto(productoId).disminuirStock(cantidad);
    }

    private Producto buscarProducto(String productoId) {
        if (productoId == null || productoId.isBlank()) {
            throw new IllegalArgumentException("El id del producto es obligatorio");
        }
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe: " + productoId));
    }

}
