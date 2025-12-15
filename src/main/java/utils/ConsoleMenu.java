package utils;

import java.util.InputMismatchException;

import static utils.Functions.accountManager;
import static utils.ValidationUtils.scanner;

public class ConsoleMenu {
    static ValidationUtils validationUtils = new ValidationUtils();

    static Functions functions  = new Functions();
    public static void displayMenu() {
        System.out.println("\n=============================================");
        System.out.println("      BANK ACCOUNT MANAGEMENT SYSTEM MENU    ");
        System.out.println("=============================================");
        System.out.println("1. Manage Account ");
        System.out.println("2. Perform transactions ");
        System.out.println("3. Generate account statements ");
        System.out.println("4. Save/load");
        System.out.println("5. Run concurrency");
        System.out.println("6. Exit ");
        System.out.println("=============================================");

    }
    public  void managerAccount(){
//        functions.initializeData();
        int choice =0;
        do {
            System.out.println("Account Management_____________");
            System.out.println("1. Create New Account ");
            System.out.println("2. view all accounts ");
            System.out.println("3. View Transaction History");
            System.out.println("4. back to main");
            try {
                choice = validationUtils.getIntInput("select from (1-4): ",1,4);

                switch (choice){
                    case 1:
                        functions.handleCreateAccount();
                        break;
                    case 2:
                        accountManager.viewAllAccounts();
                        accountManager.getTotalBalance();
                        break;
                    case 3:
                        functions.handleViewHistory();
                    case 4:

                        break;
                    default:
                        // Input Validation (US-5 Acceptance Criteria)
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                        break;
                }
            }catch (InputMismatchException e){
                // Input Validation for non-integer inputs (US-5 Acceptance Criteria)
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();

            }


        }while (choice !=4);
    }
    public void performTransaction(){

        int choice =0;
        do {
            System.out.println("Perform transactions____________________");
            System.out.println("1. Process Transaction (Deposit/Withdraw)");
            System.out.println("2. Process Account Transfer");
            System.out.println("3. Back to home");
            try {
                choice = validationUtils.getIntInput("select from (1-3): ",1,3);

                switch (choice){
                    case 1:
                        functions.handleProcessTransaction();
                        break;
                    case 2:
                        functions.handleTransfer();
                    case 3:
                        break;
                    default:
                        // Input Validation (US-5 Acceptance Criteria)
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                        break;
                }
            }catch (InputMismatchException e){
                // Input Validation for non-integer inputs (US-5 Acceptance Criteria)
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();

            }


        }while (choice !=3);
    }
    public void handleSaveLoad() {
        int choice = 0;
        do {
            System.out.println("\n--- SAVE / LOAD DATA ---");
            System.out.println("1. Save All Data to File (Accounts & Transactions)");
            System.out.println("2. Load Data from File");
            System.out.println("3. Back to Main Menu");

            try {
                choice = validationUtils.getIntInput("Select option (1-3): ", 1, 3);
                switch (choice) {
                    case 1:
                        functions.handleSaveData(); // <-- New function to implement
                        break;
                    case 2:
                        functions.handleLoadData(); // <-- New function to implement/refactor
                        break;
                    case 3:
                        break; // Back to main menu
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        } while (choice != 3);
    }
}
