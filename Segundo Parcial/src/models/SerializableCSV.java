package models;

public interface SerializableCSV {
    String toCSV();
    SerializableCSV fromCSV(String linea);
}