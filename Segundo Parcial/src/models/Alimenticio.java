package models;

public class Alimenticio extends Producto implements SerializableCSV {
    private String fechaVencimiento;

    public Alimenticio(String codigo, String marca, String modelo, double precio, int stock, Proveedor proveedor, String fechaVencimiento) {
        super(codigo, marca, modelo, precio, stock, proveedor);
        this.fechaVencimiento = fechaVencimiento;
    }

    public Alimenticio() {
    }

    public String getFechaVencimiento() {
        return fechaVencimiento; }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento; }

    @Override
    public String toCSV() {
        return String.join(";", "Alimenticio", getCodigo(), getMarca(), getModelo(),
                String.valueOf(getPrecio()), String.valueOf(getStock()),
                String.valueOf(getProveedor().getId()), fechaVencimiento);
    }

    @Override
    public Alimenticio fromCSV(String linea) {
        String[] p = linea.split(";");
        return new Alimenticio(p[1], p[2], p[3], Double.parseDouble(p[4]),
                Integer.parseInt(p[5]), new Proveedor(Integer.parseInt(p[6])), p[7]);
    }
}