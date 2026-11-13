package com.empresa.eventos.dao;

import com.empresa.eventos.model.Evento;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interfaz del Data Access Object para la entidad Evento.
 * Define el contrato de operaciones CRUD y consultas.
 */
public interface EventoDAO {

    /**
     * Inserta un nuevo evento en la base de datos.
     *
     * @param evento evento a insertar (se le asigna el id generado)
     */
    void insertar(Evento evento);

    /**
     * Lista todos los eventos ordenados por id descendente.
     *
     * @return lista de eventos
     */
    List<Evento> listar();

    /**
     * Busca eventos por nombre o cliente que contengan el filtro.
     *
     * @param filtro texto a buscar
     * @return lista de eventos coincidentes
     */
    List<Evento> buscar(String filtro);

    /**
     * Filtra eventos por estado exacto.
     *
     * @param estado estado a filtrar
     * @return lista de eventos con ese estado
     */
    List<Evento> filtrarPorEstado(String estado);

    /**
     * Actualiza un evento existente segun su id.
     *
     * @param evento evento con los datos actualizados
     */
    void actualizar(Evento evento);

    /**
     * Elimina un evento por su id.
     *
     * @param idEvento id del evento a eliminar
     */
    void eliminar(Integer idEvento);

    /**
     * Calcula la suma total de presupuestos de todos los eventos.
     *
     * @return presupuesto total (BigDecimal.ZERO si no hay registros)
     */
    BigDecimal calcularPresupuestoTotal();
}
