package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Alimenticio;
import models.Electronico;
import models.Producto;
import models.Proveedor;
import java.util.List;

public class FormularioController {

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtMarca;

    @FXML
    private TextField txtModelo;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtStock;

    @FXML
    private TextField txtDatoExtra;

    @FXML
    private ChoiceBox<String> cbTipo;

    @FXML
    private ChoiceBox<Proveedor> cbProveedor;

    @FXML
    private Button btnAceptar;

    @FXML
    private Label lblDatoExtra;

    @FXML
    private Button btnCancelar;

    private Producto producto;

    @FXML
    public void initialize() {
        this.cbTipo.getItems().addAll("Electronico", "Alimenticio");
        this.cbTipo.setValue("Electronico");
        this.btnAceptar.setOnAction(e -> aceptar());
        this.btnCancelar.setOnAction(e -> cancelar());
    }

    @FXML
    public void onTipoCambiado() {
        actualizarEtiquetaDatoExtra(this.cbTipo.getValue());
    }

    private void actualizarEtiquetaDatoExtra(String tipo) {
        switch (tipo) {
            case "Electronico" -> this.lblDatoExtra.setText("Garantía (meses):");
            case "Alimenticio" -> this.lblDatoExtra.setText("Vencimiento (AAAA-MM-DD):");
            default -> this.lblDatoExtra.setText("Dato extra:");
        }
    }

    public void cargarProveedores(List<Proveedor> proveedores) {
        this.cbProveedor.getItems().addAll(proveedores);
        if (!proveedores.isEmpty()) {
            this.cbProveedor.setValue(proveedores.get(0));
        }
    }

    public void setProductoOriginal(Producto p) {
        this.producto = p;
        this.txtCodigo.setDisable(true);

        if (p != null) {
            this.txtCodigo.setText(p.getCodigo());
            this.txtMarca.setText(p.getMarca());
            this.txtModelo.setText(p.getModelo());
            this.txtPrecio.setText(String.valueOf(p.getPrecio()));
            this.txtStock.setText(String.valueOf(p.getStock()));
            this.cbProveedor.setValue(p.getProveedor());

            if (p instanceof Electronico elec) {
                this.cbTipo.setValue("Electronico");
                this.txtDatoExtra.setText(String.valueOf(elec.getGarantia()));
            } else if (p instanceof Alimenticio alim) {
                this.cbTipo.setValue("Alimenticio");
                this.txtDatoExtra.setText(alim.getFechaVencimiento());
            }
        }
    }

    @FXML
    private void aceptar() {
        String codigo = this.txtCodigo.getText();
        String marca = this.txtMarca.getText();
        String modelo = this.txtModelo.getText();
        double precio = Double.parseDouble(this.txtPrecio.getText());
        int stock = Integer.parseInt(this.txtStock.getText());
        Proveedor prov = this.cbProveedor.getValue();
        String tipo = cbTipo.getValue();

        if (producto != null) {
            producto.setMarca(marca);
            producto.setModelo(modelo);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setProveedor(prov);

            switch (tipo) {
                case "Electronico" -> ((Electronico) producto).setGarantia(Integer.parseInt(this.txtDatoExtra.getText()));
                case "Alimenticio" -> ((Alimenticio) producto).setFechaVencimiento(this.txtDatoExtra.getText());
            }
        } else {
            switch (tipo) {
                case "Electronico" -> this.producto = new Electronico(codigo, marca, modelo, precio, stock, prov, Integer.parseInt(this.txtDatoExtra.getText()));
                case "Alimenticio" -> this.producto = new Alimenticio(codigo, marca, modelo, precio, stock, prov, this.txtDatoExtra.getText());
            }
        }
        cerrar();
    }

    @FXML
    private void cancelar() {
        this.producto = null;
        cerrar();
    }

    private void cerrar() {
        Stage stage = (Stage) this.btnCancelar.getScene().getWindow();
        stage.close();
    }

    public Producto getProducto() {
        return this.producto;
    }
}