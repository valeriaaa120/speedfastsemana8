package cl.duoc.vistas;

import javax.swing.*;

public class MainView extends JFrame {

    private JButton btnRepartidores;
    private JButton btnPedidos;
    private JButton btnEntregas;

    public MainView() {

        setTitle("SpeedFast");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        btnRepartidores = new JButton("Repartidores");
        btnPedidos = new JButton("Pedidos");
        btnEntregas = new JButton("Entregas");

        panel.add(btnRepartidores);
        panel.add(btnPedidos);
        panel.add(btnEntregas);

        add(panel);

        // Botón Repartidores
        btnRepartidores.addActionListener(e -> {

            new RepartidorView().setVisible(true);

        });

        // Botón Pedidos
        btnPedidos.addActionListener(e -> {

            new PedidoView().setVisible(true);

        });

        // Botón Entregas
        btnEntregas.addActionListener(e -> {

            new EntregaView().setVisible(true);

        });
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MainView ventana = new MainView();

            ventana.setVisible(true);

        });
    }
}
