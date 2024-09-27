package clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnection {
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/consultorio";
            String user = "root";
            String password = "andrea123"; // Reemplaza con tu contraseña
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getPacientes() {
        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("SELECT * FROM paciente");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}