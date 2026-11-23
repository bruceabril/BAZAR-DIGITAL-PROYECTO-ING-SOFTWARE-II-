package com.bazardigital.domain.model;

import java.time.Instant;
import java.util.Objects;

import com.bazardigital.domain.strategy.pago.ResultadoPago;

public final class Factura {
    private final String id;
    private final Pedido pedido;
    private final Instant fechaEmision;
    private final ResultadoPago pago;

    public Factura(String id, Pedido pedido, Instant fechaEmision, ResultadoPago pago) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id de la factura es obligatorio");
        }
        this.id = id;
        this.pedido = Objects.requireNonNull(pedido, "El pedido es obligatorio");
        this.fechaEmision = Objects.requireNonNull(fechaEmision, "La fecha es obligatoria");
        this.pago = Objects.requireNonNull(pago, "El pago es obligatorio");
    }

    public String getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Instant getFechaEmision() {
        return fechaEmision;
    }

    public ResultadoPago getPago() {
        return pago;
    }

    public double getSubtotal() {
        return pedido.calcularSubtotal();
    }

    public double getDescuento() {
        return pedido.getDescuento();
    }

    public double getCostoEntrega() {
        return pedido.getModalidadEntrega().getCostoAdicional();
    }

    public double getTotal() {
        return pedido.calcularTotal();
    }
}
