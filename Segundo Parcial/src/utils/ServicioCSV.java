package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.SerializableCSV;

public class ServicioCSV<T extends SerializableCSV> {

    public void guardar(String archivo, List<T> objetos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (T obj : objetos) {
                bw.write(obj.toCSV());
                bw.newLine();
            }
        }
    }

    public List<String> cargar(String archivo) throws IOException {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return lineas;
    }
}