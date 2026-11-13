#!/bin/bash
# Launcher del Sistema de Gestion - Empresa de Eventos
cd "$(dirname "$0")"

echo "=========================================="
echo "  Sistema de Gestion - Empresa de Eventos"
echo "=========================================="
echo

# Verificar si existe el JAR, si no, empaquetar
if [ ! -f "target/tareaPOO-1.0.0.jar" ]; then
    echo "El JAR no se encontro. Empaquetando con Maven..."
    mvn package -q
    if [ $? -ne 0 ]; then
        echo "ERROR: No se pudo compilar el proyecto."
        exit 1
    fi
fi

echo "Iniciando la aplicacion..."
java -jar target/tareaPOO-1.0.0.jar

if [ $? -ne 0 ]; then
    echo
    echo "ERROR al ejecutar la aplicacion."
    exit 1
fi
