package cr.ac.una.unaplanilla.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author josue_5njzopn
 */
public class JuegoController extends Controller implements Initializable {

    @FXML
    private MFXButton btnIniciar;
    @FXML
    private MFXButton btnReiniciar;
    @FXML
    private Label lblPreguntas;
    @FXML
    private ImageView ImgMil;
    @FXML
    private ImageView imgDosMil;
    @FXML
    private ImageView imgCincoMil;
    @FXML
    private ImageView imgDiezMil;
    @FXML
    private ImageView imgVeinteMil;
    @FXML
    private ImageView imgCinco;
    @FXML
    private ImageView imgDiez;
    @FXML
    private ImageView imgVeinticinco;
    @FXML
    private ImageView imgCincuenta;
    @FXML
    private ImageView imgCien;
    @FXML
    private ImageView imgQuinientos;

    private ArrayList<String> preguntas = new ArrayList<>();
    private ArrayList<String> preguntasBarajadas = new ArrayList<>();

    private int preguntasRespondidas = 0;
    private int respuestasCorrectas = 0;
    private int respuestasIncorrectas = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
        cargarPreguntas();
        barajarPreguntas();

    }

    @FXML
    private void OnActionBtnIniciar(ActionEvent event) {
        mostrarPregunta();
    }

    @FXML
    private void OnActionBtnReiniciar(ActionEvent event) {
        preguntasRespondidas = 0;
        respuestasCorrectas = 0;
        respuestasIncorrectas = 0;
        barajarPreguntas();
        mostrarPregunta();
    }

    @FXML
    private void OnMouseClickedCinco(MouseEvent event) {
        verificarRespuesta(1);
    }

    @FXML
    private void OnMouseClickedDiez(MouseEvent event) {
        verificarRespuesta(2);
    }

    @FXML
    private void OnMouseClickedVeinticinco(MouseEvent event) {
        verificarRespuesta(3);
    }

    @FXML
    private void OnMouseClickedCincuenta(MouseEvent event) {
        verificarRespuesta(4);
    }

    @FXML
    private void OnMouseClickedCien(MouseEvent event) {
        verificarRespuesta(5);
    }

    @FXML
    private void OnMouseClickedQuinientos(MouseEvent event) {
        verificarRespuesta(6);
    }

    @FXML
    private void OnMouseClickedMil(MouseEvent event) {
        verificarRespuesta(7);

    }

    @FXML
    private void OnMouseClickedDosMil(MouseEvent event) {
        verificarRespuesta(8);
    }

    @FXML
    private void OnMouseClickedCincoMil(MouseEvent event) {
        verificarRespuesta(9);
    }

    @FXML
    private void OnMouseClickedDiezMil(MouseEvent event) {
        verificarRespuesta(10);
    }

    @FXML
    private void OnMouseClickedVeinteMil(MouseEvent event) {
        verificarRespuesta(11);
    }

    private void mostrarPregunta() {
        if (preguntasBarajadas.isEmpty()) {
            mostrarResultado(); // Si no hay más preguntas, mostrar el resultado final
        } else {
            String pregunta = preguntasBarajadas.remove(0); // Obtener y eliminar la primera pregunta barajada
            String preguntaSinRespuesta = pregunta.split(",")[0];
            lblPreguntas.setText(preguntaSinRespuesta);
        }
    }

    private void cargarPreguntas() {
        try (BufferedReader br = new BufferedReader(new FileReader("preguntas.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                String pregunta = partes[0];
                int respuesta = Integer.parseInt(partes[1]);
                preguntas.add(pregunta + "," + respuesta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void barajarPreguntas() {
        preguntasBarajadas.clear();
        preguntasBarajadas.addAll(preguntas);
        Collections.shuffle(preguntasBarajadas);
    }

    private void verificarRespuesta(int respuestaSeleccionada) {
        if (preguntasRespondidas >= 0 && preguntasRespondidas < preguntas.size()) {
            String[] preguntaYRespuesta = preguntas.get(preguntasRespondidas).split(",");
            int respuestaCorrecta = Integer.parseInt(preguntaYRespuesta[1]);

            if (respuestaSeleccionada == respuestaCorrecta) {
                respuestasCorrectas++;
            } else {
                respuestasIncorrectas++;
            }

            preguntasRespondidas++;

            if (preguntasRespondidas < preguntas.size()) {
                mostrarPregunta();
            } else {
                mostrarResultado();
            }
        } else {
        }
    }

    @FXML
    private void onImageClicked(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        int respuestaSeleccionada = Integer.parseInt(imageView.getId().substring(3));
        if (preguntasRespondidas >= 0 && preguntasRespondidas < preguntas.size()) {
            String preguntaActual = preguntasBarajadas.get(preguntasRespondidas);
            int respuestaCorrecta = Integer.parseInt(preguntaActual.split(",")[1]);

            if (respuestaSeleccionada == respuestaCorrecta) {
                respuestasCorrectas++;
            } else {
                respuestasIncorrectas++;
            }

            preguntasRespondidas++;

            if (preguntasRespondidas < preguntas.size()) {
                mostrarPregunta();
            }
        } else {
        }
    }

    private void mostrarResultado() {
        String mensaje = "Juego terminado";
        lblPreguntas.setText(mensaje);
    }
}
