package models;

public abstract class Producto {
    private final String codigo;
    private String marca;
    private String modelo;
    private double precio;
    private int stock;
    private Proveedor proveedor;

    public Producto(String codigo, String marca, String modelo, double precio, int stock, Proveedor proveedor) {
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = stock;
        this.proveedor = proveedor;
    }

    public Producto() {
        this.codigo = "";
    }

    public String getCodigo() {
        return codigo; }

    public String getMarca() {
        return marca; }

    public void setMarca(String marca) {
        this.marca = marca; }

    public String getModelo() {
        return modelo; }

    public void setModelo(String modelo) {
        this.modelo = modelo; }

    public double getPrecio() {
        return precio; }

    public void setPrecio(double precio) {
        this.precio = precio; }

    public int getStock() {
        return stock; }

    public void setStock(int stock) {
        this.stock = stock; }

    public Proveedor getProveedor() {
        return proveedor; }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return codigo.equalsIgnoreCase(producto.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return codigo + " - " + marca + " " + modelo + " ($" + precio + ")";
    }
}