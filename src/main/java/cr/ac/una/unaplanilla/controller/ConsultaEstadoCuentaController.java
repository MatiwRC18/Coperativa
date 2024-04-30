package cr.ac.una.unaplanilla.controller;

import cr.ac.una.unaplanilla.model.Account;
import cr.ac.una.unaplanilla.util.FlowController;
import cr.ac.una.unaplanilla.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ConsultaEstadoCuentaController extends Controller implements Initializable {

    String filePath = "cuentas.txt";

    @FXML
    private MFXButton btnBuscar;

    @FXML
    private MFXButton btnMostrarDetallado;

    @FXML
    private MFXComboBox<String> cmbCuentas;

    @FXML
    private TableView<Account> tbvResumido;

    @FXML
    private MFXTextField txfFolio;

    private ObservableList<Account> accounts;

    @Override
    public void initialize() {
        tbvResumido.getItems().clear();
        cmbCuentas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    loadAccountDetails(newValue);
                }
            }
        });

        if (tbvResumido.getColumns().isEmpty()) {
            TableColumn<Account, String> tipoCuentaCol = new TableColumn<>("Tipo de Cuenta");
            tipoCuentaCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));

            TableColumn<Account, String> balanceCol = new TableColumn<>("Balance");
            balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

            tbvResumido.getColumns().addAll(tipoCuentaCol, balanceCol);
        }

        btnMostrarDetallado.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    void OnActionBtnBuscar(ActionEvent event) {
        String folio = txfFolio.getText();
        if (folio.isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar Folio", getStage(),
                    "Debe ingresar un folio");
            return;
        }
        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Folio", getStage(),
                "Folio encontrado con exito, revise la caja de cuentas y seleccione una cuenta");
        cmbCuentas.getItems().clear(); // **No borrar linea
        List<String> accountTypes = searchFolioInFile(folio);

        if (!accountTypes.isEmpty()) {
            cmbCuentas.setItems(FXCollections.observableArrayList(accountTypes));

        } else {
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Buscar Folio", getStage(),
                    "No se encontraron cuentas asociadas al folio " + folio);
        }
    }

    @FXML
    void OnActionMostrarDetallado(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("DetalladoView");

    }

    private List<String> searchFolioInFile(String folio) {
        List<String> accountTypes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(folio)) {
                    accountTypes.add(parts[1]);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RetiroDepositoController.class.getName()).log(Level.SEVERE, "Error al leer archivo", ex);
        }
        return accountTypes;
    }

    private void loadAccountDetails(String accountType) {
        String folio = txfFolio.getText();
        if (folio.isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Folio Vacío", getStage(),
                    "Debe ingresar un folio");
            return;
        }

        Account selectedAccount = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(folio) && parts[1].equals(accountType)) {
                    String balance = parts[2];
                    selectedAccount = new Account(folio, accountType, balance);
                    break;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(RetiroDepositoController.class.getName()).log(Level.SEVERE, "Error al leer archivo", ex);
        }

        if (selectedAccount != null) {
            if (tbvResumido.getItems().isEmpty()) {
                tbvResumido.getItems().add(selectedAccount);
            } else {
                tbvResumido.getItems().set(0, selectedAccount);
            }
            btnMostrarDetallado.setDisable(false);
        } else {
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Cuenta no encontrada", getStage(),
                    "No se encontró la cuenta asociada al folio " + folio + " y tipo de cuenta " + accountType);
        }

    }

}
