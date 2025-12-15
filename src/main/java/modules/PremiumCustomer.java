package modules;

import java.io.Serializable;

public class PremiumCustomer extends Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    // Private field specific to Premium customers [cite: 378]
    private  final double minimumBalance ; // Minimum to maintain premium status

    // Constructor [cite: 378]
    public PremiumCustomer(String name, int age, String contact, String address) {
        super(name, age, contact, address);
        this.minimumBalance = 10000;
    }

    // Special method for benefits (waived fees) [cite: 381]
    public boolean hasWaivedFees() {
        // Premium customers always have waived monthly fees
        return true;
    }

    // Override the abstract method to specify the type [cite: 380]
    @Override
    public String getCustomerType() {
        return "Premium";
    }

    // Override to show customer info plus premium benefits [cite: 380]
    @Override
    public void displayCustomerDetails() {
        System.out.println("Customer ID: " + getCustomerId());
        System.out.println("Name: " + getName());
        System.out.println("Age: " + getAge());
        System.out.println("Contact: " + getContact());
        System.out.println("Type: " + getCustomerType() + " (Priority Service)");
        System.out.println("CustomerType :"+getCustomerType());
        System.out.println("Benefit: Monthly fees waived.");
        System.out.println("Min Balance Requirement: $" + String.format("%,.2f", getMinimumBalance()));
    }

    // Getter for minimumBalance [cite: 379]
    public double getMinimumBalance() {
        return minimumBalance;
    }

}