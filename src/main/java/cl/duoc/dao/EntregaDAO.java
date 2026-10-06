package cl.duoc.dao;

import cl.duoc.conexion.ConexionDB;
import cl.duoc.model.Entrega;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {


    public boolean create(Entrega entrega) {

        String sql = """
                INSERT INTO entregas
                (id_pedido, id_repartidor, fecha, hora)
                VALUES (?, ?, ?, ?)
                """;

        Connection conexion = ConexionDB.conectar();

        if (conexion == null) {
            return false;
        }

        try (
                conexion;
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, java.sql.Date.valueOf(entrega.getFecha()));
            ps.setTime(4, java.sql.Time.valueOf(entrega.getHora()));

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println("Error al crear entrega:");
            System.out.println(e.getMessage());

            return false;
        }
    }



    public List<Entrega> readAll() {

        List<Entrega> lista = new ArrayList<>();

        String sql = """
                SELECT id, id_pedido, id_repartidor, fecha, hora
                FROM entregas
                """;

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

                Entrega entrega = new Entrega();

                entrega.setId(rs.getInt("id"));
                entrega.setIdPedido(rs.getInt("id_pedido"));
                entrega.setIdRepartidor(rs.getInt("id_repartidor"));

                entrega.setFecha(
                        rs.getDate("fecha").toLocalDate()
                );

                entrega.setHora(
                        rs.getTime("hora").toLocalTime()
                );

                lista.add(entrega);
            }

        } catch (SQLException e) {

            System.out.println("Error al leer entregas:");
            System.out.println(e.getMessage());
        }

        return lista;
    }



    public boolean update(Entrega entrega) {

        String sql = """
                UPDATE entregas
                SET id_pedido = ?,
                    id_repartidor = ?,
                    fecha = ?,
                    hora = ?
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

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, java.sql.Date.valueOf(entrega.getFecha()));
            ps.setTime(4, java.sql.Time.valueOf(entrega.getHora()));
            ps.setInt(5, entrega.getId());

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println("Error al actualizar entrega:");
            System.out.println(e.getMessage());

            return false;
        }
    }



    public boolean delete(int id) {

        String sql = "DELETE FROM entregas WHERE id = ?";

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

            System.out.println("Error al eliminar entrega:");
            System.out.println(e.getMessage());

            return false;
        }
    }
}