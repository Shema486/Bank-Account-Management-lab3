package services;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import modules.Transaction;

/**
 * Service class to manage transactions in the banking system.
 * Provides functionality to add, retrieve, view, calculate totals,
 * and persist transactions to a file.
 * <p>
 * Thread-safe for adding transactions using synchronized methods.
 * </p>
 */
public class TransactionManager  {

    /** List to hold all transactions in memory. */
    private List<Transaction> transactions;

    /**
     * Constructs a new TransactionManager with an empty transaction list.
     */
    public TransactionManager() {
        this.transactions = new ArrayList<>();
    }

    /**
     * Adds a new transaction to the manager in a thread-safe manner.
     *
     * @param newTransaction the Transaction object to be added
     */
    public synchronized void addTransaction(Transaction newTransaction) {
        this.transactions.add(newTransaction);
    }

    /**
     * Calculates the total amount for a specific transaction type
     * (DEPOSIT or WITHDRAW) for a given account number.
     *
     * @param accountNumber the account number to filter transactions
     * @param requiredType  the type of transaction ("DEPOSIT" or "WITHDRAW")
     * @return the total sum of amounts for the filtered transactions
     */
    private double calculateTotal(String accountNumber, String requiredType) {
        return transactions.stream()
                .filter(t ->
                        t.getAccountNumber().equalsIgnoreCase(accountNumber) &&
                                t.getType().equalsIgnoreCase(requiredType)
                )
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Calculates the total amount withdrawn for a given account.
     *
     * @param accountNumber the account number
     * @return the total withdrawals for the account
     */
    public double calculateTotalWithdraw(String accountNumber) {
        return calculateTotal(accountNumber, "WITHDRAW");
    }

    /**
     * Calculates the total amount deposited for a given account.
     *
     * @param accountNumber the account number
     * @return the total deposits for the account
     */
    public double calculateTotalDeposit(String accountNumber) {
        return calculateTotal(accountNumber, "DEPOSIT");
    }

    /**
     * Retrieves a list of transactions filtered by account number.
     *
     * @param accountNumber the account number to filter
     * @return a list of transactions for the specified account
     */
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equalsIgnoreCase(accountNumber))
                .toList();
    }

    /**
     * Displays transaction history for a given account in a formatted table.
     * Also prints total deposits, withdrawals, and net change.
     *
     * @param accountNumber the account number
     */
    public void viewTransactionsByAccount(String accountNumber) {
        List<Transaction> history = getTransactionsByAccount(accountNumber);

        System.out.println("\n--- TRANSACTION HISTORY FOR " + accountNumber + " ---");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.printf("| %-7s | %-19s | %-12s | %-10s | %-10s | %-10s |\n",
                "ID", "Timestamp", "Account", "Type", "Amount", "Balance After");
        System.out.println("------------------------------------------------------------------------------------");

        if (history.isEmpty()) {
            System.out.println("| NO TRANSACTIONS FOUND for account " + accountNumber + ".");
        } else {
            for (int i = history.size() - 1; i >= 0; i--) {
                history.get(i).displayTransactionDetails();
            }
        }

        System.out.println("------------------------------------------------------------------------------------");

        double totalDeposits = calculateTotalDeposit(accountNumber);
        double totalWithdrawals = calculateTotalWithdraw(accountNumber);
        double netChange = totalDeposits - totalWithdrawals;

        System.out.printf("SUMMARY: Total Deposits: $%,.2f | Total Withdrawals: $%,.2f | Net Change: %s$%,.2f\n",
                totalDeposits, totalWithdrawals, (netChange >= 0 ? "+" : ""), netChange);
        System.out.println("------------------------------------------------------------------------------------");
    }

    /**
     * Returns the total number of transactions managed.
     *
     * @return the count of transactions
     */
    public int getTransactionCount() {
        return this.transactions.size();
    }

    /**
     * Saves all transactions to a file in plain text format.
     * Creates necessary directories if they do not exist.
     */
    public void saveTransaction() {
        String filePath = "src/main/java/data/transactions.txt";
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        try {
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    System.out.println("Failed to create directory structure: " + parentDir.getAbsolutePath());
                    return;
                }
            }
            List<String> lines = transactions.stream()
                    .map(Transaction::toString)
                    .toList();
            Files.write(Paths.get(filePath), lines);
            System.out.println("Transactions saved.");
        } catch (Exception e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    /**
     * Loads transactions from a file and populates the internal list.
     * Existing transactions are replaced by the loaded transactions.
     */
    public void loadTransaction() {
        try {
            String filePath = "src/main/java/data/transactions.txt";
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) return;

            transactions = new ArrayList<>(
                    Files.readAllLines(path).stream()
                            .map(Transaction::fromString)
                            .toList()
            );

            System.out.println("Transactions loaded.");
        } catch (Exception e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

}
