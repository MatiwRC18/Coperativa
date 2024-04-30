package cr.ac.una.unaplanilla.controller;

import cr.ac.una.unaplanilla.model.Associated;
import cr.ac.una.unaplanilla.util.AppContext;
import cr.ac.una.unaplanilla.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MantenimientoAsoController extends Controller implements Initializable {

    @FXML
    public MFXButton btnEliminar;

    @FXML
    public MFXButton btnCambiarFoto;

    @FXML
    public MFXButton btnBarrer;

    @FXML
    private MFXButton btnBuscar;

    @FXML
    private MFXButton btnModificar;

    @FXML
    private MFXTextField txfEdad;

    @FXML
    private MFXTextField txfApellido;

    @FXML
    private MFXTextField txfFolio;

    @FXML
    private MFXTextField txfNombre;

    @FXML
    private ImageView imgFoto;

    private ObservableList<Associated> asociate;

    @Override
    public void initialize() {
        asociate = ((ObservableList<Associated>) AppContext.getInstance().get("Asociados"));
        readAsociado();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    void OnActionBtnBuscar(ActionEvent event) {
        if (txfFolio.getText().isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar Folio", getStage(),
                    "Debe ingresar un folio");
        }
        try {
            String folio = txfFolio.getText();
            for (Associated associate : asociate) {
                if (associate.getFolio().equals(folio)) {
                    txfNombre.setText(associate.getName().toString());
                    txfEdad.setText(associate.getAge().toString());
                    txfApellido.setText(associate.getLastName().toString());
                    String photo = associate.getFolio() + ".jpg";
                    String path = "./Photos/" + photo;
                    File file = new File(path);
                    if (file.exists()) {
                        Image image = new Image(file.toURI().toString());
                        imgFoto.setImage(image);
                    }
                }
            }
        } catch (Exception e) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar Asociado", getStage(),
                    "Error al buscar asociado");
        }
    }

    @FXML
    void OnActionBtnModificar(ActionEvent event) {
        if (txfFolio.getText().isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Actualizar Asociado", getStage(),
                    "Debe ingresar un folio");
            return;
        }

        String folio = txfFolio.getText();
        int index = -1;

        for (int i = 0; i < asociate.size(); i++) {
            Associated associate = asociate.get(i);
            if (associate.getFolio().equals(folio)) {
                index = i;

                break;
            }
        }

        if (index != -1) {
            asociate.get(index).setName(txfNombre.getText());
            asociate.get(index).setAge(txfEdad.getText());
            asociate.get(index).setLastName(txfApellido.getText());

            try {
                File file = new File("Asociados.txt");
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();
                String line;

                int lineCount = 0;
                while ((line = reader.readLine()) != null) {
                    if (lineCount == index) {
                        sb.append(asociate.get(index).getName() + ",")
                                .append(asociate.get(index).getLastName() + ",")
                                .append(asociate.get(index).getFolio() + ",")
                                .append(asociate.get(index).getAge() + ",")
                                .append(asociate.get(index).getPhoto() + "\n");
                    } else {
                        sb.append(line).append("\n");
                    }
                    lineCount++;
                }

                reader.close();

                String content = sb.toString();

                FileWriter writer = new FileWriter(file);
                writer.write(content);
                writer.close();

                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Actualizar Asociado", getStage(),
                        "Asociado actualizado exitosamente");
            } catch (IOException ex) {
                Logger.getLogger(MantenimientoAsoController.class.getName()).log(Level.SEVERE,
                        "Error al escribir archivo", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Actualizar Asociado", getStage(),
                        "Error al actualizar asociado en el archivo");
            }
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Actualizar Asociado", getStage(),
                    "Asociado no encontrado!");
        }
    }

    @FXML
    public void OnActionBtnBarrer(ActionEvent actionEvent) {
        txfFolio.clear();
        txfEdad.clear();
        txfNombre.clear();
        txfApellido.clear();
        imgFoto.setImage(null);
    }

    @FXML
    public void OnActionBtnEliminar(ActionEvent actionEvent) {
        if (txfFolio.getText().isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Asociado", getStage(),
                    "Debe ingresar un folio");
            return;
        }

        String folio = txfFolio.getText();
        boolean AsoRemovido = false;

        for (int i = 0; i < asociate.size(); i++) {
            Associated associate = asociate.get(i);
            if (associate.getFolio().equals(folio)) {
                asociate.remove(i);
                AsoRemovido = true;
                break;
            }
        }

        if (AsoRemovido) {
            try {
                File file = new File("Asociados.txt");
                file.createNewFile();

                StringBuilder sb = new StringBuilder();
                for (Associated associate : asociate) {
                    sb.append(associate.getName() + ",")
                            .append(associate.getLastName() + ",")
                            .append(associate.getFolio() + ",")
                            .append(associate.getAge() + ",")
                            .append(associate.getPhoto() + "\n");
                }
                String content = sb.toString();
                java.nio.file.Files.write(file.toPath(), content.getBytes());

                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Eliminar Asociado", getStage(),
                        "Asociado eliminado exitosamente");
                txfNombre.clear();
                txfEdad.clear();
                txfApellido.clear();
                imgFoto.setImage(null);
                txfFolio.clear();
            } catch (IOException ex) {
                Logger.getLogger(MantenimientoAsoController.class.getName()).log(Level.SEVERE,
                        "Error al escribir archivo", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Asociado", getStage(),
                        "Error al eliminar asociado del archivo");
            }
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Asociado", getStage(),
                    "Asociado no encontrado!");
        }
    }

    @FXML
    public void OnActionBtnCambiarFoto(ActionEvent actionEvent) {
        if (txfFolio.getText().isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar Folio", getStage(),
                    "Debe ingresar un folio");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);

                String folio = txfFolio.getText();
                String newImageFilename = folio + ".jpg";
                String newImagePath = "./Photos/" + newImageFilename;
                Files.copy(selectedFile.toPath(), Path.of(newImagePath), StandardCopyOption.REPLACE_EXISTING);

                Image currentImage = imgFoto.getImage();

                imgFoto.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                new Mensaje().showModal(Alert.AlertType.ERROR, "Error", getStage(),
                        "Error al abrir la imagen");
            }
        }
    }

    public void readAsociado() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Asociados.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String lastName = parts[1];
                String folio = parts[2];
                String age = parts[3];
                String photo = parts[4];
                Associated asociado = new Associated(name, lastName, folio, age, photo);
                asociate.add(asociado);
            }
            br.close();

        } catch (IOException ex) {
            Logger.getLogger(MantenimientoAsoController.class.getName()).log(Level.SEVERE,
                    "Error al leer archivo", ex);
        }
    }

}
