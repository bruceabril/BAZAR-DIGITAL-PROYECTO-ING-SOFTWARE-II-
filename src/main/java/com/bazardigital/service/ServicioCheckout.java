package com.bazardigital.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.bazardigital.domain.model.Carrito;
import com.bazardigital.domain.model.Cliente;
import com.bazardigital.domain.model.Factura;
import com.bazardigital.domain.model.LineaCarrito;
import com.bazardigital.domain.model.ModalidadEntrega;
import com.bazardigital.domain.model.Pedido;
import com.bazardigital.domain.model.Producto;
import com.bazardigital.domain.strategy.descuento.CalculadoraDescuentos;
import com.bazardigital.domain.strategy.descuento.EstrategiaDescuento;
import com.bazardigital.domain.strategy.descuento.ResultadoDescuento;
import com.bazardigital.domain.strategy.pago.DatosPago;
import com.bazardigital.domain.strategy.pago.EstrategiaPago;
import com.bazardigital.domain.strategy.pago.ResultadoPago;
import com.bazardigital.repository.PedidoRepository;

public class ServicioCheckout {
    private final PedidoRepository pedidoRepository;
    private final Map<String, Factura> facturasPorPedido = new HashMap<>();

    public ServicioCheckout(PedidoRepository pedidoRepository) {
        this.pedidoRepository = Objects.requireNonNull(pedidoRepository);
    }

    public Factura cerrarPedido(Cliente cliente, Carrito carrito, ModalidadEntrega modalidadEntrega,
                                List<EstrategiaDescuento> descuentos, EstrategiaPago estrategiaPago,
                                DatosPago datosPago) {
        Objects.requireNonNull(cliente, "El cliente es obligatorio");
        Objects.requireNonNull(carrito, "El carrito es obligatorio");
        Objects.requireNonNull(modalidadEntrega, "La modalidad de entrega es obligatoria");
        Objects.requireNonNull(estrategiaPago, "El medio de pago es obligatorio");
        if (carrito.estaVacio()) {
            throw new IllegalArgumentException("No se puede cerrar un carrito vacio");
        }

        double subtotal = carrito.calcularSubtotal();
        ResultadoDescuento resultadoDescuento = CalculadoraDescuentos.calcular(
                subtotal, carrito.getCantidadTotal(), descuentos);
        List<Producto> productosPedido = new ArrayList<>();
        for (LineaCarrito linea : carrito.getLineas()) {
            for (int i = 0; i < linea.getCantidad(); i++) {
                productosPedido.add(linea.getProducto());
            }
        }

        String pedidoId = UUID.randomUUID().toString();
        Pedido pedido = new Pedido(pedidoId, cliente, productosPedido, modalidadEntrega,
                resultadoDescuento.getMonto());
        ResultadoPago resultadoPago = estrategiaPago.procesar(pedido.calcularTotal(), datosPago);
        pedidoRepository.save(pedido);

        Factura factura = new Factura(UUID.randomUUID().toString(), pedido, Instant.now(), resultadoPago);
        facturasPorPedido.put(pedidoId, factura);
        carrito.vaciar();
        return factura;
    }

    public Optional<Factura> buscarFacturaPorPedido(String pedidoId) {
        return Optional.ofNullable(facturasPorPedido.get(pedidoId));
    }
}
