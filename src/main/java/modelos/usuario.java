/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.sql.Connection;
import clases.conexion;
import clases.sentencias;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Milagros Taboada
 */
public class usuario extends conexion  implements sentencias{

    public usuario() {
    }
    
    
    
    public boolean loginAccount(String nombre, String contra) {
    String sql = "SELECT nombre, contra FROM usuario WHERE nombre = ? AND contra = ?";
    try (
        Connection con = getCon();
        PreparedStatement stm = con.prepareStatement(sql)) {
        
        stm.setString(1, nombre);
        stm.setString(2, contra);
        
        // Ejecutar la consulta y obtener resultados
        try (ResultSet rs = stm.executeQuery()) {
            // Si el conjunto de resultados tiene al menos una fila, significa que se encontró el usuario
            return rs.next(); // Usuario encontrado
            // Usuario no encontrado
        }
        
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false; // Manejo de excepciones: retorno falso en caso de error
    }
}

}
