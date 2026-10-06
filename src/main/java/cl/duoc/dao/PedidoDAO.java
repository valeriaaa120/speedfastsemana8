package cl.duoc.dao;

import cl.duoc.conexion.ConexionDB;
import cl.duoc.model.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {


    public boolean create(Pedido pedido) {

        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";

        Connection conexion = ConexionDB.conectar();

        if (conexion == null) {
            return false;
        }

        try (
                conexion;
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo());
            ps.setString(3, pedido.getEstado());

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println("Error al crear pedido:");
            System.out.println(e.getMessage());

            return false;
        }
    }



    public List<Pedido> readAll() {

        List<Pedido> lista = new ArrayList<>();

        String sql = "SELECT id, direccion, tipo, estado FROM pedidos";

        Connection conexion = ConexionDB.conectar();

        if (conexion == null) {
            return lista;
        }

        try (
                conexion;
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Pedido pedido = new Pedido();

                pedido.setId(rs.getInt("id"));
                pedido.setDireccion(rs.getString("direccion"));
                pedido.setTipo(rs.getString("tipo"));
                pedido.setEstado(rs.getString("estado"));

                lista.add(pedido);
            }

        } catch (SQLException e) {

            System.out.println("Error al leer pedidos:");
            System.out.println(e.getMessage());
        }

        return lista;
    }



    public boolean update(Pedido pedido) {

        String sql = """
                UPDATE pedidos
                SET direccion = ?, tipo = ?, estado = ?
                WHERE id = ?
                """;

        Connection conexion = ConexionDB.conectar();

        if (conexion == null) {
            return false;
        }

        try (
                conexion;
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo());
            ps.setString(3, pedido.getEstado());
            ps.setInt(4, pedido.getId());

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println("Error al actualizar pedido:");
            System.out.println(e.getMessage());

            return false;
        }
    }



    public boolean delete(int id) {

        String sql = "DELETE FROM pedidos WHERE id = ?";

        Connection conexion = ConexionDB.conectar();

        if (conexion == null) {
            return false;
        }

        try (
                conexion;
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println("Error al eliminar pedido:");
            System.out.println(e.getMessage());

            return false;
        }
    }
}
