import java.util.*;
import java.util.regex.*;

public class BankApplication {

    private static Map<String, String> users = new HashMap<>();
    private static Map<String, String> cardPins = new HashMap<>();
    private static Map<String, List<Account>> userAccounts = new HashMap<>();
    private static boolean isLoggedIn = false;
    private static boolean isBankStaffLoggedIn = false;
    private static String loggedInUser = "";
    private static String currentCardNumber = "";

    private static Map<String, Double> currencyRates = new HashMap<>();

    public static void main(String[] args) {
        currencyRates.put("USD", 1.0);
        currencyRates.put("EUR", 0.9);
        currencyRates.put("CHF", 1.1);
        currencyRates.put("GBP", 0.8);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWitaj w systemie bankowym!");
            System.out.println("1. Utwórz konto");
            System.out.println("2. Zaloguj się");
            System.out.println("3. Zaloguj się jako obsługa banku");
            System.out.println("4. Wyloguj się");
            System.out.println("5. Wykonaj przelew");
            System.out.println("6. Usługi bankomatowe");
            System.out.println("7. Zarządzaj kontem");
            System.out.println("8. Wyjdź");

            System.out.print("Wybierz opcję: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createAccount(scanner);
                    break;
                case 2:
                    login(scanner);
                    break;
                case 3:
                    loginAsBankStaff(scanner);
                    break;
                case 4:
                    logout();
                    break;
                case 5:
                    makeTransfer(scanner);
                    break;
                case 6:
                    atmServices(scanner);
                    break;
                case 7:
                    manageAccount(scanner);
                    break;
                case 8:
                    System.out.println("Dziękujemy za korzystanie z systemu. Do widzenia!");
                    return;
                default:
                    System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }
    }

    private static void createAccount(Scanner scanner) {
        System.out.println("Proszę podać swoje dane:");

        System.out.print("Imię: ");
        String firstName = scanner.nextLine();

        System.out.print("Nazwisko: ");
        String lastName = scanner.nextLine();

        System.out.print("Adres zamieszkania: ");
        String address = scanner.nextLine();

        System.out.print("Data urodzenia (yyyy-mm-dd): ");
        String birthDate = scanner.nextLine();

        System.out.print("PESEL: ");
        String pesel = scanner.nextLine();

        System.out.print("Login: ");
        String login = scanner.nextLine();

        String email = "";
        while (true) {
            System.out.print("E-mail: ");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Niepoprawny format adresu e-mail. Spróbuj ponownie.");
            }
        }

        String password = "";
        while (true) {
            System.out.print("Hasło: ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                break;
            } else {
                System.out.println("Hasło musi mieć co najmniej 8 znaków, zawierać dużą literę, małą literę i cyfrę.");
            }
        }

        String accountNumber = generateAccountNumber();
        String cardNumber = generateCardNumber();
        String pin = generatePIN();

        users.put(email, password);
        cardPins.put(cardNumber, pin);
        userAccounts.put(email, new ArrayList<>());

        Account account = new Account(accountNumber, "USD", 0.0);
        userAccounts.get(email).add(account);

        System.out.println("\nKonto zostało utworzone pomyślnie!");
        System.out.println("Numer rachunku: " + accountNumber);
        System.out.println("Numer karty: " + cardNumber);
        System.out.println("PIN: " + pin);
    }

    private static void login(Scanner scanner) {
        if (isLoggedIn) {
            System.out.println("Jesteś już zalogowany jako " + loggedInUser + ".");
            return;
        }

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.print("Hasło: ");
        String password = scanner.nextLine();

        if (users.containsKey(email) && users.get(email).equals(password)) {
            isLoggedIn = true;
            loggedInUser = email;
            System.out.println("Zalogowano pomyślnie.");
        } else {
            System.out.println("Błędny e-mail lub hasło.");
        }
    }

    private static void loginAsBankStaff(Scanner scanner) {
        if (isBankStaffLoggedIn) {
            System.out.println("Jesteś już zalogowany jako obsługa banku.");
            return;
        }

        System.out.print("Login obsługi banku: ");
        String bankStaffLogin = scanner.nextLine();

        System.out.print("Hasło obsługi banku: ");
        String bankStaffPassword = scanner.nextLine();

        String adminLogin = "admin";
        String adminPassword = "admin123";

        if (bankStaffLogin.equals(adminLogin) && bankStaffPassword.equals(adminPassword)) {
            isBankStaffLoggedIn = true;
            System.out.println("Zalogowano jako obsługa banku.");
            bankStaffMenu(scanner);
        } else {
            System.out.println("Błędny login lub hasło dla obsługi banku.");
        }
    }

    private static void bankStaffMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nMenu obsługi banku:");
            System.out.println("1. Pokaż listę użytkowników w systemie");
            System.out.println("2. Wyloguj");

            System.out.print("Wybierz opcję: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showAllUsers();
                    break;
                case 2:
                    logoutAsBankStaff();
                    return;
                default:
                    System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }
    }

    private static void showAllUsers() {
        System.out.println("\nLista użytkowników w systemie:");

        if (users.isEmpty()) {
            System.out.println("Brak użytkowników.");
        } else {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                System.out.println("E-mail: " + entry.getKey());
            }
        }
    }

    private static void logout() {
        if (isLoggedIn) {
            isLoggedIn = false;
            System.out.println("Wylogowano pomyślnie.");
        } else if (isBankStaffLoggedIn) {
            isBankStaffLoggedIn = false;
            System.out.println("Wylogowano obsługę banku.");
        } else {
            System.out.println("Nie jesteś zalogowany.");
        }
    }

    private static void logoutAsBankStaff() {
        isBankStaffLoggedIn = false;
        System.out.println("Wylogowano obsługę banku. Powrót do menu początkowego.");
    }

    private static void makeTransfer(Scanner scanner) {
        if (!isLoggedIn) {
            System.out.println("Musisz być zalogowany, aby wykonać przelew.");
            return;
        }

        System.out.print("Wprowadź numer konta odbiorcy: ");
        String recipientAccountNumber = scanner.nextLine();

        if (!recipientAccountNumber.matches("\\d{26}")) {
            System.out.println("Numer konta musi zawierać 26 cyfr.");
            return;
        }

        System.out.print("Wprowadź kwotę do przelania: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Wprowadź walutę przelewu (USD, EUR, CHF, GBP): ");
        String currency = scanner.nextLine();

        List<Account> accounts = userAccounts.get(loggedInUser);
        Account userAccount = null;
        for (Account account : accounts) {
            if (account.getCurrency().equals(currency)) {
                userAccount = account;
                break;
            }
        }

        if (userAccount == null || userAccount.getBalance() < amount) {
            System.out.println("Brak wystarczających środków na koncie.");
            return;
        }

        double convertedAmount = amount * currencyRates.get(currency);
        userAccount.withdraw(amount);
        System.out.println("Przelew wykonany pomyślnie. Przesłano " + convertedAmount + " " + currency + " na konto odbiorcy.");
    }

    private static void atmServices(Scanner scanner) {
        if (!isLoggedIn) {
            System.out.println("Musisz być zalogowany, aby skorzystać z bankomatu.");
            return;
        }

        System.out.print("Wprowadź numer karty: ");
        String cardNumber = scanner.nextLine();

        if (!cardPins.containsKey(cardNumber)) {
            System.out.println("Nieprawidłowy numer karty.");
            return;
        }

        System.out.print("Wprowadź PIN: ");
        String pin = scanner.nextLine();

        if (!cardPins.get(cardNumber).equals(pin)) {
            System.out.println("Błędny PIN.");
            return;
        }

        System.out.println("Dostępne usługi bankomatu:");
        System.out.println("1. Wpłać środki");
        System.out.println("2. Wypłać środki");
        System.out.println("3. Zmień PIN");
        System.out.println("4. Wysuń kartę");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                depositFunds(scanner, cardNumber);
                break;
            case 2:
                withdrawFunds(scanner, cardNumber);
                break;
            case 3:
                changePin(scanner, cardNumber);
                break;
            case 4:
                System.out.println("Wysuwanie karty. Zakończenie usługi bankomatu.");
                return;
            default:
                System.out.println("Nieprawidłowy wybór.");
        }
    }

    private static void depositFunds(Scanner scanner, String cardNumber) {
        System.out.print("Wprowadź kwotę do wpłaty: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        List<Account> accounts = userAccounts.get(loggedInUser);
        Account userAccount = accounts.get(0);
        userAccount.deposit(amount);
        System.out.println("Środki zostały pomyślnie wpłacone na konto.");
    }

    private static void withdrawFunds(Scanner scanner, String cardNumber) {
        System.out.print("Wprowadź kwotę do wypłaty: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        List<Account> accounts = userAccounts.get(loggedInUser);
        Account userAccount = accounts.get(0);
        if (userAccount.getBalance() >= amount) {
            userAccount.withdraw(amount);
            System.out.println("Środki zostały pomyślnie wypłacone.");
        } else {
            System.out.println("Brak wystarczających środków na koncie.");
        }
    }

    private static void changePin(Scanner scanner, String cardNumber) {
        System.out.print("Wprowadź nowy PIN: ");
        String newPin = scanner.nextLine();
        cardPins.put(cardNumber, newPin);
        System.out.println("PIN został pomyślnie zmieniony.");
    }

    private static void manageAccount(Scanner scanner) {
        if (!isLoggedIn) {
            System.out.println("Musisz być zalogowany, aby zarządzać swoim kontem.");
            return;
        }

        System.out.println("Zarządzaj kontem:");
        System.out.println("1. Zmień dane osobowe");
        System.out.println("2. Zmień hasło");
        System.out.println("3. Sprawdź stan konta");
        System.out.println("4. Powrót do menu");

        System.out.print("Wybierz opcję: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                changePersonalData(scanner);
                break;
            case 2:
                changePassword(scanner);
                break;
            case 3:
                checkAccountBalance();
                break;
            case 4:
                return;
            default:
                System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
        }
    }

    private static void checkAccountBalance() {
        List<Account> accounts = userAccounts.get(loggedInUser);
        if (accounts != null && !accounts.isEmpty()) {
            Account account = accounts.get(0);
            System.out.println("Stan konta: " + account.getBalance());
        }
    }

    private static void changePersonalData(Scanner scanner) {
        System.out.print("Wprowadź nowe imię: ");
        String firstName = scanner.nextLine();

        System.out.print("Wprowadź nowe nazwisko: ");
        String lastName = scanner.nextLine();

        System.out.println("Twoje dane zostały zaktualizowane.");
    }

    private static void changePassword(Scanner scanner) {
        System.out.print("Wprowadź nowe hasło: ");
        String newPassword = scanner.nextLine();

        if (isValidPassword(newPassword)) {
            users.put(loggedInUser, newPassword);
            System.out.println("Hasło zostało zmienione.");
        } else {
            System.out.println("Hasło musi mieć co najmniej 8 znaków, zawierać dużą literę, małą literę i cyfrę.");
        }
    }

    static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*\\d.*");
    }

    private static String generateAccountNumber() {
        return String.format("%026d", Math.abs(new Random().nextLong()));
    }

    private static String generateCardNumber() {
        return String.format("%016d", Math.abs(new Random().nextLong()));
    }

    private static String generatePIN() {
        return String.format("%04d", Math.abs(new Random().nextInt(10000)));
    }

    static class Account {
        private String accountNumber;
        private String currency;
        private double balance;

        public Account(String accountNumber, String currency, double balance) {
            this.accountNumber = accountNumber;
            this.currency = currency;
            this.balance = balance;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getCurrency() {
            return currency;
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            balance += amount;
        }

        public void withdraw(double amount) {
            balance -= amount;
        }
    }
}
