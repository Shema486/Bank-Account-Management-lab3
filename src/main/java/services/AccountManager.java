package services;

import modules.Account;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Service class to manage bank accounts using HashMap.
 * Demonstrates use of Streams for totals, listing, and filtering.
 */
public class AccountManager {

    /** Stores accounts by account number for fast lookup */
    private Map<String, Account> accountsMap;

    /** Constructor */
    public AccountManager() {
        accountsMap = new HashMap<>();
    }

    /**
     * Adds a new account to the manager.
     * If the account number already exists, it will be overwritten.
     *
     * @param newAccount the Account object to be added
     */
    public void addAccount(Account newAccount) {
        accountsMap.put(newAccount.getAccountNumber(), newAccount);
    }

    /**
     * Finds an account by its account number.
     *
     * @param accountNumber the account number to search for
     * @return the Account object if found, otherwise null
     */
    public Account findAccount(String accountNumber) {
        return accountsMap.get(accountNumber);
    }

    /**
     * Calculates the total balance of all accounts using Stream API.
     *
     * @return the sum of balances of all accounts
     */
    public double getTotalBalance() {
        return accountsMap.values()
                .stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    /**
     * Displays all accounts in a formatted manner using Stream API.
     * Also prints the total number of accounts and total bank balance.
     */
    public void viewAllAccounts() {
        if (accountsMap.isEmpty()) {
            System.out.println("No accounts in the bank.");
            return;
        }

        System.out.println("----- All Bank Accounts -----");

        // Stream to display all accounts
        accountsMap.values()
                .stream()
                .filter(Objects::nonNull)
                .forEach(Account::displayAccountDetails);

        System.out.println("Total accounts: " + getTotalAccounts());
        System.out.println("Total Bank balance: $" + getTotalBalance());
    }

    /** Returns the total number of accounts managed. */
    public int getTotalAccounts() {
        return accountsMap.size();
    }

    /**
     * Saves all accounts to a text file using Stream API.
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

            List<String> lines = accountsMap.values()
                    .stream()
                    .map(Account::toString)
                    .toList();

            Files.write(Paths.get(filePath), lines);
            System.out.println("Accounts saved (text format).");
        } catch (Exception e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    /**
     * Loads accounts from a text file and populates the internal map using simple logic.
     */
    public void loadAccounts() {
        try {
            String filePath = "src/main/java/data/accounts.txt";
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) return;

            accountsMap.clear();

            Files.readAllLines(path)
                    .stream()
                    .map(Account::fromString)
                    .filter(Objects::nonNull)
                    .forEach(account -> accountsMap.put(account.getAccountNumber(), account));

            System.out.println("Accounts loaded (text format).");
        } catch (Exception e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }
}
