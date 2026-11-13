-- ============================================================================
-- Script de creacion de base de datos y tabla para el Sistema de Gestion
-- de Eventos (Empresa de Eventos)
-- Estudiante: Jake Patrick SANCHEZ SANTOS
-- ============================================================================

CREATE DATABASE IF NOT EXISTS db_empresa_eventos;
USE db_empresa_eventos;

DROP TABLE IF EXISTS eventos;

CREATE TABLE eventos (
    id_evento INT PRIMARY KEY AUTO_INCREMENT,
    nombre_evento VARCHAR(120) NOT NULL,
    tipo_evento VARCHAR(60) NOT NULL,
    cliente VARCHAR(120) NOT NULL,
    fecha_evento DATE NOT NULL,
    lugar VARCHAR(150) NOT NULL,
    presupuesto DECIMAL(10, 2) NOT NULL,
    estado VARCHAR(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Registros de prueba realistas para demostrar el sistema
INSERT INTO eventos (nombre_evento, tipo_evento, cliente, fecha_evento, lugar, presupuesto, estado) VALUES
('Boda de Ana y Carlos', 'Boda', 'Ana Perez y Carlos Ruiz', '2026-12-15', 'Salon Real, Lima', 12500.00, 'Planificado'),
('Conferencia Tecnologica 2026', 'Conferencia', 'Innovatec SAC', '2026-11-20', 'Centro de Convenciones Lima', 8500.00, 'Confirmado'),
('Lanzamiento de Producto X', 'Corporativo', 'TechSolutions Peru', '2026-12-05', 'Oficinas Corporativas San Isidro', 15000.00, 'Planificado'),
('Cumpleaños 15 de Maria', 'Cumpleaños', 'Familia Gonzales', '2026-11-30', 'Quinta Los Almendros', 6200.00, 'Planificado'),
('Seminario de Liderazgo', 'Conferencia', 'Lidera Group', '2026-12-10', 'Auditorio Universidad', 4800.00, 'En ejecucion'),
('Concierto de Rock Andino', 'Concierto', 'Andes Producciones', '2026-12-20', 'Estadio Nacional', 25000.00, 'Planificado'),
('Graduacion Universidad', 'Graduacion', 'Universidad Vallegrande', '2026-11-25', 'Campus Principal', 9500.00, 'Confirmado'),
('Evento Corporativo Anual', 'Corporativo', 'Global Corp SAC', '2026-12-18', 'Hotel Swissotel Lima', 18000.00, 'Planificado'),
('Boda de Luis y Sofia', 'Boda', 'Luis Mendoza y Sofia Torres', '2027-01-10', 'Hacienda Los Fresnos', 14000.00, 'Planificado'),
('Feria de Emprendedores', 'Conferencia', 'Cierra la Brecha', '2027-02-05', 'Parque de la Exposicion', 7200.00, 'Planificado');
