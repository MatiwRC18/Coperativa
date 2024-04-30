package cr.ac.una.unaplanilla.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;

public class Associated {

    public SimpleStringProperty name;
    public SimpleStringProperty lastName;
    public SimpleStringProperty folio;
    public SimpleStringProperty age;
    public SimpleStringProperty photo;

    public Associated() {

        this.name = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.folio = new SimpleStringProperty();
        this.age = new SimpleStringProperty();
        this.photo = new SimpleStringProperty();

    }

    public Associated(String name, String lastName, String folio, String age, String photo) {

        this();
        this.name.set(name);
        this.lastName.set(lastName);
        this.folio.set(folio.toString());
        this.age.set(age.toString());
        this.photo.set(photo.toString());

    }

    public Associated(String name, String lastName, String age, String photo) {

        this();
        this.name.set(name);
        this.lastName.set(lastName);
        this.age.set(age);
        this.photo.set(photo);

    }

    public Associated(Associated associade) {

        this();
        this.name.set(associade.getName());
        this.lastName.set(associade.getLastName());
        this.folio.set(associade.getFolio());
        this.age.set(associade.getAge());
        this.photo.set(associade.getPhoto());
    }

    public void setAssociated(Associated associate) {
        this.name.set(associate.getName());
        this.lastName.set(associate.getLastName());
        this.folio.set(associate.getFolio());
        this.age.set(associate.getAge());
        this.photo.set(associate.getPhoto());

    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getFolio() {
        return folio.get();
    }

    public void setFolio(String folio) {
        this.folio.set(folio);
    }

    public String getAge() {
        return age.get();
    }

    public void setAge(String age) {
        this.age.set(age);
    }

    public String getPhoto() {
        return photo.get();
    }

    public void setPhoto(String photo) {
        this.photo.set(photo);
    }

    @Override
    public String toString() {
        return name.get() + "," + lastName.get() + "," + folio.get() + "," + age.get() + "," + photo.get();
    }

    public void createFolio() {
        final int max = 20;
        final int min = 10;
        int range = max - min + 1;
        int random = (int) (Math.random() * range) + min;
        this.folio.set(name.get().substring(0, 1).toString() + lastName.get().substring(0, 1).toString() + age.get().toString() + random);
    }

    public void createFile(Associated associate) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter("Asociados.txt", true));
        try {

            writer.write(associate.toString());
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
