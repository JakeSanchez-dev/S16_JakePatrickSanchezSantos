package com.empresa.eventos.reportes;

import com.empresa.eventos.config.ConexionDB;
import com.empresa.eventos.util.NavigationUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilitario para generar el reporte de eventos en PDF con JasperReports.
 * Conecta directamente a MySQL mediante JDBC y abre el PDF generado.
 */
public final class ReporteUtil {

    private ReporteUtil() {
        // Clase de utilidad, no instanciable
    }

    /**
     * Genera el reporte PDF de eventos y lo abre automaticamente.
     *
     * @return el archivo PDF generado, o null si fallo
     */
    public static File generarReportePDF() {
        String rutaReporte = "src/main/resources/reportes/reporte_eventos.jrxml";
        String rutaSalida = "reporte_eventos_" + timestamp() + ".pdf";

        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();

            // Compilar el reporte desde el JRXML
            JasperReport reporte = net.sf.jasperreports.engine.JasperCompileManager
                    .compileReport(rutaReporte);

            // Llenar el reporte con datos de la base de datos (conexion JDBC)
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, conn);

            // Exportar a PDF
            File archivoPDF = new File(rutaSalida);
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(archivoPDF));

            SimplePdfReportConfiguration config = new SimplePdfReportConfiguration();
            exporter.setConfiguration(config);

            exporter.exportReport();

            return archivoPDF;

        } catch (JRException | SQLException e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            ConexionDB.cerrarConexion(conn);
        }
    }

    private static String timestamp() {
        return DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                .format(LocalDateTime.now());
    }

    /**
     * Genera el reporte PDF y lo abre con el visor predeterminado del SO.
     *
     * @return true si se genero y abrio correctamente
     */
    public static boolean generarYAbrirReportePDF() {
        File pdf = generarReportePDF();
        if (pdf != null && pdf.exists()) {
            return NavigationUtil.abrirArchivo(pdf);
        }
        return false;
    }
}
