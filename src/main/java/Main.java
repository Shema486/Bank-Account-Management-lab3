import modules.*;
import utils.ConsoleMenu;
import utils.Functions;



import java.util.InputMismatchException;
import java.util.Scanner;




public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ConsoleMenu consoleMenu = new ConsoleMenu();
    static Functions functions = new Functions();

    public static void main(String[] args) {

        functions.initializeData();
        functions.handleLoadData();

        int choice = 0;
        do {
            ConsoleMenu.displayMenu();
            try {
                System.out.print("Enter your choice (1-6): ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        consoleMenu.managerAccount();

                        break;
                    case 2:
                        consoleMenu.performTransaction();
                        break;
                    case 3:
                        functions.handleViewHistory();

                        break;
                    case 4:
                        consoleMenu.handleSaveLoad();
                        break;
                    case 5:
                        functions.runConcurrentSimulation();
                        break;
                    case 6:
                        System.out.println("\nThank you for using the Bank Account Management System. Goodbye!");
                        break;
                    default:
                        // Input Validation (US-5 Acceptance Criteria)
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                        break;
                }
            } catch (InputMismatchException e) {
                // Input Validation for non-integer inputs (US-5 Acceptance Criteria)
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
                choice = 0;
            }
        } while (choice != 6);

        scanner.close();
    }
}