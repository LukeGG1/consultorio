package modelos;

import clases.conexion;
import clases.sentencias;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tratamiento extends conexion implements sentencias {

    private int IdTratamiento;
    private String Frecuencia;
    private String Dosis;
    private int Cantidad;
    private String Instrucciones;
    private int informeIdInforme;
    private int productoIdProducto;
    private String nombrePaciente;
    private String nombreProducto;
    private String fechaInforme;

    public Tratamiento(int IdTratamiento, String Frecuencia, String Dosis, int Cantidad, String Instrucciones, int informeIdInforme, int productoIdInProducto, String nombrePaciente, String nombreProducto, String fechaInforme) {
        this.IdTratamiento = IdTratamiento;
        this.Frecuencia = Frecuencia;
        this.Dosis = Dosis;
        this.Cantidad = Cantidad;
        this.Instrucciones = Instrucciones;
        this.informeIdInforme = informeIdInforme;
        this.productoIdProducto = productoIdInProducto;
        this.nombrePaciente = nombrePaciente;
        this.nombreProducto = nombreProducto;
        this.fechaInforme = fechaInforme;
    }

    public Tratamiento(String producto, float precio, int cantidad) {
       this.nombreProducto = producto;
       
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getFechaInforme() {
        return fechaInforme;
    }

    public void setFechaInforme(String fechaInforme) {
        this.fechaInforme = fechaInforme;
    }

    public Tratamiento() {
    }

    public int getIdTratamiento() {
        return IdTratamiento;
    }

    public void setIdTratamiento(int IdTratamiento) {
        this.IdTratamiento = IdTratamiento;
    }

    public String getFrecuencia() {
        return Frecuencia;
    }

    public void setFrecuencia(String Frecuencia) {
        this.Frecuencia = Frecuencia;
    }

    public String getDosis() {
        return Dosis;
    }

    public void setDosis(String Dosis) {
        this.Dosis = Dosis;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public String getInstrucciones() {
        return Instrucciones;
    }

    public void setInstrucciones(String Instrucciones) {
        this.Instrucciones = Instrucciones;
    }

    public int getInformeIdInforme() {
        return informeIdInforme;
    }

    public void setInformeIdInforme(int informeIdInforme) {
        this.informeIdInforme = informeIdInforme;
    }

    public int getProductoIdInProducto() {
        return productoIdProducto;
    }

    public void setProductoIdInProducto(int productoIdInProducto) {
        this.productoIdProducto = productoIdInProducto;
    }

    @Override
    public boolean insertar() {
        String sql = "INSERT INTO tratamiento(frecuencia, dosis, cantidad, instrucciones, informe_id_informe, producto_id_producto) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, this.Frecuencia);
            stm.setString(2, this.Dosis);
            stm.setInt(3, this.Cantidad);
            stm.setString(4, this.Instrucciones);
            stm.setInt(5, this.informeIdInforme);
            stm.setInt(6, this.productoIdProducto);

            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(Tratamiento.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean modificar() {
        String sql = "UPDATE tratamiento SET frecuencia = ?, dosis = ?, cantidad = ?, instrucciones = ?, informe_id_informe = ?, producto_id_producto = ? WHERE id_tratamiento = ?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, this.Frecuencia);
            stm.setString(2, this.Dosis);
            stm.setInt(3, this.Cantidad);
            stm.setString(4, this.Instrucciones);
            stm.setInt(5, this.informeIdInforme);
            stm.setInt(6, this.productoIdProducto);
            stm.setInt(7, this.IdTratamiento);

            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(Tratamiento.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean eliminar() {
        String sql = "DELETE FROM tratamiento WHERE id_tratamiento = ?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, this.IdTratamiento);

            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(Tratamiento.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
public ArrayList<Tratamiento> consulta() {
    ArrayList<Tratamiento> tratamientos = new ArrayList<>();
    String sql = "SELECT t.*, p.nombre AS nombre_producto, pa.nombre AS nombre_paciente, pa.apellido AS apellido_paciente, i.fecha AS fecha_informe " +
                 "FROM tratamiento t " +
                 "JOIN producto p ON t.producto_id_producto = p.id_producto " +
                 "JOIN informe i ON t.informe_id_informe = i.id_informe " +
                 "JOIN paciente pa ON i.paciente_id_paciente = pa.id_paciente";

    try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

        while (rs.next()) {
            Tratamiento tratamiento = new Tratamiento();
            tratamiento.setIdTratamiento(rs.getInt("id_tratamiento"));
            tratamiento.setFrecuencia(rs.getString("frecuencia"));
            tratamiento.setDosis(rs.getString("dosis"));
            tratamiento.setCantidad(rs.getInt("cantidad"));
            tratamiento.setInstrucciones(rs.getString("instrucciones"));
            tratamiento.setInformeIdInforme(rs.getInt("informe_id_informe"));
            tratamiento.setProductoIdInProducto(rs.getInt("producto_id_producto"));
            tratamiento.setNombreProducto(rs.getString("nombre_producto"));
            tratamiento.setNombrePaciente(rs.getString("nombre_paciente") + " " + rs.getString("apellido_paciente"));
            tratamiento.setFechaInforme(rs.getString("fecha_informe"));
            tratamientos.add(tratamiento);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return tratamientos;
}

public Float obtenerPrecioProducto(String nombreProducto) {
        String sql = "SELECT precio FROM producto WHERE nombre = ?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql);) {
            stm.setString(1, nombreProducto);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getFloat("precio");
            } else {
                System.out.println("Producto no encontrado: " + nombreProducto);
                return null; // O puedes lanzar una excepción
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
