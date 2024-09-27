package modelos;

import clases.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetalleFactura extends conexion{
    private int cantidad;
    private float subtotal;
    private float descuento;
    private int facturaIdFactura;
    private int productoIdProducto;
    private String nombreProducto;

    // Constructor vacío
    public DetalleFactura() {
    }

    // Constructor con todos los atributos
    public DetalleFactura(int cantidad, float subtotal, float descuento, int facturaIdFactura, int productoIdProducto, String nombreProducto) {
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.facturaIdFactura = facturaIdFactura;
        this.productoIdProducto = productoIdProducto;
        this.nombreProducto = nombreProducto;
    }

    // Getters y Setters
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public int getFacturaIdFactura() {
        return facturaIdFactura;
    }

    public void setFacturaIdFactura(int facturaIdFactura) {
        this.facturaIdFactura = facturaIdFactura;
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

    // Método para asignar el ID del producto basado en su nombre
    public void asignarProductoIdPorNombre(String nombreProducto) {
        String sql = "SELECT id_producto FROM producto WHERE nombre = ?";
        
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, nombreProducto);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    this.productoIdProducto = rs.getInt("id_producto");
                    this.nombreProducto = nombreProducto;
                } else {
                    System.out.println("Producto no encontrado: " + nombreProducto);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // Método para agregar un nuevo detalle de factura a la base de datos
    public boolean agregarDetalleFactura() {
        String sql = "INSERT INTO detalle_factura (cantidad, subtotal, descuento, factura_id_factura, producto_id_producto) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, this.cantidad);
            stm.setFloat(2, this.subtotal);
            stm.setFloat(3, this.descuento);
            stm.setInt(4, this.facturaIdFactura);
            stm.setInt(5, this.productoIdProducto);
            
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    // Método para modificar un detalle de factura existente en la base de datos
    public boolean modificarDetalleFactura() {
        String sql = "UPDATE detalle_factura SET cantidad = ?, subtotal = ?, descuento = ? WHERE factura_id_factura = ? AND producto_id_producto = ?";
        
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, this.cantidad);
            stm.setFloat(2, this.subtotal);
            stm.setFloat(3, this.descuento);
            stm.setInt(4, this.facturaIdFactura);
            stm.setInt(5, this.productoIdProducto);
            
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Método para eliminar un detalle de factura de la base de datos
    public boolean eliminarDetalleFactura() {
        String sql = "DELETE FROM detalle_factura WHERE factura_id_factura = ? AND producto_id_producto = ?";
        
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, this.facturaIdFactura);
            stm.setInt(2, this.productoIdProducto);
            
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
