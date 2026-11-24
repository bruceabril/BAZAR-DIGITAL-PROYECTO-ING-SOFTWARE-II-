package com.bazardigital.ui.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.bazardigital.domain.model.Carrito;
import com.bazardigital.domain.model.Cliente;
import com.bazardigital.domain.model.EntregaDigital;
import com.bazardigital.domain.model.EntregaDomicilio;
import com.bazardigital.domain.model.EntregaRetiro;
import com.bazardigital.domain.model.ModalidadEntrega;
import com.bazardigital.domain.model.Producto;
import com.bazardigital.domain.observer.ObservadorStock;
import com.bazardigital.domain.strategy.pago.AutorizacionTarjeta;
import com.bazardigital.domain.strategy.pago.DatosPago;
import com.bazardigital.domain.strategy.pago.EstrategiaPago;
import com.bazardigital.domain.strategy.pago.PagoContraEntrega;
import com.bazardigital.domain.strategy.pago.PagoTarjeta;
import com.bazardigital.domain.strategy.pago.PagoTransferencia;
import com.bazardigital.domain.strategy.pago.ReferenciaTransferencia;
import com.bazardigital.repository.InMemoryPedidoRepository;
import com.bazardigital.repository.InMemoryProductoRepository;
import com.bazardigital.service.ServicioCarrito;
import com.bazardigital.service.ServicioCheckout;
import com.bazardigital.service.ServicioInventario;

public final class AplicacionGrafica {
    private final InMemoryProductoRepository productos = new InMemoryProductoRepository();
    private final ServicioInventario inventario = new ServicioInventario(productos);
    private final ServicioCarrito servicioCarrito = new ServicioCarrito(productos);
    private final ServicioCheckout checkout = new ServicioCheckout(new InMemoryPedidoRepository());
    private final Carrito carrito = new Carrito();
    private final JComboBox<Producto> selectorProducto = new JComboBox<>();
    private final JTextField cantidad = new JTextField("1", 5);
    private final JTextArea resumen = new JTextArea(12, 48);

    private AplicacionGrafica() {
        ObservadorStock observadorStock = (producto, anterior, nuevo) ->
                agregarMensaje("Stock de " + producto.getNombre() + ": " + anterior + " -> " + nuevo);
        productos.findAll().forEach(producto ->
                inventario.agregarObservador(producto.getId(), observadorStock));
        checkout.agregarObservadorPedido((pedido, anterior, nuevo) ->
                agregarMensaje("Pedido " + pedido.getId() + ": " + anterior + " -> " + nuevo));
        actualizarCatalogo();
    }

    public static void iniciar() {
        SwingUtilities.invokeLater(() -> new AplicacionGrafica().mostrar());
    }

    private void mostrar() {
        JFrame ventana = new JFrame("Bazar Digital");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new BorderLayout(8, 8));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectorProducto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> lista, Object valor,
                    int indice, boolean seleccionado, boolean enfocado) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, enfocado);
                if (valor instanceof Producto producto) {
                    setText(producto.getNombre() + " | $" + producto.getPrecio()
                            + " | stock: " + producto.getStock());
                }
                return this;
            }
        });
        JButton agregar = new JButton("Agregar al carrito");
        JButton inventario = new JButton("Ajustar inventario");
        JButton pagar = new JButton("Finalizar compra");
        acciones.add(new JLabel("Producto:"));
        acciones.add(selectorProducto);
        acciones.add(new JLabel("Cantidad:"));
        acciones.add(cantidad);
        acciones.add(agregar);
        acciones.add(inventario);
        acciones.add(pagar);

        resumen.setEditable(false);
        ventana.add(acciones, BorderLayout.NORTH);
        ventana.add(new JScrollPane(resumen), BorderLayout.CENTER);

        agregar.addActionListener(evento -> agregarProducto());
        inventario.addActionListener(evento -> ajustarInventario());
        pagar.addActionListener(evento -> finalizarCompra(ventana));
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent evento) {
                mostrarCatalogo();
            }
        });

        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private void agregarProducto() {
        Producto producto = (Producto) selectorProducto.getSelectedItem();
        if (producto == null) {
            mostrarError("No hay productos disponibles.");
            return;
        }
        try {
            servicioCarrito.agregarProducto(carrito, producto,
                    Integer.parseInt(cantidad.getText().trim()));
            agregarMensaje("Agregado: " + producto.getNombre() + " x" + cantidad.getText().trim());
            mostrarCarrito();
        } catch (RuntimeException error) {
            mostrarError(error.getMessage());
        }
    }

    private void ajustarInventario() {
        Producto producto = (Producto) selectorProducto.getSelectedItem();
        if (producto == null) {
            mostrarError("No hay productos seleccionados.");
            return;
        }
        String valor = JOptionPane.showInputDialog(null, "Nuevo stock para " + producto.getNombre(),
                producto.getStock());
        if (valor == null) {
            return;
        }
        try {
            inventario.actualizarStock(producto.getId(), Integer.parseInt(valor.trim()));
            actualizarCatalogo();
            agregarMensaje("Inventario actualizado para " + producto.getNombre());
        } catch (RuntimeException error) {
            mostrarError(error.getMessage());
        }
    }

    private void finalizarCompra(JFrame ventana) {
        if (carrito.estaVacio()) {
            mostrarError("Agrega productos antes de finalizar la compra.");
            return;
        }
        JTextField nombre = new JTextField();
        JTextField correo = new JTextField();
        Object[] clienteForm = {"Nombre", nombre, "Correo", correo};
        if (JOptionPane.showConfirmDialog(ventana, clienteForm, "Datos del cliente",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            ModalidadEntrega entrega = elegirEntrega(ventana);
            if (entrega == null) {
                return;
            }
            EstrategiaPago pago = elegirPago(ventana);
            if (pago == null) {
                return;
            }
            DatosPago datosPago = switch (pago.getNombre()) {
                case "Tarjeta" -> solicitarAutorizacionTarjeta(ventana);
                case "Transferencia" -> solicitarReferenciaTransferencia(ventana);
                default -> null;
            };
            if ((pago instanceof PagoTarjeta || pago instanceof PagoTransferencia)
                    && datosPago == null) {
                return;
            }
            var factura = checkout.cerrarPedido(
                    crearCliente(nombre.getText(), correo.getText()),
                    carrito, entrega, List.of(), pago, datosPago);
            agregarMensaje("Compra confirmada. Factura " + factura.getId()
                    + " | total: $" + factura.getTotal());
            mostrarCarrito();
        } catch (RuntimeException error) {
            mostrarError(error.getMessage());
        }
    }

    private static ModalidadEntrega elegirEntrega(JFrame ventana) {
        String[] opciones = {"Domicilio", "Retiro", "Digital"};
        int opcion = JOptionPane.showOptionDialog(ventana, "Selecciona la entrega", "Entrega",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        return switch (opcion) {
            case 0 -> new EntregaDomicilio();
            case 1 -> new EntregaRetiro();
            case 2 -> new EntregaDigital();
            default -> null;
        };
    }

    private static EstrategiaPago elegirPago(JFrame ventana) {
        String[] opciones = {"Tarjeta", "Transferencia", "Contra entrega"};
        int opcion = JOptionPane.showOptionDialog(ventana, "Selecciona el medio de pago", "Pago",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        return switch (opcion) {
            case 0 -> new PagoTarjeta();
            case 1 -> new PagoTransferencia();
            case 2 -> new PagoContraEntrega();
            default -> null;
        };
    }

    private static Cliente crearCliente(String nombre, String correo) {
        String nombreLimpio = nombre.trim();
        String correoLimpio = correo.trim();
        if (nombreLimpio.isEmpty() || correoLimpio.isEmpty()) {
            throw new IllegalArgumentException("El nombre y el correo son obligatorios.");
        }
        return new Cliente("CLI-" + System.currentTimeMillis(), nombreLimpio, correoLimpio);
    }

    private static DatosPago solicitarAutorizacionTarjeta(JFrame ventana) {
        String codigo = JOptionPane.showInputDialog(ventana, "Codigo de autorizacion simulado");
        return codigo == null || codigo.isBlank() ? null : new AutorizacionTarjeta(codigo.trim(), true);
    }

    private static DatosPago solicitarReferenciaTransferencia(JFrame ventana) {
        String referencia = JOptionPane.showInputDialog(ventana, "Referencia de transferencia");
        return referencia == null || referencia.isBlank()
                ? null
                : new ReferenciaTransferencia(referencia.trim());
    }

    private void actualizarCatalogo() {
        selectorProducto.setModel(new DefaultComboBoxModel<>(inventario.listarProductos()
                .toArray(Producto[]::new)));
    }

    private void mostrarCatalogo() {
        agregarMensaje("--- Catalogo ---");
        for (Producto producto : inventario.listarProductos()) {
            agregarMensaje(producto.getId() + " | " + producto.getNombre() + " | $"
                    + producto.getPrecio() + " | stock: " + producto.getStock());
        }
    }

    private void mostrarCarrito() {
        agregarMensaje("--- Carrito ---");
        carrito.getLineas().forEach(linea -> agregarMensaje(linea.getProducto().getNombre()
                + " x" + linea.getCantidad() + " | $" + linea.calcularSubtotal()));
        agregarMensaje("Subtotal: $" + carrito.calcularSubtotal());
    }

    private void agregarMensaje(String mensaje) {
        resumen.append(mensaje + System.lineSeparator());
    }

    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "No se pudo completar",
                JOptionPane.ERROR_MESSAGE);
    }
}
