package cr.ac.una.unaplanilla.controller;

import cr.ac.una.unaplanilla.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FuncionariosController extends Controller implements Initializable {

    @FXML
    private MFXButton btnAperturaCuentas;

    @FXML
    private MFXButton btnImpresionCarnet;

    @FXML
    private MFXButton btnMantenimientoAso;

    @FXML
    private MFXButton btnRetiroDepisito;

    @FXML
    private MFXButton btnSalir;

    @FXML
    private ImageView imgLogo;

    @FXML
    private Label lbTextoEncabezado;

    @FXML
    private BorderPane root;

    @FXML
    void OnActionBtnAperturaCuentas(ActionEvent event) {
        FlowController.getInstance().goView("AperturaCuentasView");
    }

    @FXML
    void OnActionBtnImpresionCarnet(ActionEvent event) {
        FlowController.getInstance().goView("ImpresionCarnetView");
    }

    @FXML
    void OnActionBtnManteniminetoAso(ActionEvent event) {
        FlowController.getInstance().goView("MantenimientoAsoView");
    }

    @FXML
    void OnActionBtnRetiroDeposito(ActionEvent event) {
        FlowController.getInstance().goView("RetiroDepositoView");
    }

    @FXML
    void OnActionBtnSalir(ActionEvent event) {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }

    private void cargarCoopeInfo() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Cooperativa.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.replaceAll("file:", "").split(",");
                String name = parts[0];
                String logo = parts[1];
                lbTextoEncabezado.setText(name); // Establece el texto del encabezado
                File file = new File(logo);
                if (file.exists()) {
                    System.out.println("File found: " + logo);
                    Image image = new Image(file.toURI().toString());
                    if (image != null) {
                        System.out.println("Image created successfully");
                        imgLogo.setImage(image); // Establece la imagen del logo
                    } else {
                        System.out.println("Failed to create image object");
                    }
                } else {
                    System.out.println("File not found: " + logo);
                }
            }
            br.close();
        } catch (Exception e) {
            Logger.getLogger(FuncionariosController.class.getName()).log(Level.SEVERE, "Error al buscar cooperativa", e);
        }
    }

    @Override
    public void initialize() {
        cargarCoopeInfo();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cargarCoopeInfo();
    }

}
