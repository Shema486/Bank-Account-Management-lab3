package utils;

import exceptions.InvalidAmountException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ValidationUtils {
    static  Scanner scanner = new Scanner(System.in);
    public  void enterToContinue(){
        System.out.println("\npress enter to continue...\n");
        scanner.nextLine();
    }
    //input for integer
    public  int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine();

                if (value < min || value > max) {
                    throw new InvalidAmountException("Value must be between " + min + " and " + max + ".");
                } else {
                    return value;  // valid
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter a whole number.");
                scanner.nextLine();
            }catch (InvalidAmountException e){
                System.out.println(e.getMessage());
            }
        }
    }
    // input for double
    public double getDoubleInput(String prompt, double min) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine();

                if (value < min) {
                    throw new InvalidAmountException(min);
                } else {
                    return value; // valid
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter a number.");
                scanner.nextLine();
            }catch (InvalidAmountException e){
                System.out.println(e.getMessage());
            }
        }
    }
    // input for String
    public String getStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("This field cannot be empty. Try again.");
            } else {
                return input;
            }
        }
    }
}
