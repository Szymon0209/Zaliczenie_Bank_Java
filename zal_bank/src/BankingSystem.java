import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankingSystem {
    private Map<String, Account> accounts = new HashMap<>();
    private List<Transfer> pendingTransfers = new ArrayList<>();
    private List<Account> blockedAccounts = new ArrayList<>();
    private Account loggedInAccount;

    public void registerAccount(Account account) {
        accounts.put(account.getLogin(), account);
    }

    public boolean login(String login, String password) {
        Account account = accounts.get(login);
        if (account != null && account.getPassword().equals(password)) {
            loggedInAccount = account;
            return true;
        }
        return false;
    }

    public void logout() {
        loggedInAccount = null;
    }

    public void initiateTransfer(String recipientAccountNumber, double amount) {
        if (loggedInAccount == null) {
            System.out.println("Musisz być zalogowany, aby zainicjować transfer.");
            return;
        }

        Transfer transfer = new Transfer(loggedInAccount.getAccountNumber(), recipientAccountNumber, amount);
        pendingTransfers.add(transfer);
        System.out.println("Transfer zainicjowany. Oczekuje na weryfikację.");
    }

    public void verifyTransfer(int transferIndex, boolean approve) {
        if (transferIndex < 0 || transferIndex >= pendingTransfers.size()) {
            System.out.println("Nieprawidłowy indeks transferu.");
            return;
        }

        Transfer transfer = pendingTransfers.get(transferIndex);
        if (approve) {
            Account sender = findAccountByNumber(transfer.getSenderAccountNumber());
            Account recipient = findAccountByNumber(transfer.getRecipientAccountNumber());

            if (sender.getBalance() >= transfer.getAmount()) {
                sender.setBalance(sender.getBalance() - transfer.getAmount());
                recipient.setBalance(recipient.getBalance() + transfer.getAmount());
                transfer.setVerified(true);
                System.out.println("Transfer zatwierdzony i wykonany.");
            } else {
                System.out.println("Brak wystarczających środków u nadawcy.");
            }
        } else {
            System.out.println("Transfer odrzucony.");
        }

        pendingTransfers.remove(transferIndex);
    }

    public void listUsers() {
        System.out.println("Lista użytkowników:");
        for (Account account : accounts.values()) {
            System.out.println("Login: " + account.getLogin() + ", Imię: " + account.getFirstName() +
                    ", Nazwisko: " + account.getLastName());
        }
    }

    public void listAllTransfers() {
        System.out.println("Lista wszystkich przelewów:");
        for (Transfer transfer : pendingTransfers) {
            System.out.println("Od: " + transfer.getSenderAccountNumber() +
                    ", Do: " + transfer.getRecipientAccountNumber() +
                    ", Kwota: " + transfer.getAmount() +
                    ", Zatwierdzony: " + transfer.isVerified());
        }
    }

    public void blockAccount(String accountNumber) {
        Account accountToBlock = findAccountByNumber(accountNumber);
        if (accountToBlock != null) {
            blockedAccounts.add(accountToBlock);
            accounts.remove(accountToBlock.getLogin());
            System.out.println("Konto o numerze " + accountNumber + " zostało zablokowane.");
        } else {
            System.out.println("Nie znaleziono konta o podanym numerze.");
        }
    }

    private Account findAccountByNumber(String accountNumber) {
        for (Account account : accounts.values()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
}