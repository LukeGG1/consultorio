package modelos;

import clases.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cirugia extends conexion {

    private int idCirugia;
    private String nombre;

    public Cirugia() {}

    public Cirugia(String nombre) {
        this.nombre = nombre;
    }

    public boolean verificarOCirugia() {
        String sql = "SELECT id_cirugia FROM cirugia WHERE nombre = ?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, nombre);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    this.idCirugia = rs.getInt("id_cirugia");
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean agregarCirugia() {
        if (!verificarOCirugia()) {
            String sql = "INSERT INTO cirugia (nombre) VALUES (?)";
            try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stm.setString(1, nombre);
                int rowsInserted = stm.executeUpdate();
                if (rowsInserted > 0) {
                    try (ResultSet rs = stm.getGeneratedKeys()) {
                        if (rs.next()) {
                            this.idCirugia = rs.getInt(1);
                        }
                    }
                    return true;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public int getIdCirugia() {
        return idCirugia;
    }

    public void setIdCirugia(int idCirugia) {
        this.idCirugia = idCirugia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
