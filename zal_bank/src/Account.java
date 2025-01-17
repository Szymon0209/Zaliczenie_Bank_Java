public class Account {
    private String firstName;
    private String lastName;
    private String address;
    private String birthDate;
    private String pesel;
    private String login;
    private String password;
    private String email;
    private String accountNumber;
    private double balance;

    public Account(String firstName, String lastName, String address, String birthDate,
                   String pesel, String login, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthDate = birthDate;
        this.pesel = pesel;
        this.login = login;
        this.password = password;
        this.email = email;
        this.accountNumber = generateAccountNumber();
        this.balance = 0.0;
    }

    private String generateAccountNumber() {
        return "PL" + (int)(Math.random() * 1_000_000_000);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPesel() {
        return pesel;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}