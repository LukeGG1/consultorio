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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Milagros Taboada
 */
public class Informe extends conexion implements sentencias {
    private int idInforme;
    private String motivoConsulta;
    private String hallazgos;
    private String fecha;
    private String nombrePaciente; 
    private int pacienteIdPaciente;
    private byte[] imagenOjoIzquierdo;
    private byte[] imagenOjoDerecho;

    public Informe(int idInforme, String motivoConsulta, String hallazgos, String fecha, String nombrePaciente, int pacienteIdPaciente) {
        this.idInforme = idInforme;
        this.motivoConsulta = motivoConsulta;
        this.hallazgos = hallazgos;
        this.fecha = fecha;
        this.nombrePaciente = nombrePaciente;
        this.pacienteIdPaciente = pacienteIdPaciente;
    }
    
    
    public Informe(int idInforme, String motivoConsulta, String hallazgos, String fecha, String nombrePaciente, int pacienteIdPaciente, byte[] imagenOjoIzquierdo, byte[] imagenOjoDerecho) {
        this.idInforme = idInforme;
        this.motivoConsulta = motivoConsulta;
        this.hallazgos = hallazgos;
        this.fecha = fecha;
        this.nombrePaciente = nombrePaciente;
        this.pacienteIdPaciente = pacienteIdPaciente;
        this.imagenOjoIzquierdo = imagenOjoIzquierdo;
        this.imagenOjoDerecho = imagenOjoDerecho;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public Informe() {
    }

    public int getIdInforme() {
        return idInforme;
    }

    public void setIdInforme(int idInforme) {
        this.idInforme = idInforme;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getHallazgos() {
        return hallazgos;
    }

    public void setHallazgos(String hallazgos) {
        this.hallazgos = hallazgos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getPacienteIdPaciente() {
        return pacienteIdPaciente;
    }

    public void setPacienteIdPaciente(int pacienteIdPaciente) {
        this.pacienteIdPaciente = pacienteIdPaciente;
    }

    public byte[] getImagenOjoIzquierdo() {
        return imagenOjoIzquierdo;
    }

    public void setImagenOjoIzquierdo(byte[] imagenOjoIzquierdo) {
        this.imagenOjoIzquierdo = imagenOjoIzquierdo;
    }

    public byte[] getImagenOjoDerecho() {
        return imagenOjoDerecho;
    }

    public void setImagenOjoDerecho(byte[] imagenOjoDerecho) {
        this.imagenOjoDerecho = imagenOjoDerecho;
    }

    @Override
    public boolean insertar() {
        String sql = "INSERT INTO informe (motivo_consulta, hallazgos, fecha, paciente_id_paciente, ojo_i, ojo_d) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, this.motivoConsulta);
            stm.setString(2, this.hallazgos);
            stm.setString(3, this.fecha);
            stm.setInt(4, this.pacienteIdPaciente);
            stm.setBytes(5, this.imagenOjoIzquierdo);
            stm.setBytes(6, this.imagenOjoDerecho);
            
            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(Informe.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean modificar() {
        String sql = "UPDATE informe SET motivo_consulta=?, hallazgos=?, fecha=?, paciente_id_paciente=?, ojo_i=?, ojo_d=? WHERE id_informe=?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            // Establecer los parámetros de la consulta
            stm.setString(1, this.motivoConsulta);
            stm.setString(2, this.hallazgos);
            stm.setString(3, this.fecha);
            stm.setInt(4, this.pacienteIdPaciente);
            stm.setBytes(5, this.imagenOjoIzquierdo);
            stm.setBytes(6, this.imagenOjoDerecho);
            stm.setInt(7, this.idInforme);

            // Ejecutar la consulta de actualización
            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(Informe.class.getName()).log(Level.SEVERE, "Error al modificar el Informe", ex);
            return false;
        }
    }

    @Override
    public boolean eliminar() {
        String sql = "DELETE FROM informe WHERE id_informe=?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, this.idInforme);

            int filasAfectadas = stm.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            Logger.getLogger(Lote.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public ArrayList<Informe> consulta() {
        ArrayList<Informe> listaInformes = new ArrayList<>();
        String sql = "SELECT i.*, p.nombre FROM informe i JOIN paciente p ON i.paciente_id_paciente = p.id_paciente";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_informe");
                String MotivoM = rs.getString("motivo_consulta");
                String Hallazgos = rs.getString("hallazgos");
                String fecha = rs.getString("fecha");
                int pacienteId = rs.getInt("paciente_id_paciente");
                String nombreP = rs.getString("nombre");
                byte[] imagenIzquierda = rs.getBytes("ojo_i");
                byte[] imagenDerecha = rs.getBytes("ojo_d");

                Informe i = new Informe(id, MotivoM, Hallazgos, fecha, nombreP, pacienteId, imagenIzquierda, imagenDerecha);
                listaInformes.add(i);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Lote.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaInformes;
    }
    
    @Override
    public String toString() {
        return nombrePaciente + " - " + fecha;
    }
}
