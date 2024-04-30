package cr.ac.una.unaplanilla.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import cr.ac.una.unaplanilla.model.Account;
import cr.ac.una.unaplanilla.model.AccountType;
import cr.ac.una.unaplanilla.model.Associated;
import cr.ac.una.unaplanilla.util.AppContext;
import cr.ac.una.unaplanilla.util.Mensaje;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class AperturaCuentasController extends Controller implements Initializable {

    @FXML
    private MFXListView<AccountType> listVActivas;

    @FXML
    private MFXListView<AccountType> listVDisponibles;

    @FXML
    private MFXButton btnGuardar;

    @FXML
    private MFXButton btnMostrar;

    @FXML
    private MFXButton btnBarrer;

    @FXML
    private MFXTextField txfFolio;

    @FXML
    private Label lblNombre;

    Account account = new Account();

    private ObservableList<AccountType> accountType;
    private ObservableList<Account> accounts;
    private ObservableList<Associated> asociate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initialize() {
        asociate = ((ObservableList<Associated>) AppContext.getInstance().get("Asociados"));
        accountType = ((ObservableList<AccountType>) AppContext.getInstance().get("TiposCuentas"));
        accounts = ((ObservableList<Account>) AppContext.getInstance().get("Cuentas"));
        listVDisponibles.getItems().clear();
        readAccount();
        loadInfo();
        readAsociado();
    }

    @FXML
    void OnActionBtnMostrar(ActionEvent event) {

        String folio = txfFolio.getText();

        if (txfFolio.getText().isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar Folio", getStage(), "Debe ingresar un folio");
            return;
        }

        for (Associated associated : asociate) {
            if (associated.getFolio().equals(folio)) {
                lblNombre.setText(associated.getName().toString());
            }
        }

        listVActivas.getItems().clear();

        try (BufferedReader br = new BufferedReader(new FileReader("cuentas.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String folioInFile = parts[0];
                if (folioInFile.equals(folio)) {
                    String accountType = parts[1];
                    AccountType type = new AccountType(accountType);
                    listVActivas.getItems().add(type);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(AperturaCuentasController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    @FXML
    void OnDragDetectedActivas(MouseEvent event) {
        OnDragDetectedListas(event, listVActivas);
    }

    @FXML
    void OnDragDetectedDisponibles(MouseEvent event) {
        OnDragDetectedListas(event, listVDisponibles);
    }

    @FXML
    void OnDragDroppedActivas(DragEvent event) {
        if (event.getDragboard().hasString()) {
            String accountTypeStr = event.getDragboard().getString();
            AccountType newAccount = new AccountType(accountTypeStr);

            boolean alreadyHasAccount = listVActivas.getItems().stream()
                    .map(AccountType::toString)
                    .anyMatch(accountTypeStr::equals);

            if (alreadyHasAccount) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Agregar Cuenta", getStage(),
                        "El asociado ya tiene una cuenta activa del mismo tipo");
                return;
            }

            listVActivas.getItems().add(newAccount);

            // Mostrar mensaje de éxito
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Agregar Cuenta", getStage(),
                    "La cuenta ha sido agregada exitosamente");
        }
    }

    @FXML
    void OnDragDroppedDisponibles(DragEvent event) {

        if (event.getDragboard().hasString()) {
            String accountType = event.getDragboard().getString();

            boolean canDeactivate = true;
            for (Account account : accounts) {
                if (account.getAccountType().equals(accountType) && !account.getBalance().equals("0.0")) {
                    canDeactivate = false;
                    System.out.println("entra al if");
                    break;
                }
            }

            if (!canDeactivate) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Desactivar Cuenta", getStage(), "No se puede desactivar la cuenta debido a fondos en ella");
                return;
            }

            deleteAccountFromFile(txfFolio.getText(), accountType);

            AccountType deactivatedAccount = new AccountType(accountType);
            listVActivas.getItems().remove(deactivatedAccount);

            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Desactivar Cuenta", getStage(), "La cuenta ha sido desactivada exitosamente");
        }

    }

    @FXML
    void OnDragOverDisponibles(DragEvent event) {

        OnDragOverListas(event, listVDisponibles);

    }

    @FXML
    void OnDragOverActivas(DragEvent event) {

        OnDragOverListas(event, listVActivas);

    }

    private void deleteAccountFromFile(String folio, String accountType) {
        try {
            File inputFile = new File("cuentas.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String lineToRemove = folio + "," + accountType;
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.contains(lineToRemove)) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
            writer.close();
            reader.close();

            if (!inputFile.delete()) {
                System.out.println("No se pudo eliminar el archivo original");
                return;
            }
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("No se pudo renombrar el archivo temporal");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readAccount() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("AccountType.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                AccountType account = new AccountType(name);
                accountType.add(account);
                if (!accountType.contains(account)) {
                    accountType.add(account);
                }
            }
            br.close();

        } catch (IOException ex) {

            Logger.getLogger(AperturaCuentasController.class.getName()).log(Level.SEVERE, "Error al leer archivo", ex);

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

            Logger.getLogger(MantenimientoAsoController.class.getName()).log(Level.SEVERE, "Error al leer archivo", ex);

        }
    }

    private void loadInfo() {

        listVDisponibles.setItems(accountType);
        listVDisponibles.setOnDragDetected(this::OnDragDetectedDisponibles);
        listVDisponibles.setOnDragOver(this::OnDragOverDisponibles);
        listVDisponibles.setOnDragDropped(this::OnDragDroppedDisponibles);
        listVActivas.setOnDragDetected(this::OnDragDetectedActivas);
        listVActivas.setOnDragOver(this::OnDragOverActivas);
        listVActivas.setOnDragDropped(this::OnDragDroppedActivas);

    }

    private void OnDragDetectedListas(MouseEvent event, MFXListView<AccountType> lista) {
        String itemToDrag = lista.getSelectionModel().getSelectedValue().toString();
        Dragboard dragboard = lista.startDragAndDrop(TransferMode.COPY);
        ClipboardContent content = new ClipboardContent();
        content.putString(itemToDrag);
        dragboard.setContent(content);

        event.consume();

    }

    private void OnDragDroppedListas(DragEvent event, MFXListView<AccountType> lista) {

        String item = event.getDragboard().getString();
        lista.getItems().add(new AccountType(item));

        MFXListView<?> listaOrigen = (MFXListView<?>) event.getGestureSource();
        listaOrigen.getItems().remove(new AccountType(item));
        event.setDropCompleted(true);
        event.consume();

    }

    private void OnDragOverListas(DragEvent event, MFXListView<AccountType> lista) {
        if (event.getGestureSource() != event.getSource() && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    private void writeAccountToFile(Account account) {
        try {
            FileWriter writer = new FileWriter("cuentas.txt", true);
            writer.write(account.getId() + "," + account.getAccountType() + "," + account.getBalance() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnActionBtnGuardar(ActionEvent event) {
        if (txfFolio.getText().isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar Folio", getStage(), "Debe ingresar un folio de asociado");
            return;
        }

        if (listVActivas.getItems().isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar Cuentas", getStage(), "No hay cuentas agregadas para guardar");
            return;
        }

        String folio = txfFolio.getText();

        for (AccountType accountType : listVActivas.getItems()) {
            String accountTypeStr = accountType.toString();
            if (!accountExists(folio, accountTypeStr)) {
                Account newAccount = new Account(folio, accountTypeStr);
                writeAccountToFile(newAccount);
            }
        }

        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Guardar Cuentas", getStage(), "Cuentas guardadas exitosamente para el asociado con folio: " + folio);
    }

    private boolean accountExists(String folio, String accountType) {
        try (BufferedReader br = new BufferedReader(new FileReader("cuentas.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String folioInFile = parts[0];
                String accountTypeInFile = parts[1];
                if (folioInFile.equals(folio) && accountTypeInFile.equals(accountType)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(AperturaCuentasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @FXML
    void OnActionBtnBarrer(ActionEvent event) {
        txfFolio.clear();
        lblNombre.setText("");
        listVActivas.getItems().clear();
    }

}
