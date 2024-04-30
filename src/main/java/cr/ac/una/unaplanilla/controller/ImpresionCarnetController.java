package cr.ac.una.unaplanilla.controller;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javafx.scene.image.ImageView;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import cr.ac.una.unaplanilla.model.Associated;
import cr.ac.una.unaplanilla.model.Cooperativa;
import cr.ac.una.unaplanilla.util.AppContext;
import cr.ac.una.unaplanilla.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

public class ImpresionCarnetController extends Controller implements Initializable {

    @FXML
    private MFXButton btnIngresarFolio;

    @FXML
    private MFXButton btnImprimirCarnet;

    @FXML
    private ImageView imgFoto;

    @FXML
    private ImageView imgLogo;

    @FXML
    private Label lblTextoEncabezado;

    @FXML
    private MFXTextField txfFolioCarnet;

    @FXML
    private Label lblApellido;

    @FXML
    private Label lblEdad;

    @FXML
    private Label lblFolio;

    @FXML
    private Label lblNombre;

    @FXML
    public MFXButton btnBarrer;

    @FXML
    private AnchorPane root;

    @FXML
    private StackPane StackPaneCarnet;

    Associated associado = new Associated();

    Cooperativa cooperativa = new Cooperativa();

    private ObservableList<Cooperativa> cooperativas;
    private ObservableList<Associated> asociate;

    @Override
    public void initialize() {
        cooperativas = ((ObservableList<Cooperativa>) AppContext.getInstance().get("cooperativa"));
        asociate = ((ObservableList<Associated>) AppContext.getInstance().get("Asociados"));
        cargarCoopeInfo();
        readAsociado();
        cargarCarnetInfo();

    }

    @FXML
    void OnActionBtnIngresarFolio(ActionEvent event) {

        if (txfFolioCarnet.getText().isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar Folio", getStage(), "Debe ingresar un folio");
        }

        try {
            String folio = txfFolioCarnet.getText();

            for (Associated associate : asociate) {
                if (associate.getFolio().equals(folio)) {
                    this.associado = associate;
                    lblFolio.setText(associate.getFolio().toString());
                    lblNombre.setText(associate.getName().toString());
                    lblApellido.setText(associate.getLastName().toString());
                    lblEdad.setText(associate.getAge().toString());
                    String photo = associate.getFolio() + ".jpg";
                    String path = "./Photos/" + photo;
                    File file = new File(path);

                    if (file.exists()) {
                        javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
                        imgFoto.setImage(image);
                    }
                    System.out.println("Asociado encontrado");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ImpresionCarnetController.class.getName()).log(Level.SEVERE, "Error al buscar asociado", e);

        }

    }

    @FXML
    void OnActionBtnImprimirCarnet(ActionEvent event) {

        if (txfFolioCarnet.getText().isEmpty()) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Ingrese un Folio", getStage(), "Debe ingresar un folio antes");
            return;
        }

        try {

            Document documento = new Document(PageSize.A4);
            PdfWriter.getInstance(documento, new FileOutputStream(
                    "Carnet " + this.associado.getName() + " " + this.associado.getFolio() + ".pdf"));
            documento.open();

            WritableImage image = StackPaneCarnet.snapshot(new SnapshotParameters(), null);
            File output = new File("Carnet " + this.associado.getName() + " " + this.associado.getFolio() + " .png");

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

            File file = new File("Carnet " + this.associado.getName() + " " + this.associado.getFolio() + ".pdf");
            desktop.open(file);

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void OnActionBtnBarrer(ActionEvent actionEvent) {
        lblFolio.setText("");
        lblEdad.setText("");
        lblNombre.setText("");
        lblApellido.setText("");
        txfFolioCarnet.clear();
        imgFoto.setImage(null);
    }

    public void cargarCarnetInfo() {

        coopeInfoCarnet();

    }

    public void cargarCoopeInfo() {

        try {
            BufferedReader br = new BufferedReader(new FileReader("Cooperativa.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.replaceAll("file:", "").split(",");
                String name = parts[0];
                String logo = parts[1];
                Cooperativa cooperativa = new Cooperativa(name, logo);
                cooperativas.add(cooperativa);
            }
            br.close();
        } catch (Exception e) {
            Logger.getLogger(ImpresionCarnetController.class.getName()).log(Level.SEVERE, "Error al buscar cooperativa", e);
        }

    }

    public void coopeInfoCarnet() {

        for (Cooperativa cooperativa : cooperativas) {
            lblTextoEncabezado.setText(cooperativa.getName());
            String logo = cooperativa.getLogo();
            File file = new File(logo);
            if (file.exists()) {
                System.out.println("File found: " + logo);
                javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
                if (image != null) {
                    System.out.println("Image created successfully");
                    imgLogo.setImage(image);
                } else {
                    System.out.println("Failed to create image object");
                }
            } else {
                System.out.println("File not found: " + logo);
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

            Logger.getLogger(MantenimientoAsoController.class.getName()).log(Level.SEVERE, "Error al leer archivo", ex);

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
