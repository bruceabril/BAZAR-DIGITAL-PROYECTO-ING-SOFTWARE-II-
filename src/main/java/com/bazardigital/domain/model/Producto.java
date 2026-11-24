package com.bazardigital.domain.model;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bazardigital.domain.observer.ObservadorStock;

public abstract class Producto {
    private final String id;
    private final String nombre;
    private final String descripcion;
    private final double precio;
    private final Categoria categoria;
    private final CopyOnWriteArrayList<ObservadorStock> observadoresStock = new CopyOnWriteArrayList<>();
    private volatile int stock;

    protected Producto(String id, String nombre, String descripcion, double precio,
                       Categoria categoria, int stock) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del producto es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException("El precio debe ser finito y no negativo");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion == null ? "" : descripcion;
        this.precio = precio;
        this.categoria = Objects.requireNonNull(categoria, "La categoria es obligatoria");
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public int getStock() {
        return stock;
    }

    public void agregarObservadorStock(ObservadorStock observador) {
        observadoresStock.addIfAbsent(Objects.requireNonNull(observador,
                "El observador de stock es obligatorio"));
    }

    public void quitarObservadorStock(ObservadorStock observador) {
        observadoresStock.remove(Objects.requireNonNull(observador,
                "El observador de stock es obligatorio"));
    }

    public synchronized void actualizarStock(int nuevoStock) {
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        notificarCambioStock(nuevoStock);
    }

    public synchronized void aumentarStock(int cantidad) {
        validarCantidadStock(cantidad);
        notificarCambioStock(Math.addExact(stock, cantidad));
    }

    public synchronized void disminuirStock(int cantidad) {
        validarCantidadStock(cantidad);
        if (cantidad > stock) {
            throw new IllegalArgumentException("La disminucion supera el stock disponible");
        }
        notificarCambioStock(stock - cantidad);
    }

    private static void validarCantidadStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }

    private void notificarCambioStock(int nuevoStock) {
        int stockAnterior = stock;
        this.stock = nuevoStock;
        if (stockAnterior != nuevoStock) {
            observadoresStock.forEach(observador ->
                    observador.alCambiarStock(this, stockAnterior, nuevoStock));
        }
    }
}
