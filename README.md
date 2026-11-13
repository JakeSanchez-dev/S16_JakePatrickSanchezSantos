# Sistema de Gestión - Empresa de Eventos

Sistema de escritorio CRUD completo desarrollado en **JavaFX**, **MySQL (JDBC puro)**, **Maven** y **JasperReports**, como parte de la tarea de Programación Orientada a Objetos.

**Estudiante:** Jake Patrick SANCHEZ SANTOS  
**Sistema asignado:** Empresa de eventos  
**Base de datos:** `db_empresa_eventos` / Tabla `eventos`

---

## Estructura del proyecto

```
tareaPOO/
├── pom.xml
├── README.md
├── ejecutar.bat                 # Launcher para Windows (doble clic)
├── ejecutar.sh                  # Launcher para Linux/Mac
├── database/
│   └── schema.sql               # Script SQL: base de datos, tabla y datos de prueba
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── empresa/
        │           └── eventos/
        │               ├── config/
        │               │   └── ConexionDB.java          # Conexión JDBC a MySQL
        │               ├── model/
        │               │   └── Evento.java              # Modelo de dominio (POJO)
        │               ├── dao/
        │               │   ├── EventoDAO.java           # Interfaz DAO
        │               │   └── EventoDAOImpl.java       # Implementación JDBC pura
        │               ├── controller/
        │               │   └── EventoController.java    # Controlador JavaFX (CRUD)
        │               ├── util/
        │               │   ├── AlertUtil.java           # Alertas visuales
        │               │   └── NavigationUtil.java      # Apertura de archivos
        │               ├── reportes/
        │               │   └── ReporteUtil.java         # Generación de reporte PDF (JasperReports)
        │               └── Launcher.java                # Punto de entrada (no extiende Application)
        └── resources/
            ├── fxml/
            │   └── evento_view.fxml                     # Vista principal (JavaFX FXML)
            ├── reportes/
            │   └── reporte_eventos.jrxml               # Plantilla JasperReports
            └── styles.css                               # Hojas de estilo CSS modernas
```

---

## Requisitos previos

1. **JDK 17 o superior** (se probó con JDK 21).
2. **Apache Maven 3.6 o superior**.
3. **My Server MySQL 8.x** en ejecución en `localhost:3306`.
4. Conexión a Internet la primera vez (para descargar dependencias de Maven).

---

## 1. Ejecutar el script SQL en MySQL

Abra una terminal de MySQL (o use MySQL Workbench / phpMyAdmin) y ejecute:

```bash
mysql -u root -p < database/schema.sql
```

Esto creará:
- La base de datos `db_empresa_eventos`.
- La tabla `eventos` con los 8 campos exactos (`id_evento`, `nombre_evento`, `tipo_evento`, `cliente`, `fecha_evento`, `lugar`, `presupuesto`, `estado`).
- 10 registros de prueba realistas (bodas, conferencias, corporativos, conciertos, graduaciones, cumpleaños).

> **Nota:** Si usa otro usuario o contraseña de MySQL, ajuste el comando o edite `ConexionDB.java`.

---

## 2. Configurar credenciales de MySQL

Edite el archivo:

```
src/main/java/com/empresa/eventos/config/ConexionDB.java
```

Y modifique las constantes si su entorno lo requiere:

```java
private static final String URL = "jdbc:mysql://localhost:3306/db_empresa_eventos?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Lima";
private static final String USUARIO = "root";
private static final String CONTRASENA = ""; // su contraseña de MySQL
```

---

## 3. Compilar y ejecutar con Maven

Desde la raíz del proyecto (donde está el `pom.xml`):

### Compilar
```bash
mvn clean compile
```

### Ejecutar directamente
```bash
mvn javafx:run
```

> Si el plugin `javafx:run` no está configurado, puede ejecutar la clase principal directamente con el classpath generado, o empaquetar el JAR (ver siguiente sección).

---

## 4. Generar el JAR ejecutable

El proyecto usa `maven-shade-plugin` para generar un JAR ejecutable que incluye todas las dependencias (JavaFX, MySQL Connector/J, JasperReports).

### Empaquetar
```bash
mvn clean package
```

Esto genera el archivo:

```
target/tareaPOO-1.0.0.jar
```

### Ejecutar el JAR
```bash
java -jar target/tareaPOO-1.0.0.jar
```

El `Launcher.java` (clase principal configurada en el `pom.xml`) lanza la aplicación JavaFX sin requerir que el JAR extienda `Application`, evitando problemas de módulos de JavaFX.

---

## 5. Lanzamiento rápido (launcher)

En la raíz del proyecto encontrarás dos scripts para ejecutar la aplicación sin escribir comandos de Maven:

- **Windows:** haz doble clic en `ejecutar.bat` o ejecútalo desde la terminal.
- **Linux/Mac:** `./ejecutar.sh` (da permisos de ejecución la primera vez: `chmod +x ejecutar.sh`).

Estos scripts verifican si el JAR ya está empaquetado; si no, lo compilan automáticamente con `mvn package` y luego lanzan la aplicación.

### Ícono de la aplicación

La ventana muestra un ícono personalizado con **dos círculos azules** (archivo `src/main/resources/icono.png`), en lugar del ícono predeterminado de Java.

---

## 6. Funcionalidades del sistema

| Módulo | Descripción |
|--------|-------------|
| **Registrar** | Formulario con validaciones (campos obligatorios, presupuesto > 0, tipo/estado por ComboBox, fecha con DatePicker). |
| **Consultar** | `TableView` con todas las columnas, sincronizado en tiempo real con MySQL, buscador dinámico por nombre/cliente, filtro por estado y botón refrescar. |
| **Actualizar** | Al seleccionar una fila, sus datos se cargan en el formulario para editarlos y guardarlos en MySQL. |
| **Eliminar** | Botón eliminar con cuadro de confirmación (`AlertType.CONFIRMATION`) previo. |
| **Limpiar** | Borra todos los controles del formulario y deselects la tabla. |
| **Exportar Reporte PDF** | Genera un reporte con JasperReports (listado de eventos + presupuesto total) y lo abre automáticamente. |

---

## 6. Arquitectura y patrones

- **Arquitectura limpia** por paquetes: `config`, `model`, `dao`, `controller`, `util`, `reportes`.
- **Patrón DAO** con `PreparedStatement` y `try-with-resources` para evitar inyección SQL y fugas de recursos.
- **Encapsulamiento** en el modelo `Evento` (atributos privados, getters/setters).
- **Manejo de excepciones** con capas clara y alertas visuales al usuario.
- **JavaFX + FXML + CSS** para una interfaz moderna y separada de la lógica.

---

## 7. Solución de problemas comunes

- **`ClassNotFoundException: com.mysql.cj.jdbc.Driver`**: asegúrese de haber ejecutado `mvn clean compile` para descargar `mysql-connector-j`.
- **Error de conexión a MySQL**: verifique que el servidor MySQL esté encendido, que exista la base de datos `db_empresa_eventos` y que las credenciales en `ConexionDB.java` sean correctas.
- **El JAR no inicia**: ejecute `mvn clean package` y verifique que `target/tareaPOO-1.0.0.jar` se generó; luego ejecútelo con `java -jar`.
- **Reporte PDF no se abre**: confirme que JasperReports compiló el `.jrxml` correctamente y que existe una conexión válida a MySQL.

---

## 8. Autenticación / notas del entregable

Este proyecto está listo para ejecutarse y sustentarse. Toda la lógica de acceso a datos está isolada en `EventoDAOImpl`, la interfaz gráfica en `EventoController` + `evento_view.fxml`, y el estilo visual en `styles.css`.
