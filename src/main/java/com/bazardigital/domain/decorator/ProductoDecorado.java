package com.bazardigital.domain.decorator;

import java.util.Objects;

import com.bazardigital.domain.model.Producto;

public abstract class ProductoDecorado extends Producto {
    private final Producto producto;
    private final String nombreExtra;
    private final double costoExtra;

    protected ProductoDecorado(Producto producto, String nombreExtra, double costoExtra) {
        super(validarProducto(producto).getId(), producto.getNombre(), producto.getDescripcion(),
                producto.getPrecio(), producto.getCategoria(), producto.getStock());
        this.producto = producto;
        this.nombreExtra = Objects.requireNonNull(nombreExtra, "El nombre del extra es obligatorio");
        if (nombreExtra.isBlank()) {
            throw new IllegalArgumentException("El nombre del extra es obligatorio");
        }
        if (!Double.isFinite(costoExtra) || costoExtra < 0) {
            throw new IllegalArgumentException("El costo del extra debe ser finito y no negativo");
        }
        if (!Double.isFinite(producto.getPrecio() + costoExtra)) {
            throw new IllegalArgumentException("El precio decorado debe ser finito");
        }
        this.costoExtra = costoExtra;
    }

    private static Producto validarProducto(Producto producto) {
        return Objects.requireNonNull(producto, "El producto base es obligatorio");
    }

    @Override
    public String getDescripcion() {
        return producto.getDescripcion() + " + " + nombreExtra;
    }

    @Override
    public double getPrecio() {
        return producto.getPrecio() + costoExtra;
    }

    @Override
    public int getStock() {
        return producto.getStock();
    }

    @Override
    public void actualizarStock(int nuevoStock) {
        producto.actualizarStock(nuevoStock);
    }
}
