@echo off
cd /d "%~dp0"

echo ==========================================
echo  Sistema de Gestion - Empresa de Eventos
echo ==========================================
echo.

REM Verificar si existe el JAR, si no, empaquetar
if not exist "target\tareaPOO-1.0.0.jar" (
    echo El JAR no se encontro. Empaquetando con Maven...
    mvn package -q
    if errorlevel 1 (
        echo ERROR: No se pudo compilar el proyecto.
        pause
        exit /b 1
    )
)

echo Iniciando la aplicacion...
java -jar target\tareaPOO-1.0.0.jar

if errorlevel 1 (
    echo.
    echo ERROR al ejecutar la aplicacion.
    pause
    exit /b 1
)
