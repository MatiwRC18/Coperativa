package cr.ac.una.unaplanilla.model;

import javafx.beans.property.SimpleStringProperty;

public class Account {

    public SimpleStringProperty id;
    public SimpleStringProperty accountType;
    public SimpleStringProperty balance;

    public Account() {

        this.id = new SimpleStringProperty();
        this.accountType = new SimpleStringProperty();
        this.balance = new SimpleStringProperty();

    }

    public Account(String accountType) {

        this();
        this.accountType.set(accountType.toString());

    }

    public Account(String id, String accountType, String balance) {

        this();
        this.id.set(id);
        this.accountType.set(accountType);
        this.balance.set(balance);

    }

    public Account(String id, AccountType accountType, String balance) {

        this();
        this.id.set(id);
        this.accountType.set(accountType.toString());
        this.balance.set(balance);

    }

    public Account(Account account) {

        this();
        this.id.set(account.getId());
        this.accountType.set(account.getAccountType());
        this.balance.set(account.getBalance());

    }

    public void setAccount(Account account) {
        this.id.set(account.getId());
        this.accountType.set(account.getAccountType());
        this.balance.set(account.getBalance());

    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getBalance() {
        return balance.get();
    }

    public void setBalance(String balance) {
        this.balance.set(balance);
    }

    public String getAccountType() {
        return accountType.get();
    }

    public void setAccountType(String accountType) {
        this.accountType.set(accountType);
    }

    public String toStringName() {
        return accountType.get();
    }

    public String toString() {
        return id.get() + " " + accountType.get() + " " + balance.get();
    }

    public Account(String folio, String accountType) {
        this();
        this.id.set(folio);
        this.accountType.set(accountType);
        this.balance.set("0");

    }

}
