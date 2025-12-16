package services;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
// Import the Transaction class correctly based on your package structure
import modules.Transaction;

public class TransactionManager  {
    //  Depend on the List abstraction, not the concrete array/ArrayList.
    // This is the High-Level Module depending on Abstraction.
    private  List<Transaction> transactions;

    public TransactionManager() {
        // Initialize with a concrete implementation (ArrayList)
        this.transactions = new ArrayList<>();
    }

    // SRP: Responsibility is purely adding the transaction object.
    // Array size checking is now handled automatically by the List.
    public synchronized void addTransaction(Transaction newTransaction) {
        this.transactions.add(newTransaction);
    }

    // --- Methods for Reporting and Testing ---
    private double calculateTotal(String accountNumber, String requiredType) {
        double total = 0.0;
        for (Transaction t : this.transactions) {
            // Robust check: Ignore case for type and account number.
            if (t.getAccountNumber().equalsIgnoreCase(accountNumber) &&
                    t.getType().equalsIgnoreCase(requiredType)) {
                total += t.getAmount();
            }
        }
        return total;
    }

    // Public methods for external access
    // These methods now call the single private helper function.
    public double calculateTotalWithdraw(String accountNumber) {
        // Renamed to be consistent with the test class, ensures correctness
        return calculateTotal(accountNumber, "WITHDRAW");
    }

    public double calculateTotalDeposit(String accountNumber) {
        // Renamed to be consistent with the test class
        return calculateTotal(accountNumber, "DEPOSIT");
    }

    // --- Data Retrieval (Better for SRP than printing) ---
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        List<Transaction> accountHistory = new ArrayList<>();
        // Iterate through all transactions and collect only the relevant ones
        for (Transaction t : this.transactions) {
            if (t.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                accountHistory.add(t);
            }
        }
        // Return the data; the calling class (e.g., Main) handles the output.
        return accountHistory;
    }


    // Method for US-4 (View Transaction History)
    public void viewTransactionsByAccount(String accountNumber) {
        // 1. SRP: Get the filtered data first.
        List<Transaction> history = getTransactionsByAccount(accountNumber);

        System.out.println("\n--- TRANSACTION HISTORY FOR " + accountNumber + " ---");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.printf("| %-7s | %-19s | %-12s | %-10s | %-10s | %-10s |\n",
                "ID", "Timestamp", "Account", "Type", "Amount", "Balance After");
        System.out.println("------------------------------------------------------------------------------------");

        if (history.isEmpty()) {
            System.out.println("| NO TRANSACTIONS FOUND for account " + accountNumber + ".");
        } else {
            // 2. DSA & Efficiency: Iterate over the filtered 'history' List,
            //    going backward to show newest transactions first.
            for (int i = history.size() - 1; i >= 0; i--) {
                history.get(i).displayTransactionDetails();
            }
        }

        System.out.println("------------------------------------------------------------------------------------");

        // 3. Display Summary (Uses dedicated, testable helper methods)
        double totalDeposits = calculateTotalDeposit(accountNumber);
        double totalWithdrawals = calculateTotalWithdraw(accountNumber);
        double netChange = totalDeposits - totalWithdrawals;

        System.out.printf("SUMMARY: Total Deposits: $%,.2f | Total Withdrawals: $%,.2f | Net Change: %s$%,.2f\n",
                totalDeposits, totalWithdrawals, (netChange >= 0 ? "+" : ""), netChange);
        System.out.println("------------------------------------------------------------------------------------");
    }

    public int getTransactionCount() {
        return this.transactions.size();}
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
    public void loadTransaction() {
        try {
            String filePath = "src/main/java/data/transactions.txt";
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) return;

            transactions = new ArrayList<>(
                    Files.readAllLines(path).stream()
                    .map(Transaction::fromString)
                    .toList());

            System.out.println("Transactions loaded.");
        } catch (Exception e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

}