import os
import sys
from pathlib import Path

try:
    from pypdf import PdfReader
except ImportError:
    print("pypdf no esta instalado. Instalalo con: pip install pypdf")
    sys.exit(1)

carpeta = Path(__file__).resolve().parent
salida = carpeta / "lectura_pdf.txt"

pdfs = sorted(carpeta.glob("*.pdf"))

with open(salida, "w", encoding="utf-8") as out:
    if not pdfs:
        out.write("No se encontraron archivos .pdf en esta carpeta.\n")
    for pdf in pdfs:
        out.write(f"\n===== {pdf.name} =====\n")
        try:
            reader = PdfReader(str(pdf))
            for i, pagina in enumerate(reader.pages, start=1):
                texto = pagina.extract_text() or ""
                out.write(f"--- Pagina {i} ---\n")
                out.write(texto)
                out.write("\n")
        except Exception as e:
            out.write(f"[Error al leer {pdf.name}: {e}]\n")

print(f"Proceso terminado. Archivos PDF encontrados: {len(pdfs)}")
print(f"Texto guardado en: {salida}")
