package modelos;

import java.sql.Connection;
import clases.sentencias;
import clases.conexion;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author luism
 */
public class Paciente extends conexion implements sentencias {

    private int idPaciente;
    private String nombre;
    private String apellido;
    private String sexo;
    private int edad;
    private String correo;
    private int telefono;

    public Paciente() {
    }

    public Paciente(int idPaciente, String nombre, String apellido, String sexo, int edad, String correo, int telefono) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.sexo = sexo;
        this.edad = edad;
        this.correo = correo;
        this.telefono = telefono;
    }

    

    public boolean createPaciente() {
        String sql = "INSERT INTO paciente(nombre, apellido, sexo, edad, correo, telefono) VALUES (?,?,?,?,?,?)";
        try (
                Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, getNombre());
            stm.setString(2, getApellido());
            stm.setString(3, getSexo());
            stm.setInt(4, getEdad());
            stm.setString(5, getCorreo());
            stm.setInt(6, getTelefono());

            int rowsInserted = stm.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Manejo de excepciones: retorno falso en caso de error
        }
    }

    public boolean agregarAlergiaAPaciente(int idAlergia) {
        String sql = "INSERT INTO paciente_has_alergia (paciente_id_paciente, alergia_id_alergia) VALUES (?, ?)";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, getIdPaciente());
            stm.setInt(2, idAlergia);
            int rowsInserted = stm.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean agregarCirugiaAPaciente(int idCirugia) {
        String sql = "INSERT INTO paciente_has_cirugia (paciente_id_paciente, cirugia_id_cirugia) VALUES (?, ?)";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, getIdPaciente());
            stm.setInt(2, idCirugia);
            int rowsInserted = stm.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    @Override
    public boolean insertar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean modificar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList consulta() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
