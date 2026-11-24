package com.bazardigital.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bazardigital.domain.observer.ObservadorPedido;

public class Pedido {
    private final String id;
    private final Cliente cliente;
    private final List<Producto> productos;
    private final ModalidadEntrega modalidadEntrega;
    private final double descuento;
    private final CopyOnWriteArrayList<ObservadorPedido> observadores = new CopyOnWriteArrayList<>();
    private EstadoPedido estado = EstadoPedido.CREADO;

    public Pedido(String id, Cliente cliente, List<Producto> productos,
                  ModalidadEntrega modalidadEntrega) {
        this(id, cliente, productos, modalidadEntrega, 0);
    }

    public Pedido(String id, Cliente cliente, List<Producto> productos,
                  ModalidadEntrega modalidadEntrega, double descuento) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del pedido es obligatorio");
        }
        this.id = id;
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

    public EstadoPedido getEstado() {
        return estado;
    }

    public void agregarObservador(ObservadorPedido observador) {
        observadores.addIfAbsent(Objects.requireNonNull(observador,
                "El observador del pedido es obligatorio"));
    }

    public void quitarObservador(ObservadorPedido observador) {
        observadores.remove(Objects.requireNonNull(observador,
                "El observador del pedido es obligatorio"));
    }

    public void cambiarEstado(EstadoPedido nuevoEstado) {
        Objects.requireNonNull(nuevoEstado, "El estado del pedido es obligatorio");
        if (estado == nuevoEstado) {
            return;
        }
        if (estado != EstadoPedido.CREADO || nuevoEstado == EstadoPedido.CREADO) {
            throw new IllegalStateException("La transicion de estado del pedido no es valida");
        }

        EstadoPedido estadoAnterior = estado;
        estado = nuevoEstado;
        observadores.forEach(observador ->
                observador.alCambiarEstado(this, estadoAnterior, nuevoEstado));
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
