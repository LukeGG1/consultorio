package modelos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Movimiento {
    private static final String URL = "jdbc:mysql://localhost:3306/consultorio";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private int id;
    private String tipo;
    private String motivo;
    private double monto;
    private String descripcion;
    private Date fecha;

    // Constructor para insertar un nuevo movimiento
    public Movimiento(String tipo, String motivo, double monto, String descripcion, Date fecha) {
        this.tipo = tipo;
        this.motivo = motivo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    // Constructor para editar/eliminar un movimiento existente
    public Movimiento(int id, String tipo, String motivo, double monto, String descripcion, Date fecha) {
        this.id = id;
        this.tipo = tipo;
        this.motivo = motivo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    // Constructor por defecto
    public Movimiento() {}

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    private Connection getCon() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void insertarMovimiento(Movimiento movimiento) throws SQLException {
        String sql = "INSERT INTO movimientos (tipo, motivo, monto, descripcion, fecha) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, movimiento.tipo);
            stmt.setString(2, movimiento.motivo);
            stmt.setDouble(3, movimiento.monto);
            stmt.setString(4, movimiento.descripcion);
            stmt.setDate(5, movimiento.fecha);
            stmt.executeUpdate();
        }
    }

    public static void editarMovimiento(Movimiento movimiento) {
        String sql = "UPDATE movimientos SET tipo = ?, motivo = ?, monto = ?, descripcion = ?, fecha = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, movimiento.getTipo());
            pstmt.setString(2, movimiento.getMotivo());
            pstmt.setDouble(3, movimiento.getMonto());
            pstmt.setString(4, movimiento.getDescripcion());
            pstmt.setDate(5, movimiento.getFecha());
            pstmt.setInt(6, movimiento.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void eliminarMovimiento(int id) {
        String sql = "DELETE FROM movimientos WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Movimiento> consulta() {
        List<Movimiento> listaMovimiento = new ArrayList<>();
        String sql = "SELECT * FROM movimientos";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stm = con.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String tipo = rs.getString("tipo");
                String motivo = rs.getString("motivo");
                double monto = rs.getDouble("monto");
                String descripcion = rs.getString("descripcion");
                Date fecha = rs.getDate("fecha");
                Movimiento m = new Movimiento(id, tipo, motivo, monto, descripcion, fecha);
                listaMovimiento.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Movimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaMovimiento;
    }
}
