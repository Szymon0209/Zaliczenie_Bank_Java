public class Main {
    public static void main(String[] args) {
        BankingSystem bankingSystem = new BankingSystem();

        Account account1 = new Account("Jan", "Kowalski", "ul. Przykładowa 1, Warszawa",
                                      "1990-01-01", "90010112345", 
                                      "jan.kowalski", "haslo123", "jan@example.com");
        Account account2 = new Account("Anna", "Nowak", "ul. Przykładowa 2, Kraków",
                                      "1985-05-15", "85051567890", 
                                      "anna.nowak", "haslo456", "anna@example.com");

        bankingSystem.registerAccount(account1);
        bankingSystem.registerAccount(account2);

        if (bankingSystem.login("jan.kowalski", "haslo123")) {
            account1.setBalance(1000.0);
            bankingSystem.initiateTransfer(account2.getAccountNumber(), 300.0);
            bankingSystem.logout();
        }

        bankingSystem.listAllTransfers();
        bankingSystem.verifyTransfer(0, true);

        bankingSystem.listUsers();
        bankingSystem.blockAccount(account1.getAccountNumber());
        bankingSystem.listUsers();
    }
}