package cr.ac.una.unaplanilla.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import cr.ac.una.unaplanilla.util.Mensaje;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;

public class CameraController extends Controller implements Initializable {

    @FXML
    public MFXButton btnTomarFoto1;

    @FXML
    public MFXButton btnTomarOtraVez;

    @FXML
    public ImageView FotoTomada;

    @FXML
    public MFXButton btnGuardar1;

    @FXML
    public MFXButton btnSalirCamara;

    @FXML
    private ImageView PrevistaFoto;

    private Webcam webcam;
    private ScheduledExecutorService executor;
    private static final Logger logger = LoggerFactory.getLogger(CameraController.class);

    public void initialize() {
        btnTomarOtraVez.setDisable(true);
        btnGuardar1.setDisable(true);
        try {
            webcam = Webcam.getDefault();
            webcam.setViewSize(WebcamResolution.VGA.getSize());
            webcam.open();
            startCameraPreview();
        } catch (Exception ex) {
            logger.error("Camera not found ", ex);
            btnTomarFoto1.setDisable(true);
            btnGuardar1.setDisable(true);
            btnTomarOtraVez.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void onActionTomarFoto1() {
        try {
            String fotos = "./Photos";
            File folder = new File(fotos);
            if (!folder.exists()) {
                folder.mkdir();
            }
            String fileName = "foto1.jpg";
            File file = new File(folder, fileName);

            ImageIO.write(webcam.getImage(), "JPG", file);
            logger.info("Image saved successfully: {}", file);

            displayCapturedImage(file);
        } catch (IOException e) {
            logger.error("Error saving image: ", e);
        }

        btnTomarOtraVez.setDisable(false);
        btnGuardar1.setDisable(false);
        btnTomarFoto1.setDisable(true);
        btnSalirCamara.setDisable(true);
    }

    @FXML
    public void onActionTomarOtraVez(ActionEvent event) {
        File file = new File(String.format("./Photos/foto1" + ".jpg", System.currentTimeMillis()));
        file.delete();
        FotoTomada.setImage(null);
        btnTomarOtraVez.setDisable(true);
        btnGuardar1.setDisable(true);
        btnTomarFoto1.setDisable(false);
        btnSalirCamara.setDisable(false);
    }

    @FXML
    public void onActionGuardar1(ActionEvent event) {
        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Vista Camara", getStage(),
                "Foto guardada exitosamente");
        btnTomarOtraVez.setDisable(true);
        btnTomarFoto1.setDisable(false);
        btnGuardar1.setDisable(true);

        stopCameraPreview();
        ((Stage) btnSalirCamara.getScene().getWindow()).close();
    }

    @FXML
    public void onActionSalirCamara(ActionEvent event) {
        btnTomarFoto1.setDisable(false);
        btnGuardar1.setDisable(false);
        stopCameraPreview();
        ((Stage) btnSalirCamara.getScene().getWindow()).close();
    }

    private void startCameraPreview() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            Image image = SwingFXUtils.toFXImage(webcam.getImage(), null);
            PrevistaFoto.setImage(image);
        }, 1, 33, TimeUnit.MILLISECONDS);
    }

    void stopCameraPreview() {
        executor.shutdown();
        webcam.close();
    }

    private void displayCapturedImage(File file) {
        try {
            Image image = new Image(file.toURI().toString());
            FotoTomada.setImage(image);
        } catch (Exception e) {
            logger.error("Error displaying captured image: ", e);
        }
    }

}
