package controllers;

import exceptions.ProductoDuplicadoException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Alimenticio;
import models.Electronico;
import models.Producto;
import models.Proveedor;
import models.SerializableCSV;
import models.Tienda;
import utils.ServicioCSV;

public class PrincipalController implements Initializable {

    private static final String RUTA_BASE = "src/";

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnExportar;

    @FXML
    private ListView<Producto> listViewProductos;

    private Tienda tienda;

    private ServicioCSV<SerializableCSV> servicio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.tienda = new Tienda();
        this.servicio = new ServicioCSV<>();
        this.cargarProveedores();
        this.cargarDatos();
        this.refrescarLista();
    }

    private void cargarProveedores() {
        File archivo = new File(RUTA_BASE + "proveedores.csv");
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            br.readLine();
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                Proveedor prov = new Proveedor(Integer.parseInt(p[0]), p[1], p[2], p[3], p[4]);
                this.tienda.getProveedores().add(prov);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Proveedor buscarProveedor(int id) {
        for (Proveedor p : this.tienda.getProveedores()) {
            if (p.getId() == id) return p;
        }
        return new Proveedor(id, "Proveedor Desconocido", "", "", "");
    }

    @FXML
    private void agregarProducto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/formulario.fxml"));
            Parent root = loader.load();

            FormularioController controller = loader.getController();
            controller.cargarProveedores(this.tienda.getProveedores());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Agregar Producto");
            stage.setScene(new Scene(root));

            stage.showAndWait();

            Producto nuevo = controller.getProducto();

            if (nuevo != null) {
                this.tienda.agregarProducto(nuevo);
                this.guardarDatos();
                this.refrescarLista();
            }

        } catch (IOException | ProductoDuplicadoException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void modificarProducto(ActionEvent event) {
        Producto seleccionado = this.listViewProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/formulario.fxml"));
                Parent root = loader.load();

                FormularioController controller = loader.getController();
                controller.cargarProveedores(this.tienda.getProveedores());
                controller.setProductoOriginal(seleccionado);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Modificar Producto");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                Producto resultado = controller.getProducto();
                if (resultado != null) {
                    this.guardarDatos();
                    this.refrescarLista();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void eliminarProducto(ActionEvent a) {
        Producto producto = this.listViewProductos.getSelectionModel().getSelectedItem();

        if (producto != null) {
            Alert alerta = new Alert(Alert.AlertType.NONE);
            alerta.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            alerta.setTitle("Confirmar eliminación");
            alerta.setHeaderText("¿Está seguro que quiere eliminar el producto?");
            alerta.setContentText(producto.toString());

            Optional<ButtonType> resultado = alerta.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                this.tienda.eliminarProducto(producto);
                this.guardarDatos();
                this.refrescarLista();
            }
        }
    }

    public void guardarDatos() {
        List<SerializableCSV> datos = new ArrayList<>();

        for (Producto p : this.tienda.getProductos()) {
            if (p instanceof SerializableCSV s) {
                datos.add(s);
            }
        }

        try {
            this.servicio.guardar(RUTA_BASE + "productos.csv", datos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarDatos() {
        File archivo = new File(RUTA_BASE + "productos.csv");
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        List<Producto> productosCargados = new ArrayList<>();

        try {
            for (String linea : this.servicio.cargar(RUTA_BASE + "productos.csv")) {
                if (linea == null || linea.isEmpty()) continue;

                String[] partes = linea.split(";");
                String tipo = partes[0];

                Producto p = null;
                switch (tipo) {
                    case "Electronico":
                        p = new Electronico().fromCSV(linea);
                        break;
                    case "Alimenticio":
                        p = new Alimenticio().fromCSV(linea);
                        break;
                    default:
                        System.out.println("Tipo desconocido: " + tipo);
                }

                if (p != null) {
                    p.setProveedor(buscarProveedor(p.getProveedor().getId()));
                    productosCargados.add(p);
                }
            }

            this.tienda.getProductos().addAll(productosCargados);
            this.refrescarLista();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void verArchivoTexto(ActionEvent event) {
        StringBuilder contenido = new StringBuilder();
        for (Producto p : this.tienda.getProductos()) {
            if (p.getPrecio() > 500000) {
                contenido.append(p.toString()).append("\n");
            }
        }

        if (contenido.length() > 0) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_BASE + "productos_caros.txt"))) {
                bw.write(contenido.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            TextArea textArea = new TextArea(contenido.toString());
            textArea.setEditable(false);

            Stage nuevoStage = new Stage();
            nuevoStage.setTitle("Productos mayores a $500.000");
            nuevoStage.setScene(new Scene(textArea, 450, 300));
            nuevoStage.initModality(Modality.APPLICATION_MODAL);
            nuevoStage.show();
        } else {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setHeaderText(null);
            alerta.setContentText("No se encontraron productos con un precio superior a $500.000.");
            alerta.showAndWait();
        }
    }

    private void refrescarLista() {
        this.listViewProductos.getItems().clear();
        this.listViewProductos.getItems().addAll(this.tienda.getProductos());
    }
}