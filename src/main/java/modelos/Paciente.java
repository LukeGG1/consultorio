package modelos;

import clases.sentencias;
import clases.conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Paciente extends conexion implements sentencias {

    private int idPaciente;
    private String nombre;
    private String apellido;
    private String sexo;
    private String fechaNac;
    private String correo;
    private String telefono;

    public Paciente() {
    }

    public Paciente(int idPaciente, String nombre, String apellido, String sexo, String fechaNac, String correo, String telefono) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.sexo = sexo;
        this.fechaNac = fechaNac;
        this.correo = correo;
        this.telefono = telefono;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
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

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean createPaciente() {
        String sql = "INSERT INTO paciente(nombre, apellido, sexo, fecha_nac, correo, telefono) VALUES (?,?,?,?,?,?)";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stm.setString(1, getNombre());
            stm.setString(2, getApellido());
            stm.setString(3, getSexo());
            stm.setString(4, getFechaNac());
            stm.setString(5, getCorreo());
            stm.setString(6, getTelefono());

            int rowsInserted = stm.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        setIdPaciente(generatedKeys.getInt(1));
                        return true;
                    } else {
                        throw new SQLException("No se pudo obtener el ID del paciente insertado.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void agregarAlergiaAPaciente(String alergia) throws SQLException {
        Connection con = getCon();
        String sqlSelect = "SELECT COUNT(*) FROM paciente_has_alergia WHERE paciente_id_paciente = ? AND alergia_id_alergia = ?";
        String sqlInsert = "INSERT INTO paciente_has_alergia (paciente_id_paciente, alergia_id_alergia) VALUES (?, ?)";

        try (PreparedStatement stmtSelect = con.prepareStatement(sqlSelect); PreparedStatement stmtInsert = con.prepareStatement(sqlInsert)) {

            stmtSelect.setInt(1, this.idPaciente);
            stmtSelect.setInt(2, obtenerIdAlergia(alergia));
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) { // Verifica si no existe la entrada
                stmtInsert.setInt(1, this.idPaciente);
                stmtInsert.setInt(2, obtenerIdAlergia(alergia));
                stmtInsert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    private int obtenerIdAlergia(String alergia) throws SQLException {
        Connection con = getCon();
        String sql = "SELECT id_alergia FROM alergia WHERE nombre = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, alergia);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_alergia");
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return 0;
    }

    public boolean agregarCirugiaAPaciente(String nombreCirugia) {
        String sql = "INSERT INTO paciente_has_cirugia (paciente_id_paciente, cirugia_id_cirugia) VALUES (?, (SELECT id_cirugia FROM cirugia WHERE nombre = ?))";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, getIdPaciente());
            stm.setString(2, nombreCirugia);
            int rowsInserted = stm.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Getters y setters...
    @Override
    public boolean insertar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean modificar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean eliminar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<Paciente> consulta() {
        ArrayList<Paciente> listaPacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_paciente");
                String nombreP = rs.getString("nombre");
                String apellidoP = rs.getString("apellido");
                String sexoP = rs.getString("sexo");
                String fechaNacP = rs.getString("fecha_nac");
                String correoP = rs.getString("correo");
                String telefonoP = rs.getString("telefono");

                Paciente p = new Paciente(id, nombreP, apellidoP, sexoP, fechaNacP, correoP, telefonoP);
                listaPacientes.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Paciente.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaPacientes;
    }

    public List<String> getAlergias() {
        List<String> alergias = new ArrayList<>();
        try (Connection con=getCon()) {
            String sql = "SELECT a.nombre FROM alergia a INNER JOIN paciente_has_alergia pa ON a.id_alergia = pa.alergia_id_alergia WHERE pa.paciente_id_paciente = " + this.idPaciente;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                alergias.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alergias;
    }

    public List<String> getCirugias() {
        List<String> cirugias = new ArrayList<>();
        try (Connection con=getCon()) {
            String sql = "SELECT c.nombre FROM cirugia c INNER JOIN paciente_has_cirugia pc ON c.id_cirugia = pc.cirugia_id_cirugia WHERE pc.paciente_id_paciente = " + this.idPaciente;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cirugias.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cirugias;
    }

    public boolean modificarPaciente(String nombre, String apellido, String sexo, String fechaNac, String correo, String telefono, List<String> nuevasAlergias, List<String> nuevasCirugias) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.sexo = sexo;
        this.fechaNac = fechaNac;
        this.correo = correo;
        this.telefono = telefono;

        try (Connection con=getCon()) {
            con.setAutoCommit(false);
            // Verificar y actualizar alergias
            List<String> alergiasExistentes = getAlergias();
            for (String alergia : alergiasExistentes) {
                if (!nuevasAlergias.contains(alergia)) {
                    eliminarAlergia(alergia);
                }
            }
            for (String alergia : nuevasAlergias) {
                if (!alergiasExistentes.contains(alergia)) {
                    verificarOAlergia(alergia); // Verificar y agregar si no existe
                    agregarAlergiaAPaciente(alergia);
                }
            }

            // Verificar y actualizar cirugías
            List<String> cirugiasExistentes = getCirugias();
            for (String cirugia : cirugiasExistentes) {
                if (!nuevasCirugias.contains(cirugia)) {
                    eliminarCirugia(cirugia);
                }
            }
            for (String cirugia : nuevasCirugias) {
                if (!cirugiasExistentes.contains(cirugia)) {
                    verificarOCirugia(cirugia); // Verificar y agregar si no existe
                    agregarCirugiaAPaciente(cirugia);
                }
            }

            // Actualizar información básica del paciente
            String updatePacienteSQL = "UPDATE paciente SET nombre = ?, apellido = ?, sexo = ?, fecha_nac = ?, correo = ?, telefono = ? WHERE id_paciente = ?";
            try (PreparedStatement pstmt = con.prepareStatement(updatePacienteSQL)) {
                pstmt.setString(1, this.nombre);
                pstmt.setString(2, this.apellido);
                pstmt.setString(3, this.sexo);
                pstmt.setString(4, this.fechaNac);
                pstmt.setString(5, this.correo);
                pstmt.setString(6, this.telefono);
                pstmt.setInt(7, this.idPaciente);
                pstmt.executeUpdate();
            }

            

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void eliminarAlergia(String nombreAlergia) {
        try (Connection con=getCon()) {
            String sql = "DELETE FROM paciente_has_alergia WHERE paciente_id_paciente = ? AND alergia_id_alergia = (SELECT id_alergia FROM alergia WHERE nombre = ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, this.idPaciente);
                pstmt.setString(2, nombreAlergia);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarCirugia(String nombreCirugia) {
        try (Connection con=getCon()) {
            String sql = "DELETE FROM paciente_has_cirugia WHERE paciente_id_paciente = ? AND cirugia_id_cirugia = (SELECT id_cirugia FROM cirugia WHERE nombre = ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, this.idPaciente);
                pstmt.setString(2, nombreCirugia);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean eliminarPaciente() {
        Connection con=getCon();
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            

            // Iniciar transacción
            con.setAutoCommit(false);

            // Eliminar relaciones con alergias
            String deleteAlergiasSQL = "DELETE FROM paciente_has_alergia WHERE paciente_id_paciente = ?";
            pstmt = con.prepareStatement(deleteAlergiasSQL);
            pstmt.setInt(1, this.idPaciente);
            pstmt.executeUpdate();

            // Eliminar relaciones con cirugías
            String deleteCirugiasSQL = "DELETE FROM paciente_has_cirugia WHERE paciente_id_paciente = ?";
            pstmt = con.prepareStatement(deleteCirugiasSQL);
            pstmt.setInt(1, this.idPaciente);
            pstmt.executeUpdate();

            // Eliminar paciente
            String deletePacienteSQL = "DELETE FROM paciente WHERE id_paciente = ?";
            pstmt = con.prepareStatement(deletePacienteSQL);
            pstmt.setInt(1, this.idPaciente);
            pstmt.executeUpdate();

            // Confirmar transacción
            con.commit();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    private boolean agregarAlergia(String nombreAlergia) {
        String sql = "INSERT INTO alergia (nombre) VALUES (?)";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, nombreAlergia);
            int rowsInserted = stm.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean agregarCirugia(String nombreCirugia) {
        String sql = "INSERT INTO cirugia (nombre) VALUES (?)";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, nombreCirugia);
            int rowsInserted = stm.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    // Métodos para verificar y agregar alergias y cirugías si no existen

    public boolean verificarOAlergia(String nombreAlergia) {
        String sql = "SELECT id_alergia FROM alergia WHERE nombre = ?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, nombreAlergia);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return true; // La alergia existe
            } else {
                // La alergia no existe, agregarla
                return agregarAlergia(nombreAlergia);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean verificarOCirugia(String nombreCirugia) {
        String sql = "SELECT id_cirugia FROM cirugia WHERE nombre = ?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, nombreCirugia);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return true; // La cirugía existe
            } else {
                // La cirugía no existe, agregarla
                return agregarCirugia(nombreCirugia);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
