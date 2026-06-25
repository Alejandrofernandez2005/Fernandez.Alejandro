package models;

public class Electronico extends Producto implements SerializableCSV {
    private int garantia;

    public Electronico(String codigo, String marca, String modelo, double precio, int stock, Proveedor proveedor, int garantia) {
        super(codigo, marca, modelo, precio, stock, proveedor);
        this.garantia = garantia;
    }

    public Electronico() {
    }

    public int getGarantia() {
        return garantia; }

    public void setGarantia(int garantia) {
        this.garantia = garantia; }

    @Override
    public String toCSV() {
        return String.join(";", "Electronico", getCodigo(), getMarca(), getModelo(),
                String.valueOf(getPrecio()), String.valueOf(getStock()),
                String.valueOf(getProveedor().getId()), String.valueOf(garantia));
    }

    @Override
    public Electronico fromCSV(String linea) {
        String[] p = linea.split(";");
        return new Electronico(p[1], p[2], p[3], Double.parseDouble(p[4]),
                Integer.parseInt(p[5]), new Proveedor(Integer.parseInt(p[6])), Integer.parseInt(p[7]));
    }
}