package com.bazardigital.service;

import java.util.Objects;

import com.bazardigital.domain.model.Carrito;
import com.bazardigital.domain.model.Producto;
import com.bazardigital.repository.ProductoRepository;

public class ServicioCarrito {
    private final ProductoRepository productoRepository;

    public ServicioCarrito(ProductoRepository productoRepository) {
        this.productoRepository = Objects.requireNonNull(productoRepository);
    }

    public void agregarProducto(Carrito carrito, String productoId, int cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe: " + productoId));
        agregarProducto(carrito, producto, cantidad);
    }

    public void agregarProducto(Carrito carrito, Producto producto, int cantidad) {
        Objects.requireNonNull(carrito, "El carrito es obligatorio");
        Objects.requireNonNull(producto, "El producto es obligatorio");
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        Producto productoCatalogo = productoRepository.findById(producto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "El producto no existe: " + producto.getId()));
        int cantidadEnCarrito = carrito.getLineas().stream()
                .filter(linea -> linea.getProducto().getId().equals(producto.getId()))
                .mapToInt(linea -> linea.getCantidad())
                .sum();
        if (cantidadEnCarrito + cantidad > productoCatalogo.getStock()) {
            throw new IllegalArgumentException("La cantidad solicitada supera el stock disponible");
        }
        carrito.agregar(producto, cantidad);
    }

    public void quitarProducto(Carrito carrito, String productoId) {
        Objects.requireNonNull(carrito, "El carrito es obligatorio");
        if (!carrito.eliminarProducto(productoId)) {
            throw new IllegalArgumentException("El producto no esta en el carrito: " + productoId);
        }
    }

    public double calcularSubtotal(Carrito carrito) {
        return Objects.requireNonNull(carrito, "El carrito es obligatorio").calcularSubtotal();
    }
}
