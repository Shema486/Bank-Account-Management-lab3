package modules;
import java.io.Serializable;

public class RegularCustomer extends Customer implements Serializable {
    private static final long serialVersionUID = 1L;


    // Constructor (simply calls the abstract class constructor)
    public RegularCustomer(String name, int age, String contact, String address) {
        super(name, age, contact, address);
    }
    public  RegularCustomer(String name){
        super(name);
    }

    // Override the abstract method to display customer details
    @Override
    public void displayCustomerDetails() {
        System.out.println("CustomerId: "+getCustomerId());
        System.out.println("Name: "+getName());
        System.out.println("Age: "+getAge());
        System.out.println("Contact:" +getContact());
        System.out.println("Address: "+getAddress());
        System.out.println("Type: "+getCustomerType());
    }


    // Override the abstract method to specify the type
    @Override
    public String getCustomerType() {
        return "Regular";
    }
}
