package cl.duoc.vistas;

import cl.duoc.dao.RepartidorDAO;
import cl.duoc.model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RepartidorView extends JFrame {

    private JTextField txtNombre;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JButton btnCrear;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private RepartidorDAO dao;

    public RepartidorView() {

        dao = new RepartidorDAO();

        setTitle("Gestión de Repartidores");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel panelPrincipal = new JPanel(new BorderLayout());


        JPanel panelFormulario = new JPanel();

        panelFormulario.add(new JLabel("Nombre:"));

        txtNombre = new JTextField(20);
        panelFormulario.add(txtNombre);

        btnCrear = new JButton("Crear");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelFormulario.add(btnCrear);
        panelFormulario.add(btnActualizar);
        panelFormulario.add(btnEliminar);
        panelFormulario.add(btnLimpiar);

        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");

        tabla = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tabla);

        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        add(panelPrincipal);


        btnCrear.addActionListener(e -> crearRepartidor());

        btnActualizar.addActionListener(e -> actualizarRepartidor());

        btnEliminar.addActionListener(e -> eliminarRepartidor());

        btnLimpiar.addActionListener(e -> limpiarFormulario());


        tabla.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int fila = tabla.getSelectedRow();

                if (fila != -1) {

                    txtNombre.setText(
                            modeloTabla.getValueAt(fila, 1).toString()
                    );
                }
            }
        });

        cargarRepartidores();
    }


   //para ver los repartidores

    private void cargarRepartidores() {

        modeloTabla.setRowCount(0);

        for (Repartidor r : dao.readAll()) {

            modeloTabla.addRow(new Object[]{
                    r.getId(),
                    r.getNombre()
            });
        }
    }


    // para crearlos

    private void crearRepartidor() {

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar un nombre."
            );

            return;
        }

        Repartidor repartidor = new Repartidor(nombre);

        boolean creado = dao.create(repartidor);

        if (creado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor creado correctamente."
            );

            limpiarFormulario();
            cargarRepartidores();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo crear el repartidor."
            );
        }
    }


    // para actualizar

    private void actualizarRepartidor() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un repartidor."
            );

            return;
        }

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar un nombre."
            );

            return;
        }

        int id = Integer.parseInt(
                modeloTabla.getValueAt(fila, 0).toString()
        );

        Repartidor repartidor =
                new Repartidor(id, nombre);

        boolean actualizado = dao.update(repartidor);

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor actualizado correctamente."
            );

            limpiarFormulario();
            cargarRepartidores();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el repartidor."
            );
        }
    }


    // para eliminarlo

    private void eliminarRepartidor() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un repartidor."
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este repartidor?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        int id = Integer.parseInt(
                modeloTabla.getValueAt(fila, 0).toString()
        );

        boolean eliminado = dao.delete(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor eliminado correctamente."
            );

            limpiarFormulario();
            cargarRepartidores();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el repartidor."
            );
        }
    }


    // limpiar

    private void limpiarFormulario() {

        txtNombre.setText("");

        tabla.clearSelection();
    }
}