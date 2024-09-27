package modelos;

import clases.conexion;
import clases.sentencias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Factura extends conexion {

    private int idFactura;
    private String fecha;
    private String metodoPago;
    private float montoTotal;
    private float descuento;
    private int pacienteIdPaciente;
    private String nombre;
    private String apellido;
    private String ruc;

    // Constructor vacío
    public Factura() {
    }

    // Constructor con parámetros
    public Factura(int idFactura, String fecha, String metodoPago, float montoTotal, float descuento, int pacienteIdPaciente, String nombre, String apellido, String ruc) {
        this.idFactura = idFactura;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.montoTotal = montoTotal;
        this.descuento = descuento;
        this.pacienteIdPaciente = pacienteIdPaciente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.ruc = ruc;
    }

    // Getters y Setters
    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public float getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(float montoTotal) {
        this.montoTotal = montoTotal;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public int getPacienteIdPaciente() {
        return pacienteIdPaciente;
    }

    public void setPacienteIdPaciente(int pacienteIdPaciente) {
        this.pacienteIdPaciente = pacienteIdPaciente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    
   public int insertar() {
    String sql = "INSERT INTO factura (fecha, metodo_pago, monto_total, descuento, paciente_id_paciente, nombre, apellido, ruc) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stm.setString(1, this.fecha);
        stm.setString(2, this.metodoPago);
        stm.setFloat(3, this.montoTotal);
        stm.setFloat(4, this.descuento);
        stm.setInt(5, this.pacienteIdPaciente);
        stm.setString(6, this.nombre);
        stm.setString(7, this.apellido);
        stm.setString(8, this.ruc);
        
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected > 0) {
            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);  // Retorna el ID generado
                }
            }
        }

    } catch (SQLException ex) {
        Logger.getLogger(Factura.class.getName()).log(Level.SEVERE, null, ex);
        ex.printStackTrace();
    }
    return -1;  // Retorna -1 si ocurre algún error
}


    
    public boolean modificar() {
        String sql = "UPDATE factura SET fecha=?, metodo_pago=?, monto_total=?, descuento=?, paciente_id_paciente=?, nombre=?, apellido=?, ruc=? WHERE id_factura=?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, this.fecha);
            stm.setString(2, this.metodoPago);
            stm.setFloat(3, this.montoTotal);
            stm.setFloat(4, this.descuento);
            stm.setInt(5, this.pacienteIdPaciente);
            stm.setString(6, this.nombre);
            stm.setString(7, this.apellido);
            stm.setString(8, this.ruc);
            stm.setInt(7, this.idFactura);

            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(Factura.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    
    public boolean eliminar() {
        String sql = "UPDATE factura SET estado = ? WHERE id_factura=?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, 0);
            stm.setInt(1, this.idFactura);

            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(Factura.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    
    public ArrayList<Factura> consulta() {
        ArrayList<Factura> listaFacturas = new ArrayList<>();
        String sql = "SELECT * FROM factura";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_factura");
                String fecha = rs.getString("fecha");
                String metodoPago = rs.getString("metodo_pago");
                float montoTotal = rs.getFloat("monto_total");
                float descuento = rs.getFloat("descuento");
                int pacienteId = rs.getInt("paciente_id_paciente");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String ruc = rs.getString("ruc");

                Factura factura = new Factura(id, fecha, metodoPago, montoTotal, descuento, pacienteId, nombre, apellido, ruc);
                listaFacturas.add(factura);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Factura.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaFacturas;
    }

    @Override
    public String toString() {
        return "Factura N° " + idFactura + " - " + nombre + " " + apellido + " - " + fecha;
    }

    public Factura buscarFactura() {
        String sql = "SELECT * FROM factura WHERE id_factura = ?";
        Factura facturaBuscada = null;

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, idFactura); // Asumiendo que `idFactura` es una variable de la clase.
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_factura");
                    String fecha = rs.getString("fecha");
                    String metodoPago = rs.getString("metodo_pago");
                    float montoTotal = rs.getFloat("monto_total");
                    float descuento = rs.getFloat("descuento");
                    int pacienteId = rs.getInt("paciente_id_paciente");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String ruc = rs.getString("ruc");

                    facturaBuscada = new Factura(id, fecha, metodoPago, montoTotal, descuento, pacienteId, nombre, apellido, ruc);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Factura.class.getName()).log(Level.SEVERE, null, ex);
        }

        return facturaBuscada;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

}
