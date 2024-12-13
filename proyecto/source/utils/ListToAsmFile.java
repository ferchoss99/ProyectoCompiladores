package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ListToAsmFile {

    /**
     * Escribe el contenido de una lista de Strings en un archivo con extensión .asm
     *
     * @param list     Lista de cadenas a escribir en el archivo
     * @param filePath Ruta del archivo de salida (debe terminar en .asm)
     * @throws IOException Si ocurre un error durante la escritura
     */
    public void writeListToAsmFile(List<String> list, String filePath) throws IOException {
        // Verifica que el archivo tenga la extensión correcta
        if (!filePath.endsWith(".asm")) {
            throw new IllegalArgumentException("El archivo debe tener la extensión .asm");
        }

        // Usa BufferedWriter para escribir al archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : list) {
                writer.write(line);
                writer.newLine(); // Agrega un salto de línea después de cada línea escrita
            }
        }
    }

    
}
