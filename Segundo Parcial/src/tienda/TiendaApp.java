package tienda;

import controllers.PrincipalController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TiendaApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/principal.fxml"));
        Parent root = loader.load();

        PrincipalController controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.setTitle("Sistema de Gestión de Productos [cite: 6]");

        stage.setOnCloseRequest(e -> controller.guardarDatos());

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}