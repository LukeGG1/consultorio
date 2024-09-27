package modelos;

import clases.conexion;
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

public class Movimiento extends conexion {

    private int id;
    private String tipo;
    private String motivo;
    private double monto;
    private String descripcion;
    private Date fecha; // Cambiado a java.sql.Date

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

    public boolean insertar() throws SQLException {
        String sql = "INSERT INTO movimiento (tipo, motivo, monto, descripcion, fecha) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = getCon();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, getTipo());
            stmt.setString(2, getMotivo());
            stmt.setDouble(3, getMonto());
            stmt.setString(4, getDescripcion());
            stmt.setDate(5, getFecha()); // Cambiado a setDate
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Manejo de excepciones: retorno falso en caso de error
        }
    }

    public boolean modificar() {
        String sql = "UPDATE movimiento SET tipo = ?, motivo = ?, monto = ?, descripcion = ?, fecha = ? WHERE idMovimiento = ?";
        try (Connection con = getCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, getTipo());
            pstmt.setString(2, getMotivo());
            pstmt.setDouble(3, getMonto());
            pstmt.setString(4, getDescripcion());
            pstmt.setDate(5, getFecha());
            pstmt.setInt(6, getId());
            pstmt.executeUpdate();
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar() {
        String sql = "DELETE FROM movimiento WHERE idMovimiento = ?";
        try (Connection con = getCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, getId());
            
            int filasAfectadas = pstmt.executeUpdate();

                return filasAfectadas > 0;
                
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Movimiento> consulta() {
        List<Movimiento> listaMovimiento = new ArrayList<>();
        String sql = "SELECT * FROM movimiento";
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("idMovimiento");
                String tipo = rs.getString("tipo");
                String motivo = rs.getString("motivo");
                double monto = rs.getDouble("monto");
                String descripcion = rs.getString("descripcion");
                Date fecha = rs.getDate("fecha"); // Cambiado a getDate
                Movimiento m = new Movimiento(id, tipo, motivo, monto, descripcion, fecha);
                listaMovimiento.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Movimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaMovimiento;
    }
}
