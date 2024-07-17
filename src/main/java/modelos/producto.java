/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import modelos.producto;

/**
 *
 * @author Milagros Taboada
 */
public class producto extends conexion implements sentencias {
    private int idProducto;
    private String nombre;
    private int precio;
    private String descripcion;

    public producto() {
    }

    public producto(int idProducto, String nombre, int precio, String descripcion) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    

    @Override
    public boolean insertar() {
        String sql = "INSERT INTO `producto`( `nombre`, `precio`, `descripcion`) VALUES (?, ?, ?)";

        try (Connection con = getCon(); 
            PreparedStatement stm = con.prepareStatement(sql)) {

            // Aquí debes proporcionar el valor adecuado para producto_id_producto
            stm.setString(1, this.nombre);
            stm.setInt(2, this.precio);
            stm.setString(3, this.descripcion);

            stm.executeUpdate();

            return true;

        } catch (SQLException ex) {
            Logger.getLogger(producto.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean modificar() {
        String sql = "UPDATE producto SET nombre=?, precio=?, descripcion=? WHERE id_producto=?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            // Establecer los parámetros de la consulta
            stm.setString(1, this.nombre);
            stm.setInt(2, this.precio);
            stm.setString(3, this.descripcion);
            stm.setInt(4, this.idProducto);

            // Ejecutar la consulta de actualización
            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(producto.class.getName()).log(Level.SEVERE, "Error al modificar el producto", ex);
            return false;
        }
    }

    @Override
    public boolean eliminar() {
        String sql = "DELETE FROM producto WHERE id_producto=?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, this.idProducto);

            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(producto.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public ArrayList consulta() {
        ArrayList<producto> listaProductos = new ArrayList<>();
        String sql = "SELECT * FROM producto";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_producto");
                String nombreProducto = rs.getString("nombre");
                int precioProducto = rs.getInt("precio");
                String descripcionProducto = rs.getString("descripcion");

                producto p = new producto(id, nombreProducto, precioProducto, descripcionProducto);
                listaProductos.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(lote.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaProductos;
    }
    
}
