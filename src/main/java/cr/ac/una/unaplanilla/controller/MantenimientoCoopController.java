package cr.ac.una.unaplanilla.controller;

import cr.ac.una.unaplanilla.model.Cooperativa;
import cr.ac.una.unaplanilla.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class MantenimientoCoopController extends Controller implements Initializable {

    @FXML
    private MFXButton btnGuardar;

    @FXML
    private MFXButton btnSeleccionarLogo;

    @FXML
    private ImageView imgvLogoCoop;

    @FXML
    private MFXTextField txfNombreCoop;
    String logoPath;
    Cooperativa cooperativa = new Cooperativa();

    @FXML
    private AnchorPane root;

    @Override
    public void initialize() {

    }

    @FXML
    void onActionBtnGuardar(ActionEvent event) {

        try {
            if (txfNombreCoop.getText().isEmpty() || txfNombreCoop.getText().isBlank()) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Registrar Cooperativa", getStage(),
                        "El campo nombre de la cooperativa no puede estar vacio");
            } else if (imgvLogoCoop.getImage() == (null)) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Registrar Cooperativa", getStage(),
                        "El campo logo de la cooperativa no puede estar vacio");

            } else {

                Cooperativa cooperativa = new Cooperativa(txfNombreCoop.getText(), imgvLogoCoop.getImage().getUrl());
                cooperativa.setCooperativa(cooperativa);
                cooperativa.createFile(cooperativa);
                Controller.iconChanger(getStage(), imgvLogoCoop.getImage());
                Controller.nameChanger(getStage(), txfNombreCoop.getText());
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Registrar Cooperativa", getStage(),
                        "Cooperativa registrada con exito");
            }
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoCoopController.class.getName()).log(Level.SEVERE, "Error loading image", ex);
        }

    }

    @FXML
    void onActionBtnSeleccionarLogo(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escoga el logo de la cooperativa");
        File selectedFile = fileChooser.showOpenDialog(null);

        String relativePath = "./CoopeLogos/" + selectedFile.getName();

        try {
            String path = "./CoopeLogos";
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdir();
            }

            Path source = Paths.get(selectedFile.getPath());
            Path destiny = Paths.get(path, selectedFile.getName());
            Files.move(source, destiny);
            System.out.println("File moved successfully");

            String absolutePath = "file:" + relativePath;
            Image icon = new Image(absolutePath);
            imgvLogoCoop.setImage(icon);

        } catch (Exception ex) {
            Logger.getLogger(MantenimientoCoopController.class.getName()).log(Level.SEVERE, "Error moving image", ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
