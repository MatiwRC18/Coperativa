package cr.ac.una.unaplanilla;

import cr.ac.una.unaplanilla.model.Account;
import cr.ac.una.unaplanilla.model.AccountType;
import cr.ac.una.unaplanilla.model.Associated;
import cr.ac.una.unaplanilla.model.Cooperativa;
import cr.ac.una.unaplanilla.util.AppContext;
import cr.ac.una.unaplanilla.util.FlowController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaFX App
 */
public class App extends Application {
private static String accesParameter = "";
    Cooperativa cooperativa = new Cooperativa();

    ObservableList<Cooperativa> cooperativas = FXCollections.observableArrayList();
    ObservableList<Account> accounts = FXCollections.observableArrayList();
    ObservableList<AccountType> accountType = FXCollections.observableArrayList();
    ObservableList<Associated> associate = FXCollections.observableArrayList();


    @Override
    public void start(Stage stage) throws IOException {

        readCoope();
        for (Cooperativa cooperativa : cooperativas) {
            stage.setTitle(cooperativa.getName());
            stage.getIcons().add(new Image(cooperativa.getLogo()));
        }

        AppContext.getInstance().set("cooperativa", cooperativas);
        AppContext.getInstance().set("Asociados", associate);
        AppContext.getInstance().set("Cuentas", accounts);
        AppContext.getInstance().set("TiposCuentas", accountType);

        FlowController.getInstance().InitializeFlow(stage, null);

        if(accesParameter.equals("P")) {
            FlowController.getInstance().goMain("ProfesoresView");
        } else if(accesParameter.equals("F")){
            FlowController.getInstance().goMain("FuncionariosView");
        } else if (accesParameter.equals("A")) {
            FlowController.getInstance().goMain("AsociadosView");
        }else
            FlowController.getInstance().goMain("UsuariosView");


    }
    public void readCoope() {
        try {
            File file = new File("Cooperativa.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String logo = parts[1];
                Cooperativa cooperativa = new Cooperativa(name, logo);
                cooperativas.add(cooperativa);
            }
            br.close();

        } catch (IOException ex) {

            Logger.getLogger(App.class.getName()).log(Level.SEVERE, "Error al leer archivo", ex);

        }
    }

    public static void main(String[] args) {
if (args.length > 0){
    accesParameter = args[0];
}
        launch();
    }


}