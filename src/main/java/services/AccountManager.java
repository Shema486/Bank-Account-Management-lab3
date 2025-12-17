package services;

import modules.Account;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class to manage bank accounts.
 * Provides functionality to add, find, view, calculate totals,
 * and persist accounts to a file.
 */
public class AccountManager {

    /** List that stores all Account objects in memory. */
    private List<Account> accounts;

    /**
     * Constructs a new AccountManager with an empty account list.
     */
    public AccountManager() {
        this.accounts = new ArrayList<>();
    }

    /**
     * Adds a new account to the manager.
     *
     * @param newAccount the Account object to be added
     */
    public void addAccount(Account newAccount) {
        this.accounts.add(newAccount);
    }

    /**
     * Finds an account by its account number.
     *
     * @param accountNumber the account number to search for
     * @return the Account object if found, otherwise null
     */
    public Account findAccount(String accountNumber) {
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equalsIgnoreCase(accountNumber))
                .findFirst()
                .orElse(null);
    }

    /**
     * Calculates the total balance of all accounts.
     *
     * @return the sum of balances of all accounts
     */
    public double getTotalBalance() {
        return accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    /**
     * Displays all accounts in a formatted manner.
     * Also prints the total number of accounts and total bank balance.
     * Uses Stream API to iterate through accounts.
     */
    public void viewAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts in the bank.");
            return;
        }

        System.out.println("----- All Bank Accounts -----");

        accounts.stream()
                .filter(account -> account != null)
                .forEach(Account::displayAccountDetails);

        System.out.println("Total accounts: " + getTotalAccounts());
        System.out.println("Total Bank balance: $" + getTotalBalance());
    }

    /**
     * Returns the total number of accounts managed.
     *
     * @return the count of accounts
     */
    public int getTotalAccounts() {
        return accounts.size();
    }

    /**
     * Saves all accounts to a text file.
     * Creates necessary directories if they do not exist.
     */
    public void saveAccounts() {
        String filePath = "src/main/java/data/accounts.txt";
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        try {
            if (parentDir != null && !parentDir.exists()) {
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

    /**
     * Loads accounts from a text file and populates the internal list.
     * Existing accounts are replaced by the loaded accounts.
     * Filters out null accounts during loading.
     */
    public void loadAccounts() {
        try {
            String filePath = "src/main/java/data/accounts.txt";
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
