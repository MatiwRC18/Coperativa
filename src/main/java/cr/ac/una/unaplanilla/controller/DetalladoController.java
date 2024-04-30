package cr.ac.una.unaplanilla.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import cr.ac.una.unaplanilla.model.Associated;
import cr.ac.una.unaplanilla.model.Movimiento;
import cr.ac.una.unaplanilla.util.AppContext;
import cr.ac.una.unaplanilla.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class DetalladoController extends Controller implements Initializable {

    @FXML
    private MFXButton btnSalir;

    @FXML
    private AnchorPane root;
    @FXML
    private StackPane StackPaneDetallado;

    @FXML
    private TableView<Movimiento> tblVDetallado;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblApellido;
    @FXML
    private Label lblEdad;
    @FXML
    private MFXTextField txfFolio;
    @FXML
    private MFXButton btnBuscar;
    @FXML
    private MFXButton btnImprimirPDF;

    Associated associado = new Associated();
    private ObservableList<Associated> asociate;

    @Override
    public void initialize() {
        asociate = ((ObservableList<Associated>) AppContext.getInstance().get("Asociados"));
        readAsociado();
        if (tblVDetallado.getColumns().isEmpty()) {
            // Crear columnas y establecer sus propiedades
            TableColumn<Movimiento, LocalDateTime> colFechaHora = new TableColumn<>("Fecha y Hora");
            colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));

            TableColumn<Movimiento, String> colFolio = new TableColumn<>("Folio");
            colFolio.setCellValueFactory(new PropertyValueFactory<>("folio"));

            TableColumn<Movimiento, String> colTipoCuenta = new TableColumn<>("Tipo de Cuenta");
            colTipoCuenta.setCellValueFactory(new PropertyValueFactory<>("tipoCuenta"));

            TableColumn<Movimiento, String> colTipoMovimiento = new TableColumn<>("Tipo de Movimiento");
            colTipoMovimiento.setCellValueFactory(new PropertyValueFactory<>("tipoMovimiento"));

            TableColumn<Movimiento, Integer> colMonto = new TableColumn<>("Monto");
            colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));

            // Agregar las columnas a la TableView
            tblVDetallado.getColumns().addAll(colFechaHora, colFolio, colTipoCuenta, colTipoMovimiento, colMonto);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    void OnActionBtnSalir(ActionEvent event) {
        txfFolio.clear();
        lblEdad.setText("");
        lblNombre.setText("");
        lblApellido.setText("");
        tblVDetallado.getItems().clear();
        ((Stage) btnSalir.getScene().getWindow()).close();

    }

    @FXML
    private void OnActionBtnBuscar(ActionEvent event) {

        if (txfFolio.getText().isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar Folio", getStage(), "Debe ingresar un folio");
        }

        String folio = txfFolio.getText();
        tblVDetallado.getItems().clear();

        try (BufferedReader br = new BufferedReader(new FileReader("movimientos.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Dividir la línea en sus componentes
                String[] parts = line.split(",");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // Parsear la fecha y hora utilizando el DateTimeFormatter personalizado
                LocalDateTime fechaHora = LocalDateTime.parse(parts[0], formatter);

                String folioMovimiento = parts[1];
                String tipoCuenta = parts[2];
                String tipoMovimiento = parts[3];
                int monto = Integer.parseInt(parts[4]);

                // Verificar si el folio del movimiento coincide con el folio buscado
                if (folioMovimiento.equals(folio)) {
                    // Crear un objeto Movimiento con los datos y agregarlo a la TableView
                    Movimiento movimiento = new Movimiento(fechaHora, folioMovimiento, tipoCuenta, tipoMovimiento, monto);
                    tblVDetallado.getItems().add(movimiento);
                }
            }

            for (Associated associate : asociate) {
                if (associate.getFolio().equals(folio)) {
                    this.associado = associate;
                    lblNombre.setText(associate.getName().toString());
                    lblApellido.setText(associate.getLastName().toString());
                    lblEdad.setText(associate.getAge().toString());

                }
            }
        } catch (Exception e) {
            Logger.getLogger(ImpresionCarnetController.class.getName()).log(Level.SEVERE, "Error al buscar asociado", e);

        }

    }

    @FXML
    private void OnActionBtnImprimirPDF(ActionEvent event) {
        if (txfFolio.getText().isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Ingrese un Folio", getStage(), "Debe ingresar un folio antes");
            return;
        }

        try {

            Document documento = new Document(PageSize.A4);
            PdfWriter.getInstance(documento, new FileOutputStream(
                    "Detalles de Cuenta " + this.associado.getName() + " " + this.associado.getFolio() + ".pdf"));
            documento.open();

            WritableImage image = StackPaneDetallado.snapshot(new SnapshotParameters(), null);
            File output = new File("Detalles de Cuenta " + this.associado.getName() + " " + this.associado.getFolio() + " .png");

            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", output);
            float pageWidth = documento.getPageSize().getWidth();
            float pageHeight = documento.getPageSize().getHeight();
            BufferedImage bufferedImage = ImageIO.read(output);
            float imageWidth = bufferedImage.getWidth();
            float imageHeight = bufferedImage.getHeight();
            float scale = Math.min(pageWidth / imageWidth, pageHeight / imageHeight);

            Image imagen = Image.getInstance(output.getPath());
            imagen.scaleToFit(imageWidth * scale, imageHeight * scale);
            imagen.setAlignment(Image.ALIGN_CENTER);
            documento.add(imagen);

            documento.close();

            Desktop desktop = Desktop.getDesktop();

            File file = new File("Detalles de Cuenta " + this.associado.getName() + " " + this.associado.getFolio() + ".pdf");
            desktop.open(file);

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

}
