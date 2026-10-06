package cl.duoc.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/speedfast_db";

    private static final String USER = "root";

    private static final String PASSWORD = "Valeria2001@";

    public static Connection conectar() {
        try {
            Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Conexión exitosa a SpeedFast");

            return conexion;

        } catch (SQLException e) {

            System.out.println("Error al conectar con la base de datos:");
            System.out.println(e.getMessage());

            return null;
        }
    }
}

