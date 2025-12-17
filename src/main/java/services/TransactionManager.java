package services;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import modules.Transaction;

public class TransactionManager  {
    private  List<Transaction> transactions;

    /**
     * ADD transaction
     */
    public TransactionManager() {this.transactions = new ArrayList<>();}


    /**
     * this is the synchronized method
     * @param newTransaction
     */
    public synchronized void addTransaction(Transaction newTransaction) {
        this.transactions.add(newTransaction);}

    /**
     * @param accountNumber
     * @param requiredType
     * @return method calculate total
     */
    // --- Methods for Reporting and Testing ---
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
     * @param accountNumber
     * @return double totalWithdraw
     */
    public double calculateTotalWithdraw(String accountNumber) {
        return calculateTotal(accountNumber, "WITHDRAW");
    }

    /**
     * @param accountNumber
     * @return double  totalDeposit
     */
    public double calculateTotalDeposit(String accountNumber) {
        return calculateTotal(accountNumber, "DEPOSIT");
    }


    /**
     * @param accountNumber
     * @return Object transaction
     */
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equalsIgnoreCase(accountNumber))
                .toList();
    }
    /**
     * @param accountNumber
     *
     */
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
     * @return integer number of transactions have been generated
     */
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