package cl.duoc.dao;

import cl.duoc.conexion.ConexionDB;
import cl.duoc.model.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {


    public boolean create(Repartidor repartidor) {

        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";

        Connection conexion = ConexionDB.conectar();

        if (conexion == null) {
            return false;
        }

        try (
                conexion;
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, repartidor.getNombre());

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println("Error al crear repartidor:");
            System.out.println(e.getMessage());

            return false;
        }
    }



    public List<Repartidor> readAll() {

        List<Repartidor> lista = new ArrayList<>();

        String sql = "SELECT id, nombre FROM repartidores";

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

                Repartidor repartidor = new Repartidor();

                repartidor.setId(rs.getInt("id"));
                repartidor.setNombre(rs.getString("nombre"));

                lista.add(repartidor);
            }

        } catch (SQLException e) {

            System.out.println("Error al leer repartidores:");
            System.out.println(e.getMessage());
        }

        return lista;
    }



    public boolean update(Repartidor repartidor) {

        String sql = "UPDATE repartidores SET nombre = ? WHERE id = ?";

        Connection conexion = ConexionDB.conectar();

        if (conexion == null) {
            return false;
        }

        try (
                conexion;
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, repartidor.getNombre());
            ps.setInt(2, repartidor.getId());

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println("Error al actualizar repartidor:");
            System.out.println(e.getMessage());

            return false;
        }
    }



    public boolean delete(int id) {

        String sql = "DELETE FROM repartidores WHERE id = ?";

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

            System.out.println("Error al eliminar repartidor:");
            System.out.println(e.getMessage());

            return false;
        }
    }
}
