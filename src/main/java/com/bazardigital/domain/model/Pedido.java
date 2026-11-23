package com.bazardigital.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Pedido {
    private final String id;
    private final Cliente cliente;
    private final List<Producto> productos;
    private final ModalidadEntrega modalidadEntrega;
    private final double descuento;

    public Pedido(String id, Cliente cliente, List<Producto> productos,
                  ModalidadEntrega modalidadEntrega) {
        this(id, cliente, productos, modalidadEntrega, 0);
    }

    public Pedido(String id, Cliente cliente, List<Producto> productos,
                  ModalidadEntrega modalidadEntrega, double descuento) {
        this.id = Objects.requireNonNull(id);
        this.cliente = Objects.requireNonNull(cliente);
        this.modalidadEntrega = Objects.requireNonNull(modalidadEntrega);
        if (!Double.isFinite(descuento) || descuento < 0) {
            throw new IllegalArgumentException("El descuento debe ser finito y no negativo");
        }
        this.descuento = descuento;
        this.productos = new ArrayList<>(productos == null ? Collections.emptyList() : productos);
    }

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }

    public ModalidadEntrega getModalidadEntrega() {
        return modalidadEntrega;
    }

    public double calcularSubtotal() {
        return productos.stream().mapToDouble(Producto::getPrecio).sum();
    }

    public double calcularTotal() {
        return calcularSubtotal() - Math.min(descuento, calcularSubtotal())
                + modalidadEntrega.getCostoAdicional();
    }

    public double getDescuento() {
        return Math.min(descuento, calcularSubtotal());
    }
}
