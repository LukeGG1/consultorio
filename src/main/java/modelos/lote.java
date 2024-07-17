package modelos;

import clases.conexion;
import clases.sentencias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class lote extends conexion implements sentencias {

    private int idLote;
    private String fechaLote;
    private String fechaFabricacion;
    private String fechaVencimiento;
    private int costoLote;
    private int cantidad;
    private int productoIdProducto;
    private String nombreProducto; // Añadir el nombre del producto

    public lote() {
    }

    public lote(int idLote, String fechaLote, String fechaFabricacion, String fechaVencimiento, int costoLote, int cantidad, int productoIdProducto, String nombreProducto) {
        this.idLote = idLote;
        this.fechaLote = fechaLote;
        this.fechaFabricacion = fechaFabricacion;
        this.fechaVencimiento = fechaVencimiento;
        this.costoLote = costoLote;
        this.cantidad = cantidad;
        this.productoIdProducto = productoIdProducto;
        this.nombreProducto = nombreProducto;
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public String getFechaLote() {
        return fechaLote;
    }

    public void setFechaLote(String fechaLote) {
        this.fechaLote = fechaLote;
    }

    public String getFechaFabricacion() {
        return fechaFabricacion;
    }

    public void setFechaFabricacion(String fechaFabricacion) {
        this.fechaFabricacion = fechaFabricacion;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getCostoLote() {
        return costoLote;
    }

    public void setCostoLote(int costoLote) {
        this.costoLote = costoLote;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getProductoIdProducto() {
        return productoIdProducto;
    }

    public void setProductoIdProducto(int productoIdProducto) {
        this.productoIdProducto = productoIdProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    @Override
    public boolean insertar() {
        String sql = "INSERT INTO lote (fecha_lote, fecha_fabricacion, fecha_vencimiento, costo_lote, cantidad, producto_id_producto) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, this.fechaLote);
            stm.setString(2, this.fechaFabricacion);
            stm.setString(3, this.fechaVencimiento);
            stm.setInt(4, this.costoLote);
            stm.setInt(5, this.cantidad);
            stm.setInt(6, this.productoIdProducto);

            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(lote.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean modificar() {
        // Consulta SQL para actualizar el registro de lote
        String sql = "UPDATE lote SET fecha_lote=?, fecha_fabricacion=?, fecha_vencimiento=?, costo_lote=?, cantidad=?, producto_id_producto=? WHERE id_lote=?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            // Establecer los parámetros de la consulta
            stm.setString(1, this.fechaLote);
            stm.setString(2, this.fechaFabricacion);
            stm.setString(3, this.fechaVencimiento);
            stm.setInt(4, this.costoLote);
            stm.setInt(5, this.cantidad);
            stm.setInt(6, this.productoIdProducto);
            stm.setInt(7, this.idLote);

            // Ejecutar la consulta de actualización
            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(lote.class.getName()).log(Level.SEVERE, "Error al modificar el lote", ex);
            return false;
        }
    }

    @Override
    public boolean eliminar() {
        String sql = "DELETE FROM lote WHERE id_lote=?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, this.idLote);

            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(lote.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public ArrayList<lote> consulta() {
        ArrayList<lote> listaLotes = new ArrayList<>();
        String sql = "SELECT l.*, p.nombre FROM lote l JOIN producto p ON l.producto_id_producto = p.id_producto";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_lote");
                String fechaLote = rs.getString("fecha_lote");
                String fechaFabricacion = rs.getString("fecha_fabricacion");
                String fechaVencimiento = rs.getString("fecha_vencimiento");
                int costoLote = rs.getInt("costo_lote");
                int cantidad = rs.getInt("cantidad");
                int productoIdProducto = rs.getInt("producto_id_producto");
                String nombreProducto = rs.getString("nombre");

                lote l = new lote(id, fechaLote, fechaFabricacion, fechaVencimiento, costoLote, cantidad, productoIdProducto, nombreProducto);
                listaLotes.add(l);
            }

        } catch (SQLException ex) {
            Logger.getLogger(lote.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaLotes;
    }

}
