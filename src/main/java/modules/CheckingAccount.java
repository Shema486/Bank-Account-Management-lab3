package modules;

import exceptions.OverdraftExceededException;

import java.io.Serializable;

public class CheckingAccount extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private double overdraftLimit;
    private double monthlyFee;

    // Constructor for NEW account
    public CheckingAccount(Customer customer, double balance) {
        super(customer, balance);
        this.overdraftLimit = 1000;
        this.monthlyFee = 10;
    }

    // Constructor for loading from file
    public CheckingAccount(String accNo, Customer c, double balance,
                           double overdraft, double fee, String status) {
        super(accNo, c, balance, status);
        this.overdraftLimit = overdraft;
        this.monthlyFee = fee;
    }

    @Override
    public void displayAccountDetails() {
        System.out.println("ACC NO: " + getAccountNumber() +
                " | CUSTOMER: " + getCustomer().getName() +
                " | TYPE: " + getAccountType() +
                " | BALANCE: " + getBalance() +
                " | STATUS: " + getStatus());
        System.out.println("    | Overdraft Limit: $" + overdraftLimit +
                " | Monthly Fee: $" + monthlyFee +
                " | Balance after Fee: $" + applyMonthlyFee());
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }

    @Override
    public synchronized boolean withdraw(double amount) throws OverdraftExceededException {
        double newBalance = balance - amount;
        if (newBalance < -overdraftLimit)
            throw new OverdraftExceededException(
                    "Withdrawal denied! Overdraft limit $" + overdraftLimit + " exceeded."
            );
        balance = newBalance;
        return true;
    }

    public double applyMonthlyFee() {
        balance -= monthlyFee;
        return balance;
    }

    @Override
    public String toString() {
        return super.toString() + "," + overdraftLimit + "," + monthlyFee;
    }
}
