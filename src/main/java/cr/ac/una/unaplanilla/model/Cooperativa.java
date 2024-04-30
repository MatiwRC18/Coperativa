package cr.ac.una.unaplanilla.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;

public class Cooperativa {

    public SimpleStringProperty name;
    public SimpleStringProperty logo;

    public Cooperativa() {
        this.name = new SimpleStringProperty();
        this.logo = new SimpleStringProperty();
    }

    public Cooperativa(String name, String logo) {
        this();
        this.name.set(name);
        this.logo.set(logo);
    }

    public Cooperativa(Cooperativa cooperativa) {
        this();
        this.name.set(cooperativa.getName());
        this.logo.set(cooperativa.getLogo());
    }

    public void setCooperativa(Cooperativa cooperativa) {
        this.name.set(cooperativa.getName());
        this.logo.set(cooperativa.getLogo());
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getLogo() {
        return logo.get();
    }

    public void setLogo(String logo) {
        this.logo.set(logo);
    }

    public String toString() {
        return name.get() + "," + logo.get();
    }

    public void createFile(Cooperativa cooperativa) {
        try {
            FileWriter file = new FileWriter("Cooperativa.txt");
            BufferedWriter writer = new BufferedWriter(file);
            writer.write(cooperativa.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
