package com.bazardigital.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Carrito {
    private final List<LineaCarrito> lineas = new ArrayList<>();

    public List<LineaCarrito> getLineas() {
        return Collections.unmodifiableList(lineas);
    }

    public void agregar(Producto producto, int cantidad) {
        lineas.add(new LineaCarrito(producto, cantidad));
    }

    public boolean eliminarProducto(String productoId) {
        Objects.requireNonNull(productoId, "El id del producto es obligatorio");
        boolean eliminado = false;
        Iterator<LineaCarrito> iterator = lineas.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getProducto().getId().equals(productoId)) {
                iterator.remove();
                eliminado = true;
            }
        }
        return eliminado;
    }

    public int getCantidadTotal() {
        return lineas.stream().mapToInt(LineaCarrito::getCantidad).sum();
    }

    public double calcularSubtotal() {
        return lineas.stream().mapToDouble(LineaCarrito::calcularSubtotal).sum();
    }

    public boolean estaVacio() {
        return lineas.isEmpty();
    }

    public void vaciar() {
        lineas.clear();
    }
}
