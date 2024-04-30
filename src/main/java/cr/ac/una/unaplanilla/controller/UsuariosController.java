package cr.ac.una.unaplanilla.controller;

import cr.ac.una.unaplanilla.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UsuariosController extends Controller implements Initializable {

    @FXML
    private MFXButton btnAsociados;

    @FXML
    private MFXButton btnFuncionarios;

    @FXML
    private MFXButton btnProfesores;

    @FXML
    private ImageView imgFondoUsuarios;

    @FXML
    private AnchorPane root;

    @FXML
    void OnActionBtnAsociados(ActionEvent event) {
        FlowController.getInstance().goMain("AsociadosView");
        ((Stage) this.root.getScene().getWindow()).close();
    }

    @FXML
    void OnActionBtnFuncionarios(ActionEvent event) {
        FlowController.getInstance().goMain("FuncionariosView");
        ((Stage) this.root.getScene().getWindow()).close();
    }

    @FXML
    void OnActionBtnProfesores(ActionEvent event) {
        FlowController.getInstance().goMain("ProfesoresView");
        ((Stage) this.root.getScene().getWindow()).close();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imgFondoUsuarios.fitHeightProperty().bind(root.heightProperty());
        imgFondoUsuarios.fitWidthProperty().bind(root.widthProperty());
    }

}
