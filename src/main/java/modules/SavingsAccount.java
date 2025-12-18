package modules;

import exceptions.InsufficientFundException;

import java.io.Serializable;

public class SavingsAccount extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private double interestRate;
    private double minimumBalance;

    // Constructor for NEW accounts
    public SavingsAccount(Customer customer, double balance) {
        super(customer, balance);
        // GOOD: Set rates ONCE during creation
        if (customer instanceof PremiumCustomer) {
            this.interestRate = 5.0;
            this.minimumBalance = 100;
        } else {
            this.interestRate = 3.5;
            this.minimumBalance = 500;
        }
    }

    // Constructor for loading from files
    public SavingsAccount(String accNo, Customer c, double balance,
                          double rate, double minBalance, String status) {
        super(accNo, c, balance, status);
        this.interestRate = rate;
        this.minimumBalance = minBalance;
    }

    @Override
    public void displayAccountDetails() {
        System.out.println("ACC NO: " + getAccountNumber() +
                " | CUSTOMER: " + getCustomer().getName() +
                " | TYPE: " + getAccountType() +
                " | BALANCE: " + getBalance() +
                " | STATUS: " + getStatus());
        System.out.println("    | Interest Rate: " + interestRate +
                "% | Minimum Balance: $" + minimumBalance +
                " | Interest Earned: $" + calculateInterestEarned());
    }

    @Override
    public String getAccountType() {
        return "Saving";
    }

    @Override
    public synchronized boolean withdraw(double amount) {
        try {
            if (balance - amount >= minimumBalance) {
                balance -= amount;
                return true;
            } else {
                System.out.println("Insufficient Fund please try again");
                return false;
            }

        } catch (InsufficientFundException e) {
            System.out.println("Insufficient Fund please try again" + e.getMessage());
            return false;
        }

    }

    public double calculateInterestEarned() {
        return this.balance * interestRate / 100;
    }

    @Override
    public String toString() {
        return super.toString() + "," + interestRate + "," + minimumBalance;
    }
}
