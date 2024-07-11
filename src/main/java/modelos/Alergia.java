package modelos;

import clases.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Alergia extends conexion {

    private int idAlergia;
    private String nombre;

    public Alergia() {}

    public Alergia(String nombre) {
        this.nombre = nombre;
    }

    public boolean verificarOAlergia() {
        String sql = "SELECT id_alergia FROM alergia WHERE nombre = ?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, nombre);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    this.idAlergia = rs.getInt("id_alergia");
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean agregarAlergia() {
        if (!verificarOAlergia()) {
            String sql = "INSERT INTO alergia (nombre) VALUES (?)";
            try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stm.setString(1, nombre);
                int rowsInserted = stm.executeUpdate();
                if (rowsInserted > 0) {
                    try (ResultSet rs = stm.getGeneratedKeys()) {
                        if (rs.next()) {
                            this.idAlergia = rs.getInt(1);
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

    public int getIdAlergia() {
        return idAlergia;
    }

    public void setIdAlergia(int idAlergia) {
        this.idAlergia = idAlergia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
