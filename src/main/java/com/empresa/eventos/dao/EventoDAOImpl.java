package com.empresa.eventos.dao;

import com.empresa.eventos.config.ConexionDB;
import com.empresa.eventos.model.Evento;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion JDBC pura del DAO de Eventos.
 * Utiliza PreparedStatement para evitar inyeccion SQL y try-with-resources
 * para garantizar el cierre de conexiones, sentencias y result sets.
 */
public class EventoDAOImpl implements EventoDAO {

    private static final String SQL_INSERT =
            "INSERT INTO eventos (nombre_evento, tipo_evento, cliente, fecha_evento, lugar, presupuesto, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_LISTAR =
            "SELECT id_evento, nombre_evento, tipo_evento, cliente, fecha_evento, lugar, presupuesto, estado "
                    + "FROM eventos ORDER BY id_evento DESC";
    private static final String SQL_BUSCAR =
            "SELECT id_evento, nombre_evento, tipo_evento, cliente, fecha_evento, lugar, presupuesto, estado "
                    + "FROM eventos WHERE nombre_evento LIKE ? OR cliente LIKE ? "
                    + "ORDER BY id_evento DESC";
    private static final String SQL_FILTRAR_ESTADO =
            "SELECT id_evento, nombre_evento, tipo_evento, cliente, fecha_evento, lugar, presupuesto, estado "
                    + "FROM eventos WHERE estado = ? ORDER BY id_evento DESC";
    private static final String SQL_UPDATE =
            "UPDATE eventos SET nombre_evento=?, tipo_evento=?, cliente=?, fecha_evento=?, lugar=?, "
                    + "presupuesto=?, estado=? WHERE id_evento=?";
    private static final String SQL_DELETE = "DELETE FROM eventos WHERE id_evento=?";
    private static final String SQL_PRESUPUESTO_TOTAL = "SELECT SUM(presupuesto) FROM eventos";

    @Override
    public void insertar(Evento evento) {
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(
                     SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, evento.getNombreEvento());
            ps.setString(2, evento.getTipoEvento());
            ps.setString(3, evento.getCliente());
            ps.setDate(4, java.sql.Date.valueOf(evento.getFechaEvento()));
            ps.setString(5, evento.getLugar());
            ps.setBigDecimal(6, evento.getPresupuesto());
            ps.setString(7, evento.getEstado());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo insertar el evento: ninguna fila afectada.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    evento.setIdEvento(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar evento: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Evento> listar() {
        List<Evento> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearEvento(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar eventos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Evento> buscar(String filtro) {
        List<Evento> lista = new ArrayList<>();
        String like = "%" + filtro + "%";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_BUSCAR)) {

            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearEvento(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar eventos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Evento> filtrarPorEstado(String estado) {
        List<Evento> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_FILTRAR_ESTADO)) {

            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearEvento(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al filtrar por estado: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void actualizar(Evento evento) {
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, evento.getNombreEvento());
            ps.setString(2, evento.getTipoEvento());
            ps.setString(3, evento.getCliente());
            ps.setDate(4, java.sql.Date.valueOf(evento.getFechaEvento()));
            ps.setString(5, evento.getLugar());
            ps.setBigDecimal(6, evento.getPresupuesto());
            ps.setString(7, evento.getEstado());
            ps.setInt(8, evento.getIdEvento());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException(
                        "No se pudo actualizar el evento con ID " + evento.getIdEvento());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar evento: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Integer idEvento) {
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, idEvento);
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo eliminar el evento con ID " + idEvento);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar evento: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal calcularPresupuestoTotal() {
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_PRESUPUESTO_TOTAL);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal(1);
                return total != null ? total : BigDecimal.ZERO;
            }
            return BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular presupuesto total: " + e.getMessage(), e);
        }
    }

    private Evento mapearEvento(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setIdEvento(rs.getInt("id_evento"));
        evento.setNombreEvento(rs.getString("nombre_evento"));
        evento.setTipoEvento(rs.getString("tipo_evento"));
        evento.setCliente(rs.getString("cliente"));
        evento.setFechaEvento(rs.getDate("fecha_evento").toLocalDate());
        evento.setLugar(rs.getString("lugar"));
        evento.setPresupuesto(rs.getBigDecimal("presupuesto"));
        evento.setEstado(rs.getString("estado"));
        return evento;
    }
}
