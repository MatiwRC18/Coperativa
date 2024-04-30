package cr.ac.una.unaplanilla.controller;

import cr.ac.una.unaplanilla.controller.CameraController;
import cr.ac.una.unaplanilla.controller.Controller;
import cr.ac.una.unaplanilla.model.Associated;
import cr.ac.una.unaplanilla.util.FlowController;
import cr.ac.una.unaplanilla.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.logging.Level;

public class RegistroAsociadoController extends Controller implements Initializable {

    @FXML
    private MFXButton btnTomarFotoCamara;

    @FXML
    private ImageView imgVFotoAsociado;

    @FXML
    private MFXTextField txfEdad;

    @FXML
    private MFXTextField txfNombre;

    @FXML
    private MFXButton btnRegistrar;

    @FXML
    private MFXTextField txfApellido;

    Associated asociado = new Associated();
    private CameraController camera;

    @Override
    public void initialize() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void OnActionShowImage(MouseEvent event) {
        LoadPhoto();
    }

    public void LoadPhoto() {
        String photoPath = "Photos/foto1.jpg";
        File file_photo = new File(photoPath);

        if (file_photo.exists()) {
            Image image = new Image(file_photo.toURI().toString());
            imgVFotoAsociado.setImage(image);
        } else {

        }
    }

    public void RenamePhoto(Associated asociado) {
        File file = new File("Photos/foto1.jpg");

        if (file.exists()) {
            String filepath = file.getParent();
            String newFileName = asociado.getFolio() + ".jpg";
            File newFile = new File(filepath, newFileName);

            if (file.renameTo(newFile)) {
                asociado.setPhoto(String.valueOf(newFile));
                LoadPhoto();
            } else {
                System.out.println("Failed to rename");
            }
        } else {
            System.out.println("In-existing photo");
        }
    }

    @FXML
    void onActionBtnTomarFotoCamara(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("CameraView");

    }

    @FXML
    void onActionBtnRegistrar(ActionEvent event) {

        try {
            if (txfNombre.getText() == null || txfNombre.getText().isEmpty()) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Registrar Asociado", getStage(),
                        "Debe ingresar el nombre del asociado");
            } else if (txfEdad.getText() == null || txfEdad.getText().isEmpty()) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Registrar Asociado", getStage(),
                        "Debe ingresar la edad del asociado");
            } else if (txfApellido.getText() == null || txfApellido.getText().isEmpty()) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Registrar Asociado", getStage(),
                        "Debe ingresar el apellido del asociado");
            } else if (imgVFotoAsociado.getImage() == null || imgVFotoAsociado.getImage().isError()) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Registrar Asociado", getStage(),
                        "Debe tomarse la foto del asociado");
            } else {
                Associated asociado = new Associated(txfNombre.getText(), txfApellido.getText(), txfEdad.getText(),
                        "Photo");
                asociado.createFolio();
                asociado.setAssociated(asociado);
                RenamePhoto(asociado);
                asociado.createFile(asociado);
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Registrar Asociado", getStage(),
                        "Asociado registrado con éxito, su numero de folio es: " + asociado.getFolio());
                txfNombre.clear();
                txfEdad.clear();
                txfApellido.clear();
                imgVFotoAsociado.setImage(null);

            }
        } catch (Exception ex) {
            Logger.getLogger(RegistroAsociadoController.class.getName()).log(Level.SEVERE,
                    "Error al Registrar Asociado", ex);
        }
    }

}
