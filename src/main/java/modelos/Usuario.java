/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import clases.HashPassword;
import java.sql.Connection;
import clases.conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Milagros Taboada
 */
public class Usuario extends conexion {
    private int id;
    private String password;
    private String username;
    private int nivel;

    public Usuario(int id, String password, String username, int nivel) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.nivel = nivel;
    }

    public Usuario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public boolean insert() {
        String sql = "INSERT INTO `usuario`(`contra`, `nombre`, `nivel`) VALUES (?, ?, ?)";
        
        try (
            Connection con = getCon();
            PreparedStatement stm = con.prepareStatement(sql)
            )
        {
            stm.setString(1, HashPassword.hash(this.password));
            stm.setString(2, this.username);
            stm.setInt(3, this.nivel);
            
            stm.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }

    }

    public boolean modify() {
        String sql = "update usuario set contra = ?, nombre = ?, nivel = ? where id_usuario = ?";
        
        try (
            Connection con = getCon();
            PreparedStatement stm = con.prepareStatement(sql)
            )
        {
            stm.setString(1, HashPassword.hash(this.password));
            stm.setString(2, this.username);
            stm.setInt(3, this.nivel);
            stm.setInt(4, this.id);
            
            stm.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }

    }

    public boolean delete() {
        String sql = "delete from usuario where id_usuario = ?";
        
        try (
            Connection con = getCon();
            PreparedStatement stm = con.prepareStatement(sql)
            )
        {
            stm.setInt(1, this.id);
            
            stm.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }

    }

    public Optional<String> findPassword(){
        String sql = "select contra from usuario where nombre = ?";
        Optional<String> password = Optional.empty();

        try (Connection link = getCon();
            PreparedStatement pstmt = link.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    password = Optional.ofNullable(rs.getString("contra"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }

        return password;
    }
}
