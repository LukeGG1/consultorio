package modelos;

import clases.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Movimiento {

    private int id;
    private String tipo;
    private String motivo;
    private double monto;
    private String descripcion;
    private Timestamp fecha;

    // Constructor para insertar un nuevo movimiento
    public Movimiento(String tipo, String motivo, double monto, String descripcion, Timestamp fecha) {
        this.tipo = tipo;
        this.motivo = motivo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    // Constructor para editar/eliminar un movimiento existente
    public Movimiento(int id, String tipo, String motivo, double monto, String descripcion, Timestamp fecha) {
        this.id = id;
        this.tipo = tipo;
        this.motivo = motivo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMotivo() {
        return motivo;
    }

    public double getMonto() {
        return monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public static void insertarMovimiento(Movimiento movimiento) {
        String sql = "INSERT INTO movimiento (tipo, motivo, monto, descripcion, fecha) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, movimiento.getTipo());
            pstmt.setString(2, movimiento.getMotivo());
            pstmt.setDouble(3, movimiento.getMonto());
            pstmt.setString(4, movimiento.getDescripcion());
            pstmt.setTimestamp(5, movimiento.getFecha());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editarMovimiento(Movimiento movimiento) {
        String sql = "UPDATE movimientos SET tipo = ?, motivo = ?, monto = ?, descripcion = ?, fecha = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, movimiento.getTipo());
            pstmt.setString(2, movimiento.getMotivo());
            pstmt.setDouble(3, movimiento.getMonto());
            pstmt.setString(4, movimiento.getDescripcion());
            pstmt.setTimestamp(5, movimiento.getFecha());
            pstmt.setInt(6, movimiento.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void eliminarMovimiento(int id) {
        String sql = "DELETE FROM movimientos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}