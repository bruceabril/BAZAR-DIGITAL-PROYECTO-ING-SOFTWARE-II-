package com.bazardigital.domain.model;

import java.util.Objects;

public abstract class Producto {
    private final String id;
    private final String nombre;
    private final String descripcion;
    private final double precio;
    private final Categoria categoria;
    private int stock;

    protected Producto(String id, String nombre, String descripcion, double precio,
                       Categoria categoria, int stock) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del producto es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
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

    public void actualizarStock(int nuevoStock) {
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stock = nuevoStock;
    }
}
