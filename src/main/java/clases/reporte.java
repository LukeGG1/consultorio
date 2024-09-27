/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Milagros Taboada
 */
public class reporte extends conexion {

    public reporte() {
    }

    public void generarReporte(String ubicacion, String titulo) {

        try {
            // Ruta al archivo .jasper
            String reportPath = getClass().getResource(ubicacion).getPath();

            // Parámetros del informe
            Map<String, Object> parameters = new HashMap<>();
            // Agrega parámetros según sea necesario

            // Llenar el informe
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, parameters, getCon());

            // Mostrar el informe en una nueva ventana
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle(titulo);
            viewer.setVisible(true);

        } catch (JRException ex) {
            Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void generarReportePaciente(String ubicacion, String titulo, int id_paciente) throws UnsupportedEncodingException {
        try {
            // Obtiene el recurso y verifica que no sea null
            URL resource = getClass().getResource(ubicacion);
            if (resource == null) {
                throw new FileNotFoundException("El archivo .jasper no se encontró en la ubicación: " + ubicacion);
            }

            // Decodifica la ruta en caso de que tenga espacios u otros caracteres especiales
            String reportPath = URLDecoder.decode(resource.getPath(), "UTF-8");

            // Parámetros del informe
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id_paciente", id_paciente);
            // Agrega otros parámetros según sea necesario

            // Llenar el informe
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, parameters, getCon());

            // Mostrar el informe en una nueva ventana
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle(titulo);
            viewer.setVisible(true);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, "Archivo .jasper no encontrado: " + ex.getMessage());
        } catch (JRException ex) {
            Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generarReporteInforme(String ubicacion, String titulo, int id_informe) throws UnsupportedEncodingException {
        try {
            // Obtiene el recurso y verifica que no sea null
            URL resource = getClass().getResource(ubicacion);
            if (resource == null) {
                throw new FileNotFoundException("El archivo .jasper no se encontró en la ubicación: " + ubicacion);
            }

            // Decodifica la ruta en caso de que tenga espacios u otros caracteres especiales
            String reportPath = URLDecoder.decode(resource.getPath(), "UTF-8");

            // Parámetros del informe
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id_informe", id_informe);
            // Agrega otros parámetros según sea necesario

            // Llenar el informe
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, parameters, getCon());

            // Mostrar el informe en una nueva ventana
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setSize(1200, 800);
            viewer.setTitle(titulo);
            viewer.setVisible(true);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, "Archivo .jasper no encontrado: " + ex.getMessage());
        } catch (JRException ex) {
            Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirAyuda() throws IOException {
        String ubicacion = "/empresa/consultorio/ConsultorioMendoza.chm";
        try {
            // Obtiene el recurso y verifica que no sea null
            URL resource = getClass().getResource(ubicacion);
            if (resource == null) {
                throw new FileNotFoundException("El archivo .chm no se encontró en la ubicación: " + ubicacion);
            }

            // Decodifica la ruta en caso de que tenga espacios u otros caracteres especiales
            String filePath = URLDecoder.decode(resource.getPath(), "UTF-8");
            File file = new File(filePath);

            // Verifica si el archivo existe
            if (file.exists()) {
                // Verifica si Desktop está soportado antes de abrir el archivo
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    System.out.println("La funcionalidad de Desktop no está soportada en este sistema.");
                }
            } else {
                System.out.println("El archivo CHM no existe.");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, "Archivo .chm no encontrado: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, "Error al intentar abrir el archivo CHM: " + ex.getMessage(), ex);
        }
    }
}
