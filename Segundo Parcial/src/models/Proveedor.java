package models;

public class Proveedor {
    private int id;
    private String razonSocial;
    private String telefono;
    private String email;
    private String ciudad;

    public Proveedor(int id, String razonSocial, String telefono, String email, String ciudad) {
        this.id = id;
        this.razonSocial = razonSocial;
        this.telefono = telefono;
        this.email = email;
        this.ciudad = ciudad;
    }

    public Proveedor(int id) {
        this.id = id;
    }

    public int getId() {
        return id; }

    public String getRazonSocial() {
        return razonSocial; }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial; }

    public String getTelefono() {
        return telefono; }

    public void setTelefono(String telefono) {
        this.telefono = telefono; }

    public String getEmail() {
        return email; }

    public void setEmail(String email) {
        this.email = email; }

    public String getCiudad() {
        return ciudad; }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proveedor proveedor = (Proveedor) o;
        return id == proveedor.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return razonSocial;
    }
}