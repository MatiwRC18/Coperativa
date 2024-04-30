package cr.ac.una.unaplanilla.model;

import javafx.beans.property.SimpleStringProperty;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class AccountType {

    public SimpleStringProperty name;

    public AccountType() {
        this.name = new SimpleStringProperty();
    }

    public AccountType(String name) {
        this();
        this.name.set(name);
    }

    public AccountType(AccountType accountType) {
        this();
        this.name.set(accountType.getName());
    }

    public void setAccountType(AccountType accountType) {
        this.name.set(accountType.getName());
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public String toString() {
        return name.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountType that = (AccountType) o;
        return Objects.equals(name.get(), that.name.get());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public void createFile(AccountType accountType) {
        try {
            FileWriter file = new FileWriter("AccountType.txt", true);
            BufferedWriter writer = new BufferedWriter(file);
            writer.write(accountType.toString() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
