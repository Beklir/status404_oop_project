package parkinglot.users;

import parkinglot.constants.AccountStatus;

public abstract class Account {
    private String userName;
    private String password;
    private AccountStatus status;
    private Person person;

    public Account(String userName, String password, Person person) {
        this.userName = userName;
        this.password = password;
        this.person = person;
        this.status = AccountStatus.ACTIVE;
    }

    public boolean login(String userName, String password) {
        if (status != AccountStatus.ACTIVE) {
            System.out.println("Account is not active. Status: " + status);
            return false;
        }
        if (this.userName.equals(userName) && this.password.equals(password)) {
            System.out.println("Login successful for user: " + userName);
            return true;
        }
        System.out.println("Invalid credentials for user: " + userName);
        return false;
    }

    public void logout() {
        System.out.println("User " + userName + " logged out.");
    }

    // Getters and Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }

    // Password setter only — never expose password as getter
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return String.format("%s{userName='%s', status=%s}", getClass().getSimpleName(), userName, status);
    }
}