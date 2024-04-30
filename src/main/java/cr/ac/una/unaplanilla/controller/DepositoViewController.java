package cr.ac.una.unaplanilla.controller;

import cr.ac.una.unaplanilla.model.Account;
import cr.ac.una.unaplanilla.util.AppContext;
import cr.ac.una.unaplanilla.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepositoViewController extends Controller implements Initializable {

    String filePath = "cuentas.txt";

    @FXML
    private MFXTextField txfFolio;

    @FXML
    private MFXButton btnBuscar;

    @FXML
    private MFXButton btnDepositar;

    @FXML
    private MFXComboBox<String> cmbCuentas;

    @FXML
    private Spinner<Integer> SpinnerCinco;

    @FXML
    private Spinner<Integer> SpinnerDiez;

    @FXML
    private Spinner<Integer> SpinnerVeintiCinco;

    @FXML
    private Spinner<Integer> SpinnerCincuenta;

    @FXML
    private Spinner<Integer> SpinnerCien;

    @FXML
    private Spinner<Integer> SpinnerQuinientos;

    @FXML
    private Spinner<Integer> SpinnerMil;

    @FXML
    private Spinner<Integer> SpinnerDosMil;

    @FXML
    private Spinner<Integer> SpinnerCincoMil;

    @FXML
    private Spinner<Integer> SpinnerDiezmil;

    @FXML
    private Spinner<Integer> SpinnerVeintemil;

    private ObservableList<Account> accounts;

    @Override
    public void initialize() {
        accounts = ((ObservableList<Account>) AppContext.getInstance().get("Cuentas"));
        ReadFolio();
        readAsociado();

        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,
                0);
        SpinnerCinco.setValueFactory(valueFactory2);

        SpinnerValueFactory<Integer> valueFactory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,
                0);
        SpinnerDiez.setValueFactory(valueFactory3);

        SpinnerValueFactory<Integer> valueFactory4 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,
                0);
        SpinnerVeintiCinco.setValueFactory(valueFactory4);

        SpinnerValueFactory<Integer> valueFactory5 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,
                0);
        SpinnerCincuenta.setValueFactory(valueFactory5);

        SpinnerValueFactory<Integer> valueFactory6 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,
                0);
        SpinnerCien.setValueFactory(valueFactory6);

        SpinnerValueFactory<Integer> valueFactory7 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,
                0);
        SpinnerQuinientos.setValueFactory(valueFactory7);

        SpinnerValueFactory<Integer> valueFactory8 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,
                0);
        SpinnerMil.setValueFactory(valueFactory8);

        SpinnerValueFactory<Integer> valueFactory9 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,
                0);
        SpinnerDosMil.setValueFactory(valueFactory9);

        SpinnerValueFactory<Integer> valueFactory10 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,
                0);
        SpinnerCincoMil.setValueFactory(valueFactory10);

        SpinnerValueFactory<Integer> valueFactory11 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,
                0);
        SpinnerDiezmil.setValueFactory(valueFactory11);

        SpinnerValueFactory<Integer> valueFactory12 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,
                0);
        SpinnerVeintemil.setValueFactory(valueFactory12);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        cmbCuentas.getItems().clear();
        List<String> accountTypes = searchFolioInFile(folio);

        if (!accountTypes.isEmpty()) {
            cmbCuentas.setItems(FXCollections.observableArrayList(accountTypes));
        } else {
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Buscar Folio", getStage(),
                    "No se encontraron cuentas asociadas al folio " + folio);
        }
    }

    @FXML
    public void OnActionBtnDepositar(ActionEvent event) {
        if (cmbCuentas.getSelectionModel().getSelectedItem() == null) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar Folio", getStage(),
                    "Debe seleccionar un tipo de cuenta");
            return;
        }
        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Depósito", getStage(),
                "Se han depositado " + SumMoney() + " a la cuenta");
        String folio = txfFolio.getText();
        String selectedAccountType = cmbCuentas.getSelectionModel().getSelectedItem();

        int depositAmount = SumMoney();

        updateBalanceForAccountTypeInFile(folio, selectedAccountType, depositAmount);

        registrarMovimiento(folio, selectedAccountType, "Depósito", depositAmount);

        cmbCuentas.clear();
        cmbCuentas.getSelectionModel().clearSelection();
        CleanSpinners();
    }

    public int SumMoney() {
        return SpinnerCinco.getValue() * 5 + SpinnerDiez.getValue() * 10 + SpinnerVeintiCinco.getValue() * 25
                + SpinnerCincuenta.getValue() * 50 + SpinnerCien.getValue() * 100
                + SpinnerQuinientos.getValue() * 500
                + SpinnerMil.getValue() * 1000 + SpinnerDosMil.getValue() * 2000
                + SpinnerCincoMil.getValue() * 5000
                + SpinnerDiezmil.getValue() * 10000 + SpinnerVeintemil.getValue() * 20000;
    }

    public void ReadFolio() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String folio = parts[0];
                Account acc = new Account(folio);
                accounts.add(acc);
            }
            br.close();

        } catch (IOException ex) {
            Logger.getLogger(AperturaCuentasController.class.getName()).log(Level.SEVERE, "Error al leer archivo", ex);
        }

    }

    public void readAsociado() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String folio = parts[0];
                String AccountType = parts[1];
                String Balance = parts[2];
                Account acc = new Account(folio, AccountType, Balance);
                accounts.add(acc);
            }
            br.close();

        } catch (IOException ex) {
            Logger.getLogger(MantenimientoAsoController.class.getName()).log(Level.SEVERE, "Error al leer archivo", ex);
        }
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

    private void updateBalanceForAccountType() {
        boolean act = false;
        String totalMoney = String.valueOf(SumMoney());
        String folio = txfFolio.getText();
        String selectedAccountType = cmbCuentas.getSelectedItem().toString();

        for (Account account : accounts) {
            if (account.getId() == null || account.getAccountType() == null) {
                continue;
            }

            if (account.getId().equals(folio) && account.getAccountType().equals(selectedAccountType)) {
                account.setBalance(totalMoney);
                act = true;
                break;
            }
        }

        if (act) {
            try {
                File file = new File(filePath);
                file.createNewFile();

                StringBuilder sb = new StringBuilder();
                for (Account account : accounts) {
                    if (account.getId() == null || account.getBalance() == null || account.getAccountType() == null) {
                        continue;
                    }
                    sb.append(account.getId()).append(",")
                            .append(account.getAccountType()).append(",")
                            .append(account.getBalance()).append("\n");

                }
                String content = sb.toString();

                java.nio.file.Files.write(file.toPath(), content.getBytes());
            } catch (IOException e) {
                Logger.getLogger(RetiroDepositoController.class.getName()).log(Level.SEVERE, "Error al sobreescribir", e);
            }
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Encontrar asociado", getStage(),
                    "No se encontró el asociado");
        }
    }

    public void CleanSpinners() {
        SpinnerCinco.getValueFactory().setValue(0);
        SpinnerDiez.getValueFactory().setValue(0);
        SpinnerVeintiCinco.getValueFactory().setValue(0);
        SpinnerCincuenta.getValueFactory().setValue(0);
        SpinnerCien.getValueFactory().setValue(0);
        SpinnerQuinientos.getValueFactory().setValue(0);
        SpinnerMil.getValueFactory().setValue(0);
        SpinnerDosMil.getValueFactory().setValue(0);
        SpinnerCincoMil.getValueFactory().setValue(0);
        SpinnerDiezmil.getValueFactory().setValue(0);
        SpinnerVeintemil.getValueFactory().setValue(0);
    }

    private void updateBalanceForAccountTypeInFile(String folio, String accountType, int additionalAmount) {
        String currentBalanceStr = getBalanceFromAccountType(folio, accountType);
        if (currentBalanceStr != null) {
            int currentBalance = Integer.parseInt(currentBalanceStr);
            int newBalance = currentBalance + additionalAmount;
            String newBalanceStr = String.valueOf(newBalance);
            updateBalanceForAccountTypeInFile(folio, accountType, newBalanceStr);
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error", getStage(),
                    "No se pudo encontrar el saldo actual para el tipo de cuenta " + accountType);
        }
    }

    private void updateBalanceForAccountTypeInFile(String folio, String accountType, String newBalance) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(folio) && parts[1].equals(accountType)) {
                    line = parts[0] + "," + parts[1] + "," + newBalance;
                }
                lines.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(RetiroDepositoController.class.getName()).log(Level.SEVERE, "Error al leer archivo", ex);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(RetiroDepositoController.class.getName()).log(Level.SEVERE, "Error al escribir archivo", ex);
        }
    }

    private String getBalanceFromAccountType(String folio, String accountType) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(folio) && parts[1].equals(accountType)) {
                    return parts[2];
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RetiroDepositoController.class.getName()).log(Level.SEVERE, "Error al leer archivo", ex);
        }
        return null;
    }

    private void registrarMovimiento(String folio, String tipoCuenta, String tipoMovimiento, int monto) {
        String movimientosFile = "movimientos.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(movimientosFile, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            writer.write(formattedDateTime + "," + folio + "," + tipoCuenta + "," + tipoMovimiento + "," + monto);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
