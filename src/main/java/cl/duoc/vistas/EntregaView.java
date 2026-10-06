package cl.duoc.vistas;

import cl.duoc.dao.EntregaDAO;
import cl.duoc.dao.PedidoDAO;
import cl.duoc.dao.RepartidorDAO;
import cl.duoc.model.Entrega;
import cl.duoc.model.Pedido;
import cl.duoc.model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class EntregaView extends JFrame {

    private JComboBox<Pedido> comboPedido;
    private JComboBox<Repartidor> comboRepartidor;

    private JTextField txtFecha;
    private JTextField txtHora;

    private JComboBox<Pedido> comboFiltroPedido;
    private JComboBox<Repartidor> comboFiltroRepartidor;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JButton btnCrear;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnFiltrar;

    private EntregaDAO entregaDAO;
    private PedidoDAO pedidoDAO;
    private RepartidorDAO repartidorDAO;

    public EntregaView() {

        entregaDAO = new EntregaDAO();
        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();

        setTitle("Gestión de Entregas");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel panelPrincipal =
                new JPanel(new BorderLayout());

        JPanel panelFormulario = new JPanel();

        panelFormulario.add(
                new JLabel("Pedido:")
        );

        comboPedido = new JComboBox<>();
        panelFormulario.add(comboPedido);


        panelFormulario.add(
                new JLabel("Repartidor:")
        );

        comboRepartidor = new JComboBox<>();
        panelFormulario.add(comboRepartidor);


        panelFormulario.add(
                new JLabel("Fecha:")
        );

        txtFecha = new JTextField(10);
        panelFormulario.add(txtFecha);


        panelFormulario.add(
                new JLabel("Hora:")
        );

        txtHora = new JTextField(8);
        panelFormulario.add(txtHora);



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
                new JLabel("Filtrar por pedido:")
        );

        comboFiltroPedido = new JComboBox<>();
        panelFiltros.add(comboFiltroPedido);


        panelFiltros.add(
                new JLabel("Filtrar por repartidor:")
        );

        comboFiltroRepartidor = new JComboBox<>();
        panelFiltros.add(comboFiltroRepartidor);


        btnFiltrar = new JButton("Filtrar");

        panelFiltros.add(btnFiltrar);


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

        modeloTabla =
                new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Pedido");
        modeloTabla.addColumn("Repartidor");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Hora");

        tabla = new JTable(modeloTabla);

        JScrollPane scrollPane =
                new JScrollPane(tabla);

        panelPrincipal.add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(panelPrincipal);



        btnCrear.addActionListener(
                e -> crearEntrega()
        );

        btnActualizar.addActionListener(
                e -> actualizarEntrega()
        );

        btnEliminar.addActionListener(
                e -> eliminarEntrega()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        btnFiltrar.addActionListener(
                e -> filtrarEntregas()
        );


        // seleccionar filaa

        tabla.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int fila =
                                tabla.getSelectedRow();

                        if (fila != -1) {

                            int idPedido =
                                    Integer.parseInt(
                                            modeloTabla
                                                    .getValueAt(fila, 1)
                                                    .toString()
                                    );

                            int idRepartidor =
                                    Integer.parseInt(
                                            modeloTabla
                                                    .getValueAt(fila, 2)
                                                    .toString()
                                    );

                            seleccionarPedido(
                                    idPedido
                            );

                            seleccionarRepartidor(
                                    idRepartidor
                            );

                            txtFecha.setText(
                                    modeloTabla
                                            .getValueAt(fila, 3)
                                            .toString()
                            );

                            txtHora.setText(
                                    modeloTabla
                                            .getValueAt(fila, 4)
                                            .toString()
                            );
                        }
                    }
                });


        cargarCombos();
        cargarEntregas();
    }


    // cargar combos

    private void cargarCombos() {

        comboPedido.removeAllItems();
        comboFiltroPedido.removeAllItems();

        comboRepartidor.removeAllItems();
        comboFiltroRepartidor.removeAllItems();


        comboFiltroPedido.addItem(null);
        comboFiltroRepartidor.addItem(null);


        for (Pedido pedido : pedidoDAO.readAll()) {

            comboPedido.addItem(pedido);
            comboFiltroPedido.addItem(pedido);
        }


        for (Repartidor repartidor :
                repartidorDAO.readAll()) {

            comboRepartidor.addItem(repartidor);
            comboFiltroRepartidor.addItem(repartidor);
        }
    }


    // cargar entregas

    private void cargarEntregas() {

        modeloTabla.setRowCount(0);

        for (Entrega entrega :
                entregaDAO.readAll()) {

            modeloTabla.addRow(
                    new Object[]{
                            entrega.getId(),
                            entrega.getIdPedido(),
                            entrega.getIdRepartidor(),
                            entrega.getFecha(),
                            entrega.getHora()
                    }
            );
        }
    }


    // crear
    private void crearEntrega() {

        Pedido pedido =
                (Pedido) comboPedido.getSelectedItem();

        Repartidor repartidor =
                (Repartidor)
                        comboRepartidor.getSelectedItem();

        String fechaTexto =
                txtFecha.getText().trim();

        String horaTexto =
                txtHora.getText().trim();


        if (pedido == null ||
                repartidor == null ||
                fechaTexto.isEmpty() ||
                horaTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe completar todos los campos."
            );

            return;
        }


        try {

            LocalDate fecha =
                    LocalDate.parse(fechaTexto);

            LocalTime hora =
                    LocalTime.parse(horaTexto);


            Entrega entrega =
                    new Entrega(
                            pedido.getId(),
                            repartidor.getId(),
                            fecha,
                            hora
                    );


            if (entregaDAO.create(entrega)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrega creada correctamente."
                );

                limpiarFormulario();
                cargarEntregas();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo crear la entrega."
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fecha u hora inválida.\n" +
                            "Fecha: yyyy-MM-dd\n" +
                            "Hora: HH:mm:ss"
            );
        }
    }


    // actualizar

    private void actualizarEntrega() {

        int fila =
                tabla.getSelectedRow();


        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una entrega."
            );

            return;
        }


        Pedido pedido =
                (Pedido) comboPedido.getSelectedItem();

        Repartidor repartidor =
                (Repartidor)
                        comboRepartidor.getSelectedItem();


        if (pedido == null ||
                repartidor == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pedido y un repartidor."
            );

            return;
        }


        try {

            LocalDate fecha =
                    LocalDate.parse(
                            txtFecha.getText().trim()
                    );

            LocalTime hora =
                    LocalTime.parse(
                            txtHora.getText().trim()
                    );


            int id =
                    Integer.parseInt(
                            modeloTabla
                                    .getValueAt(fila, 0)
                                    .toString()
                    );


            Entrega entrega =
                    new Entrega(
                            id,
                            pedido.getId(),
                            repartidor.getId(),
                            fecha,
                            hora
                    );


            if (entregaDAO.update(entrega)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrega actualizada correctamente."
                );

                limpiarFormulario();
                cargarEntregas();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar la entrega."
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fecha u hora inválida."
            );
        }
    }


    // eliminar

    private void eliminarEntrega() {

        int fila =
                tabla.getSelectedRow();


        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una entrega."
            );

            return;
        }


        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar esta entrega?",
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


        if (entregaDAO.delete(id)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega eliminada correctamente."
            );

            limpiarFormulario();
            cargarEntregas();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar la entrega."
            );
        }
    }


    // filtrar

    private void filtrarEntregas() {

        Pedido pedidoFiltro =
                (Pedido)
                        comboFiltroPedido.getSelectedItem();

        Repartidor repartidorFiltro =
                (Repartidor)
                        comboFiltroRepartidor
                                .getSelectedItem();


        modeloTabla.setRowCount(0);


        for (Entrega entrega :
                entregaDAO.readAll()) {

            boolean coincidePedido =
                    pedidoFiltro == null ||
                            entrega.getIdPedido()
                                    == pedidoFiltro.getId();


            boolean coincideRepartidor =
                    repartidorFiltro == null ||
                            entrega.getIdRepartidor()
                                    == repartidorFiltro.getId();


            if (coincidePedido &&
                    coincideRepartidor) {

                modeloTabla.addRow(
                        new Object[]{
                                entrega.getId(),
                                entrega.getIdPedido(),
                                entrega.getIdRepartidor(),
                                entrega.getFecha(),
                                entrega.getHora()
                        }
                );
            }
        }
    }


    // seleccionr pedido
    private void seleccionarPedido(int id) {

        for (int i = 0;
             i < comboPedido.getItemCount();
             i++) {

            Pedido pedido =
                    comboPedido.getItemAt(i);

            if (pedido != null &&
                    pedido.getId() == id) {

                comboPedido.setSelectedIndex(i);
                break;
            }
        }
    }


    // seleccionar repartidor

    private void seleccionarRepartidor(int id) {

        for (int i = 0;
             i < comboRepartidor.getItemCount();
             i++) {

            Repartidor repartidor =
                    comboRepartidor.getItemAt(i);

            if (repartidor != null &&
                    repartidor.getId() == id) {

                comboRepartidor.setSelectedIndex(i);
                break;
            }
        }
    }


    // limpiar

    private void limpiarFormulario() {

        if (comboPedido.getItemCount() > 0) {
            comboPedido.setSelectedIndex(0);
        }

        if (comboRepartidor.getItemCount() > 0) {
            comboRepartidor.setSelectedIndex(0);
        }

        txtFecha.setText("");
        txtHora.setText("");

        tabla.clearSelection();
    }
}