/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.unaplanilla.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class archivo {

     public static void crearArchivo(String nombreArchivo) throws IOException {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            archivo.createNewFile();
        }
    }

    public static void escribirArchivo(String nombreArchivo, String contenido) throws IOException {
        FileWriter writer = new FileWriter(nombreArchivo);
        writer.write(contenido);
        writer.close();
    }

    public static String leerArchivo(String nombreArchivo) throws IOException {
        StringBuilder contenido = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo));
        String linea;
        while ((linea = reader.readLine()) != null) {
            contenido.append(linea);
            contenido.append("\n");
        }
        reader.close();
        return contenido.toString();
    }
}
