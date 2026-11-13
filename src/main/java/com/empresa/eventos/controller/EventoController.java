package com.empresa.eventos.controller;

import com.empresa.eventos.dao.EventoDAO;
import com.empresa.eventos.dao.EventoDAOImpl;
import com.empresa.eventos.model.Evento;
import com.empresa.eventos.reportes.ReporteUtil;
import com.empresa.eventos.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador principal de la vista de gestion de eventos.
 * Implementa el CRUD completo, busqueda, filtrado y exportacion a PDF.
 */
public class EventoController implements Initializable {

    private final EventoDAO eventoDAO = new EventoDAOImpl();
    private final ObservableList<Evento> listaEventos = FXCollections.observableArrayList();

    private Evento eventoSeleccionado; // evento en edicion

    // ==================== Encabezado y estado ====================
    @FXML private Label lblEstadoConexion;

    // ==================== Formulario (panel izquierdo) ====================
    @FXML private TextField txtNombre;
    @FXML private TextField txtCliente;
    @FXML private TextField txtLugar;
    @FXML private ComboBox<String> cbTipoEvento;
    @FXML private ComboBox<String> cbEstadoFormulario;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtPresupuesto;

    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    // ==================== Tabla y busqueda (panel derecho) ====================
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cbFiltroEstado;
    @FXML private Button btnRefrescar;
    @FXML private Button btnExportarPDF;

    @FXML private TableView<Evento> tablaEventos;
    @FXML private TableColumn<Evento, Integer> colId;
    @FXML private TableColumn<Evento, String> colNombre;
    @FXML private TableColumn<Evento, String> colTipo;
    @FXML private TableColumn<Evento, String> colCliente;
    @FXML private TableColumn<Evento, String> colFecha;
    @FXML private TableColumn<Evento, String> colLugar;
    @FXML private TableColumn<Evento, String> colPresupuesto;
    @FXML private TableColumn<Evento, String> colEstado;

    // Valores validos para los ComboBox
    private static final String[] TIPOS_EVENTO = {
            "Boda", "Corporativo", "Conferencia", "Concierto", "Graduación", "Cumpleaños"
    };
    private static final String[] ESTADOS_EVENTO = {
            "Planificado", "Confirmado", "En ejecucion", "Finalizado", "Cancelado"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Configurar combos de tipo y estado
        cbTipoEvento.getItems().addAll(TIPOS_EVENTO);
        cbEstadoFormulario.getItems().addAll(ESTADOS_EVENTO);
        cbFiltroEstado.getItems().add("Todos");
        cbFiltroEstado.getItems().addAll(ESTADOS_EVENTO);
        cbFiltroEstado.setValue("Todos");

        // 2. Configurar columnas de la tabla
        configurarColumnas();

        // 3. Listener de seleccion de fila: cargar en formulario
        tablaEventos.getSelectionModel().selectedItemProperty().addListener((obs, antigua, nueva) -> {
            if (nueva != null) {
                cargarEventoEnFormulario(nueva);
            }
        });

        // 4. Listener de busqueda dinamica
        txtBuscar.textProperty().addListener((obs, antigua, nueva) -> {
            aplicarFiltroYBusqueda();
        });

        // 5. Listener de filtro por estado
        cbFiltroEstado.getSelectionModel().selectedItemProperty().addListener((obs, antigua, nueva) -> {
            aplicarFiltroYBusqueda();
        });

        // 6. Estado inicial de botones de edicion/eliminacion
        btnActualizar.setDisable(true);
        btnEliminar.setDisable(true);

        // 7. Cargar datos y verificar conexion
        cargarTabla();
        verificarConexion();
    }

    /** Configura las columnas de la TableView con sus respectivas propiedades de celda. */
    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEvento"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEvento"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoEvento"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colFecha.setCellValueFactory(cell ->
                new javafx.beans.property.ReadOnlyStringWrapper(
                        cell.getValue().getFechaEvento() != null
                                ? cell.getValue().getFechaEvento().toString()
                                : ""
                )
        );
        colLugar.setCellValueFactory(new PropertyValueFactory<>("lugar"));
        colPresupuesto.setCellValueFactory(cell ->
                new javafx.beans.property.ReadOnlyStringWrapper(
                        cell.getValue().getPresupuesto() != null
                                ? String.format("S/ %.2f", cell.getValue().getPresupuesto())
                                : "S/ 0.00"
                )
        );
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Ajuste de ancho para que se vea bien
        tablaEventos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /** Verifica la conexion a MySQL y actualiza la etiqueta de estado. */
    private void verificarConexion() {
        try (var conn = com.empresa.eventos.config.ConexionDB.getConexion()) {
            if (conn != null && conn.isValid(2)) {
                lblEstadoConexion.setText("Estado: Conectado a MySQL");
                lblEstadoConexion.getStyleClass().add("estado-conectado");
            }
        } catch (Exception e) {
            lblEstadoConexion.setText("Estado: Sin conexion a MySQL");
            lblEstadoConexion.getStyleClass().add("estado-desconectado");
            AlertUtil.mostrarError(
                    "Error de conexion",
                    "No se pudo conectar a la base de datos MySQL.\n"
                            + "Verifique que el servidor este encendido y las credenciales en ConexionDB.java.\n"
                            + "Detalle: " + e.getMessage()
            );
        }
    }

    /** Carga todos los eventos en la tabla. */
    private void cargarTabla() {
        try {
            List<Evento> eventos = eventoDAO.listar();
            listaEventos.setAll(eventos);
            tablaEventos.setItems(listaEventos);
        } catch (Exception e) {
            AlertUtil.mostrarError("Error al cargar", e.getMessage());
        }
    }

    /** Aplica busqueda por texto y filtro de estado sobre la lista cargada. */
    private void aplicarFiltroYBusqueda() {
        String texto = txtBuscar.getText() == null ? "" : txtBuscar.getText().trim().toLowerCase();
        String estadoFiltro = cbFiltroEstado.getValue();
        boolean filtroActivo = !estadoFiltro.equals("Todos");

        listaEventos.setAll(eventoDAO.listar().stream()
                .filter(e -> {
                    boolean coincideTexto = texto.isEmpty()
                            || (e.getNombreEvento() != null && e.getNombreEvento().toLowerCase().contains(texto))
                            || (e.getCliente() != null && e.getCliente().toLowerCase().contains(texto));
                    boolean coincideEstado = !filtroActivo
                            || (e.getEstado() != null && e.getEstado().equalsIgnoreCase(estadoFiltro));
                    return coincideTexto && coincideEstado;
                })
                .toList()
        );
        tablaEventos.setItems(listaEventos);
    }

    /** Carga los datos de un evento seleccionado en el formulario para edicion. */
    private void cargarEventoEnFormulario(Evento evento) {
        this.eventoSeleccionado = evento;
        txtNombre.setText(evento.getNombreEvento());
        txtCliente.setText(evento.getCliente());
        txtLugar.setText(evento.getLugar());
        cbTipoEvento.setValue(evento.getTipoEvento());
        cbEstadoFormulario.setValue(evento.getEstado());
        dpFecha.setValue(evento.getFechaEvento());
        txtPresupuesto.setText(
                evento.getPresupuesto() != null ? evento.getPresupuesto().toString() : ""
        );

        btnGuardar.setDisable(true);
        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    /** Limpia el formulario y deselecciona la tabla. */
    @FXML
    private void limpiarFormulario() {
        tablaEventos.getSelectionModel().clearSelection();
        this.eventoSeleccionado = null;

        txtNombre.clear();
        txtCliente.clear();
        txtLugar.clear();
        cbTipoEvento.setValue(null);
        cbEstadoFormulario.setValue(null);
        dpFecha.setValue(null);
        txtPresupuesto.clear();

        btnGuardar.setDisable(false);
        btnActualizar.setDisable(true);
        btnEliminar.setDisable(true);
        txtNombre.requestFocus();
    }

    /** Valida los campos del formulario antes de guardar o actualizar. */
    private boolean validarFormulario() {
        if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
            AlertUtil.mostrarError("Validacion", "Ingrese el nombre del evento.");
            txtNombre.requestFocus();
            return false;
        }
        if (txtCliente.getText() == null || txtCliente.getText().trim().isEmpty()) {
            AlertUtil.mostrarError("Validacion", "Ingrese el nombre del cliente.");
            txtCliente.requestFocus();
            return false;
        }
        if (cbTipoEvento.getValue() == null) {
            AlertUtil.mostrarError("Validacion", "Seleccione el tipo de evento.");
            cbTipoEvento.requestFocus();
            return false;
        }
        if (dpFecha.getValue() == null) {
            AlertUtil.mostrarError("Validacion", "Seleccione la fecha del evento.");
            dpFecha.requestFocus();
            return false;
        }
        if (txtLugar.getText() == null || txtLugar.getText().trim().isEmpty()) {
            AlertUtil.mostrarError("Validacion", "Ingrese el lugar del evento.");
            txtLugar.requestFocus();
            return false;
        }
        if (txtPresupuesto.getText() == null || txtPresupuesto.getText().trim().isEmpty()) {
            AlertUtil.mostrarError("Validacion", "Ingrese el presupuesto.");
            txtPresupuesto.requestFocus();
            return false;
        }
        try {
            BigDecimal presupuesto = new BigDecimal(txtPresupuesto.getText().trim());
            if (presupuesto.compareTo(BigDecimal.ZERO) <= 0) {
                AlertUtil.mostrarError("Validacion", "El presupuesto debe ser mayor a 0.");
                txtPresupuesto.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.mostrarError("Validacion", "El presupuesto debe ser un numero valido.");
            txtPresupuesto.requestFocus();
            return false;
        }
        if (cbEstadoFormulario.getValue() == null) {
            AlertUtil.mostrarError("Validacion", "Seleccione el estado del evento.");
            cbEstadoFormulario.requestFocus();
            return false;
        }
        return true;
    }

    /** Construye un objeto Evento a partir de los campos del formulario. */
    private Evento construirEventoDesdeFormulario() {
        Evento evento = new Evento();
        if (eventoSeleccionado != null) {
            evento.setIdEvento(eventoSeleccionado.getIdEvento());
        }
        evento.setNombreEvento(txtNombre.getText().trim());
        evento.setTipoEvento(cbTipoEvento.getValue());
        evento.setCliente(txtCliente.getText().trim());
        evento.setFechaEvento(dpFecha.getValue());
        evento.setLugar(txtLugar.getText().trim());
        evento.setPresupuesto(new BigDecimal(txtPresupuesto.getText().trim()));
        evento.setEstado(cbEstadoFormulario.getValue());
        return evento;
    }

    // ==================== Acciones del CRUD ====================

    @FXML
    private void guardarEvento() {
        if (!validarFormulario()) return;
        try {
            Evento evento = construirEventoDesdeFormulario();
            eventoDAO.insertar(evento);
            AlertUtil.mostrarInformacion("Exito", "Evento registrado correctamente.");
            limpiarFormulario();
            cargarTabla();
            aplicarFiltroYBusqueda();
        } catch (Exception e) {
            AlertUtil.mostrarError("Error al registrar", e.getMessage());
        }
    }

    @FXML
    private void actualizarEvento() {
        if (eventoSeleccionado == null) {
            AlertUtil.mostrarError("Actualizacion", "Seleccione un evento de la tabla para editar.");
            return;
        }
        if (!validarFormulario()) return;
        try {
            Evento evento = construirEventoDesdeFormulario();
            eventoDAO.actualizar(evento);
            AlertUtil.mostrarInformacion("Exito", "Evento actualizado correctamente.");
            limpiarFormulario();
            cargarTabla();
            aplicarFiltroYBusqueda();
        } catch (Exception e) {
            AlertUtil.mostrarError("Error al actualizar", e.getMessage());
        }
    }

    @FXML
    private void eliminarEvento() {
        if (eventoSeleccionado == null) {
            AlertUtil.mostrarError("Eliminacion", "Seleccione un evento de la tabla para eliminar.");
            return;
        }
        boolean confirmar = AlertUtil.mostrarConfirmacion(
                "Confirmar eliminacion",
                "¿Esta seguro de eliminar el evento: " + eventoSeleccionado.getNombreEvento() + "?"
        );
        if (!confirmar) return;
        try {
            eventoDAO.eliminar(eventoSeleccionado.getIdEvento());
            AlertUtil.mostrarInformacion("Exito", "Evento eliminado correctamente.");
            limpiarFormulario();
            cargarTabla();
            aplicarFiltroYBusqueda();
        } catch (Exception e) {
            AlertUtil.mostrarError("Error al eliminar", e.getMessage());
        }
    }

    @FXML
    private void refrescarTabla() {
        txtBuscar.clear();
        cbFiltroEstado.setValue("Todos");
        cargarTabla();
        limpiarFormulario();
    }

    @FXML
    private void exportarReportePDF() {
        try {
            boolean exito = ReporteUtil.generarYAbrirReportePDF();
            if (exito) {
                AlertUtil.mostrarInformacion(
                        "Reporte PDF",
                        "El reporte de eventos se ha generado y abierto correctamente."
                );
            } else {
                AlertUtil.mostrarError("Reporte PDF", "No se pudo generar el reporte PDF.");
            }
        } catch (Exception e) {
            AlertUtil.mostrarError("Error al exportar", e.getMessage());
        }
    }
}
