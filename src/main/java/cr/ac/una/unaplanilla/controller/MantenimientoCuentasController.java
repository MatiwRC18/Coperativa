package cr.ac.una.unaplanilla.controller;

import cr.ac.una.unaplanilla.model.AccountType;
import cr.ac.una.unaplanilla.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

public class MantenimientoCuentasController extends Controller implements Initializable {

    String filePath = "./AccountType.txt";

    @FXML
    private AnchorPane root;

    @FXML
    private MFXButton btnAgregar;

    @FXML
    private MFXButton btnEliminar;

    @FXML
    private MFXButton btnEditar;

    @FXML
    private MFXComboBox<String> cmbCuentas;

    @FXML
    private MFXTextField txfNombreCuentas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initialize() {

        LoadTXTFile();

    }

    @FXML
    void OnActionBtnAgregar(ActionEvent event) {
        try {
            if (txfNombreCuentas.getText() == null || txfNombreCuentas.getText().isEmpty()) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Agregar Cuenta", getStage(),
                        "Ingrese un nombre para la cuenta!");
            } else {
                String newTypeName = txfNombreCuentas.getText();
                cmbCuentas.getItems().add(newTypeName);

                AccountType accountType = new AccountType(newTypeName);

                AppendData(accountType);

                txfNombreCuentas.clear();
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Agregar Cuenta", getStage(),
                        "Cuenta agregada exitosamente!");
            }
        } catch (Exception ex) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Agregar Cuenta", getStage(),
                    "Error al agregar cuenta nueva!" + ex.getMessage());
        }
    }

    @FXML
    void OnActionBtnEliminar(ActionEvent event) {
        if (cmbCuentas.getSelectionModel().getSelectedItem() != null) {
            String selectedTypeName = cmbCuentas.getSelectionModel().getSelectedItem().toString();

            deleteItem(selectedTypeName);

            cmbCuentas.getItems().remove(selectedTypeName);
            cmbCuentas.getSelectionModel().clearSelection();

            txfNombreCuentas.clear();
            cmbCuentas.getSelectionModel().clearSelection();

            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Eliminar Cuenta", getStage(),
                    "Cuenta eliminada exitosamente!");
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Cuenta", getStage(),
                    "Seleccione una cuenta para eliminar!");
        }

    }

    @FXML
    void OnActionBtnEditar(ActionEvent event) {
        String selectedTypeName = cmbCuentas.getSelectionModel().getSelectedItem();
        String id = cmbCuentas.getSelectedItem();

        if (selectedTypeName != null) {
            if (txfNombreCuentas.getText() == null || txfNombreCuentas.getText().isEmpty()) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Editar Cuenta", getStage(),
                        "Ingrese un nuevo nombre para la cuenta!");
                return;
            }
            String newTypeName = txfNombreCuentas.getText();

            int SelectedIndex = cmbCuentas.getSelectionModel().getSelectedIndex();
            cmbCuentas.getItems().remove(SelectedIndex);
            cmbCuentas.getItems().add(SelectedIndex, newTypeName);
            cmbCuentas.getSelectionModel().clearSelection();
            txfNombreCuentas.setText(newTypeName);
            cmbCuentas.clear();

            editItem(id, newTypeName);

            cmbCuentas.clear();
            txfNombreCuentas.clear();
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Editar Cuenta", getStage(),
                    "Cuenta editada exitosamente!");
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Editar Cuenta", getStage(),
                    "Seleccione una cuenta para editar!");
        }
    }

    public void LoadTXTFile() {
        try {

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            ObservableList<String> accounts = FXCollections.observableArrayList();
            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");
                String name = partes[0];
                accounts.add(name);
            }

            cmbCuentas.setItems(accounts);
            br.close();

        } catch (IOException ex) {

            Logger.getLogger(MantenimientoCuentasController.class.getName()).log(Level.SEVERE, "Error al leer archivo", ex);

        }

    }

    public void AppendData(AccountType accountType) {
        try (FileWriter fileWriter = new FileWriter(filePath, true); BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(accountType.toString() + '\n');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteItem(String id) {
        File account = new File(filePath);
        StringBuffer temp = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader(new FileReader(account));
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length >= 1 && !partes[0].equals(id)) {
                    temp.append(line).append("\r\n");
                } else {
                    System.out.println("ID found and deleted: " + id);
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileWriter writer = new FileWriter(account);
            writer.write(temp.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void editItem(String id, String newLine) {
        File account = new File(filePath);
        StringBuffer temp = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader(new FileReader(account));
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length >= 1 && partes[0].equals(id)) {
                    temp.append(newLine).append("\r\n");
                    System.out.println("ID encontrado y editado: " + id);
                } else {
                    temp.append(line).append("\r\n");
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileWriter writer = new FileWriter(account);
            writer.write(temp.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Archivo actualizado exitosamente");
    }

    @FXML
    void OnActionCmbCuentas(ActionEvent event) {

        String selectedTypeName = cmbCuentas.getSelectionModel().getSelectedItem();
        ;
        if (selectedTypeName != null) {
            System.out.print(selectedTypeName + "\n");
            txfNombreCuentas.setText(selectedTypeName);
        } else {
        }

    }

}
