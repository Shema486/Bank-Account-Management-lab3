package services;

import modules.Account;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Assuming 'modules.Account' and 'customers.RegularCustomer' exist,
// though RegularCustomer is not used here.

public class AccountManager  {

    // Composition: Holds a List of Account objects (Good use of abstraction!)
    private List<Account> accounts;

    public AccountManager() {
        this.accounts = new ArrayList<>();
    }
    public void addAccount(Account newAccount) {
        this.accounts.add(newAccount);
    }

    public Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                return account;
            }
        }
        return null;
    }
    public double getTotalBalance() {
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    public void viewAllAccounts() {

        if (accounts.size() == 0) {
            System.out.println("No accounts in the bank.");
            return;
        }

        System.out.println("----- All Bank Accounts -----");

        for (Account account : accounts) {
            if (account != null) {
                account.displayAccountDetails();
            }
        }
        System.out.println("Total accounts: " + getTotalAccounts());
        System.out.println("Total Bank balance: $" + getTotalBalance());
    }

    // Getter
    public int getTotalAccounts() {
        return accounts.size();
    }
    public void saveAccounts() {
        String filePath = "src/main/java/data/accounts.txt";

        // 1. Define the File object and its Parent Directory
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        try {

            if (parentDir != null && !parentDir.exists()) {
                // mkdirs() creates the directory and any necessary parent directories
                if (!parentDir.mkdirs()) {
                    System.out.println("Failed to create directory structure: " + parentDir.getAbsolutePath());
                    return;
                }
            }
            List<String> lines = accounts.stream()
                    .map(Account::toString)
                    .toList();

            Files.write(Paths.get(filePath), lines);
            System.out.println("Accounts saved (text format).");
        } catch (Exception e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    public void loadAccounts() {
        try {
            // Example in AccountManager or TransactionManager
            String filePath = "src/main/java/data/accounts.txt"; // Might work during development
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) return;

            accounts = new ArrayList<>(
                    Files.readAllLines(path)
                            .stream()
                            .map(Account::fromString)
                            .filter(account -> account != null)
                            .toList()
            );


            System.out.println("Accounts loaded (text format).");
        } catch (Exception e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }


}