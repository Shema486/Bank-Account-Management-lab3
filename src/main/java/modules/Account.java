package modules;

import exceptions.InsufficientFundException;
import interfaces.Depositable;
import interfaces.Transactable;
import interfaces.Withdrawable;

import java.io.Serializable;

public abstract class Account implements Transactable, Depositable, Withdrawable, Serializable {
    private static final long serialVersionUID = 1L;

    private static int accountCounter = 0;

    private String accountNumber;
    private Customer customer;
    protected double balance;
    private String status;

    // Constructor for NEW accounts
    public Account(Customer customer, double initialBalance) {
        accountCounter++;
        this.accountNumber = String.format("ACC%03d", accountCounter);
        this.customer = customer;
        this.balance = initialBalance;
        this.status = "ACTIVE";
    }

    // Constructor for loading from file
    protected Account(String accNo, Customer c, double balance, String status) {
        this.accountNumber = accNo;
        this.customer = c;
        this.balance = balance;
        this.status = status;
    }

    //getter
    public String getStatus() { return status; }
    public double getBalance() { return balance; }
    public Customer getCustomer() { return customer; }
    public String getAccountNumber() { return accountNumber; }

    //abstracts
    public abstract void displayAccountDetails();
    public abstract String getAccountType();
    public abstract boolean withdraw(double amount);

    public synchronized boolean deposit(double amount) {
        if (amount <= 0) return false;
        this.balance += amount;
        return true;
    }

    @Override
    public boolean processTransaction(double amount, String type) {
        try {
            if (type.equalsIgnoreCase("DEPOSIT")) return deposit(amount);
            else if (type.equalsIgnoreCase("WITHDRAW")) return withdraw(amount);
            else return false;
        } catch (InsufficientFundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        return accountNumber + "," +
                customer.getName() + "," +
                balance + "," +
                getClass().getSimpleName() + "," +
                status;
    }

    public static Account fromString(String line) {
        String[] p = line.split(",");
        if (p.length < 5) return null;

        String accNo = p[0];
        String customerName = p[1];
        double balance = Double.parseDouble(p[2]);
        String type = p[3];
        String status = p[4];

        Customer c = new RegularCustomer(customerName); // Adjust if you store more customer info

        switch (type) {
            case "SavingsAccount" -> {
                double rate = Double.parseDouble(p[5]);
                double minBalance = Double.parseDouble(p[6]);
                return new SavingsAccount(accNo, c, balance, rate, minBalance, status);
            }
            case "CheckingAccount" -> {
                double overdraft = Double.parseDouble(p[5]);
                double fee = Double.parseDouble(p[6]);
                return new CheckingAccount(accNo, c, balance, overdraft, fee, status);
            }
            default -> {
                System.err.println("Unknown account type: " + type);
                return null;
            }
        }
    }
}
