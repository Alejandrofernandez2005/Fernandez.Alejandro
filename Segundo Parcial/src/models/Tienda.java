package models;

import exceptions.ProductoDuplicadoException;
import java.util.ArrayList;
import java.util.List;

public class Tienda {
    private List<Producto> productos;
    private List<Proveedor> proveedores;

    public Tienda() {
        this.productos = new ArrayList<>();
        this.proveedores = new ArrayList<>();
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    public void agregarProducto(Producto p) throws ProductoDuplicadoException {
        if (productos.contains(p)) {
            throw new ProductoDuplicadoException("El código de producto ya existe.");
        }
        productos.add(p);
    }

    public void eliminarProducto(Producto p) {
        productos.remove(p);
    }
}