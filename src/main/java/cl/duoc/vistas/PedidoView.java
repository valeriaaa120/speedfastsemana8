package cl.duoc.vistas;

import cl.duoc.dao.PedidoDAO;
import cl.duoc.model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PedidoView extends JFrame {

    private JTextField txtDireccion;

    private JComboBox<String> comboTipo;
    private JComboBox<String> comboEstado;

    private JComboBox<String> comboFiltroTipo;
    private JComboBox<String> comboFiltroEstado;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JButton btnCrear;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnFiltrar;

    private PedidoDAO dao;

    public PedidoView() {

        dao = new PedidoDAO();

        setTitle("Gestión de Pedidos");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelPrincipal =
                new JPanel(new BorderLayout());


        JPanel panelFormulario = new JPanel();

        panelFormulario.add(
                new JLabel("Dirección:")
        );

        txtDireccion = new JTextField(15);
        panelFormulario.add(txtDireccion);


        panelFormulario.add(
                new JLabel("Tipo:")
        );

        comboTipo = new JComboBox<>(
                new String[]{
                        "COMIDA",
                        "ENCOMIENDA",
                        "EXPRESS"
                }
        );

        panelFormulario.add(comboTipo);


        panelFormulario.add(
                new JLabel("Estado:")
        );

        comboEstado = new JComboBox<>(
                new String[]{
                        "PENDIENTE",
                        "EN_REPARTO",
                        "ENTREGADO"
                }
        );

        panelFormulario.add(comboEstado);


        btnCrear = new JButton("Crear");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelFormulario.add(btnCrear);
        panelFormulario.add(btnActualizar);
        panelFormulario.add(btnEliminar);
        panelFormulario.add(btnLimpiar);


        // filtros

        JPanel panelFiltros = new JPanel();

        panelFiltros.add(
                new JLabel("Filtrar por tipo:")
        );

        comboFiltroTipo = new JComboBox<>(
                new String[]{
                        "TODOS",
                        "COMIDA",
                        "ENCOMIENDA",
                        "EXPRESS"
                }
        );

        panelFiltros.add(comboFiltroTipo);


        panelFiltros.add(
                new JLabel("Filtrar por estado:")
        );

        comboFiltroEstado = new JComboBox<>(
                new String[]{
                        "TODOS",
                        "PENDIENTE",
                        "EN_REPARTO",
                        "ENTREGADO"
                }
        );

        panelFiltros.add(comboFiltroEstado);


        btnFiltrar = new JButton("Filtrar");

        panelFiltros.add(btnFiltrar);


        // panel

        JPanel panelSuperior = new JPanel();

        panelSuperior.setLayout(
                new BoxLayout(
                        panelSuperior,
                        BoxLayout.Y_AXIS
                )
        );

        panelSuperior.add(panelFormulario);
        panelSuperior.add(panelFiltros);

        panelPrincipal.add(
                panelSuperior,
                BorderLayout.NORTH
        );


        // tabla

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Dirección");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Estado");

        tabla = new JTable(modeloTabla);

        JScrollPane scrollPane =
                new JScrollPane(tabla);

        panelPrincipal.add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(panelPrincipal);



        btnCrear.addActionListener(
                e -> crearPedido()
        );

        btnActualizar.addActionListener(
                e -> actualizarPedido()
        );

        btnEliminar.addActionListener(
                e -> eliminarPedido()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        btnFiltrar.addActionListener(
                e -> filtrarPedidos()
        );


        // selec fila

        tabla.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int fila =
                                tabla.getSelectedRow();

                        if (fila != -1) {

                            txtDireccion.setText(
                                    modeloTabla
                                            .getValueAt(fila, 1)
                                            .toString()
                            );

                            comboTipo.setSelectedItem(
                                    modeloTabla
                                            .getValueAt(fila, 2)
                                            .toString()
                            );

                            comboEstado.setSelectedItem(
                                    modeloTabla
                                            .getValueAt(fila, 3)
                                            .toString()
                            );
                        }
                    }
                });


        // Cargar pedidos al abrir
        cargarPedidos();
    }


    // cargar pedidos

    private void cargarPedidos() {

        modeloTabla.setRowCount(0);

        for (Pedido p : dao.readAll()) {

            modeloTabla.addRow(
                    new Object[]{
                            p.getId(),
                            p.getDireccion(),
                            p.getTipo(),
                            p.getEstado()
                    }
            );
        }
    }


    // crear

    private void crearPedido() {

        String direccion =
                txtDireccion.getText().trim();

        String tipo =
                comboTipo.getSelectedItem().toString();

        String estado =
                comboEstado.getSelectedItem().toString();


        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar una dirección."
            );

            return;
        }


        Pedido pedido =
                new Pedido(
                        direccion,
                        tipo,
                        estado
                );


        boolean creado =
                dao.create(pedido);


        if (creado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido creado correctamente."
            );

            limpiarFormulario();
            cargarPedidos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo crear el pedido."
            );
        }
    }


    // actualizar

    private void actualizarPedido() {

        int fila =
                tabla.getSelectedRow();


        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pedido."
            );

            return;
        }


        String direccion =
                txtDireccion.getText().trim();


        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar una dirección."
            );

            return;
        }


        String tipo =
                comboTipo.getSelectedItem().toString();

        String estado =
                comboEstado.getSelectedItem().toString();


        int id =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(fila, 0)
                                .toString()
                );


        Pedido pedido =
                new Pedido(
                        id,
                        direccion,
                        tipo,
                        estado
                );


        boolean actualizado =
                dao.update(pedido);


        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido actualizado correctamente."
            );

            limpiarFormulario();
            cargarPedidos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el pedido."
            );
        }
    }


    // eliminar

    private void eliminarPedido() {

        int fila =
                tabla.getSelectedRow();


        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pedido."
            );

            return;
        }


        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar este pedido?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );


        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }


        int id =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(fila, 0)
                                .toString()
                );


        boolean eliminado =
                dao.delete(id);


        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido eliminado correctamente."
            );

            limpiarFormulario();
            cargarPedidos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el pedido."
            );
        }
    }


    // filtrar

    private void filtrarPedidos() {

        String tipo =
                comboFiltroTipo
                        .getSelectedItem()
                        .toString();

        String estado =
                comboFiltroEstado
                        .getSelectedItem()
                        .toString();


        modeloTabla.setRowCount(0);


        for (Pedido p : dao.readAll()) {

            boolean coincideTipo =
                    tipo.equals("TODOS")
                            || p.getTipo().equals(tipo);


            boolean coincideEstado =
                    estado.equals("TODOS")
                            || p.getEstado().equals(estado);


            if (coincideTipo && coincideEstado) {

                modeloTabla.addRow(
                        new Object[]{
                                p.getId(),
                                p.getDireccion(),
                                p.getTipo(),
                                p.getEstado()
                        }
                );
            }
        }
    }


    // limpiar

    private void limpiarFormulario() {

        txtDireccion.setText("");

        comboTipo.setSelectedIndex(0);

        comboEstado.setSelectedIndex(0);

        tabla.clearSelection();
    }
}
