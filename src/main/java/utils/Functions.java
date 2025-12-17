package utils;

import modules.*;
import services.AccountManager;
import services.TransactionManager;


public class Functions {
    static ValidationUtils validationUtils = new ValidationUtils();
    static AccountManager accountManager = new AccountManager();
    static TransactionManager transactionManager = new TransactionManager();
    public  void handleCreateAccount() {

        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗");
        System.out.println("CREATE NEW ACCOUNT");
        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗\n");
//        System.out.print("ENTER CUSTOMER NAME: ");
        String name = validationUtils.getStringInput("ENTER CUSTOMER NAME: ","^[A-Za-z ]{2,}$","Name must contain letters only.");

//        System.out.print("ENTER AGE: ");
        int age = validationUtils.getIntInput("Enter Customer Age: ",18,70);
        if (age == -1) return;

//        System.out.print("ENTER CONTACT: ");
        String contact = validationUtils.getStringInput("ENTER CONTACT: ","^\\d{10,13}$", "Phone must be 10–13 digits.");

//        System.out.print("ENTER ADDRESS: ");
        String address = validationUtils.getStringInput("ENTER EMAIL: ","^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", "Invalid email format. Example: name@gmail.com");

        System.out.println("\n-------CUSTOMER TYPE:-----");
        System.out.println("1.Regular Customer (Standard banking service) ");
        System.out.println("2.Premium Customer (Enhanced benefits, min balance) ");
//        System.out.print("select type (1-2): ");
        int customerType = validationUtils.getIntInput(" select type (1-2): ",1,2);


        Customer customer;
        if (customerType ==1){
            customer = new RegularCustomer(name,age,contact,address);
        }else {
            customer =new PremiumCustomer(name,age,contact,address);
        }
        System.out.println("\nACCOUNT TYPE:\n");
        System.out.println("1. Saving Account (Interest:3.5%, Min Balance: $500)");
        System.out.println("2. Checking Account (Overdraft: $1000, Monthly fee: $10)");
//        System.out.print("\select type (1-2): ");
        int accountType = validationUtils.getIntInput("select type (1-2): ",1,2);

//        System.out.print("Enter initial deposit amount: $");
        double amountDeposited = validationUtils.getDoubleInput("Enter initial deposit amount: $",500);

        Account account;
        if (accountType == 1){
            account = new SavingsAccount(customer,amountDeposited);
        }
        else {
            account = new CheckingAccount(customer,amountDeposited);
        }
        accountManager.addAccount(account);
        account.displayAccountDetails();
        System.out.println("\n ✅ CREATION OF YOUR ACCOUNT IS SUCCESSFULLY COMPLETE");
        validationUtils.enterToContinue();
    }
    public void handleProcessTransaction() {

        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗");
        System.out.println("PROCESS TRANSACTION");
        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗\n");

        String accNumber = validationUtils.getStringInput("Enter Account number (e.g., ACC001): ","^ACC\\d{3}$", "Invalid account number format.");

        // Retrieve account
        Account account = accountManager.findAccount(accNumber);
        if (account == null) {
            System.out.println("Account does not exist.");
            validationUtils.enterToContinue();
            return;
        }

        System.out.println("\nAccount Details:");
        System.out.println("Customer name: " + account.getCustomer().getName());
        System.out.println("Account type: " + account.getAccountType());
        System.out.println("Current Balance: $" + account.getBalance());

        // Select transaction type
        System.out.println("\nTransaction type:");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        int type = validationUtils.getIntInput("Select type (1-2): ", 1, 2);

        String transactionType = (type == 1) ? "DEPOSIT" : "WITHDRAW";

        // Enter amount
        double amount = validationUtils.getDoubleInput("Enter amount for transaction: $", 500);

        double initialBalance = account.getBalance();

        System.out.println("\n‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗");
        System.out.println("Transaction Confirmation");
        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗");

        System.out.println("Account Number: " + accNumber);
        System.out.println("Transaction: " + transactionType);
        System.out.println("Amount: $" + amount);
        System.out.println("Previous Balance: $" + initialBalance);

        String confirm = validationUtils.getStringInput("\nConfirm transaction? (Y/N): ","^(y|Y|n|N|yes|YES|no|NO)$", "Invalid account number format.");

        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("\nTransaction canceled.");
            validationUtils.enterToContinue();
            return;
        }

        // Process transaction
        boolean success = account.processTransaction(amount, transactionType);

        if (success) {
            // Create transaction with updated balance
            Transaction transaction = new Transaction(accNumber, transactionType, amount, account.getBalance());
            transactionManager.addTransaction(transaction);

            System.out.println("\n=========== Transaction Completed Successfully ===========");
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("New Balance: $" + transaction.getBalanceAfter());
            System.out.println("Date/Time: " + transaction.getTimestamp());

        } else {
            System.out.println("\nTransaction failed. Please try again.");
        }

        validationUtils.enterToContinue();
    }
    public void handleViewHistory() {
        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗");
        System.out.println("View transaction history");
        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗\n");

//        System.out.print("Enter Account (e.g,ACC001): ");
        String accNum = validationUtils.getStringInput("Enter Account (e.g,ACC001): ","^ACC\\d{3}$", "Invalid account number format.");

        Account account = accountManager.findAccount(accNum);
        if(account==null){
            System.out.println("\nAccount not found");
            validationUtils.enterToContinue();
            return;
        }
        System.out.println("\nCustomer: "+ account.getCustomer().getName());
        System.out.println("Account type: " + account.getAccountNumber());
        System.out.println("Current balance: " + account.getBalance());

        transactionManager.viewTransactionsByAccount(accNum);
        System.out.println("Total Deposit: " + transactionManager.calculateTotalDeposit(accNum));
        System.out.println("Total withdraw: " + transactionManager.calculateTotalWithdraw(accNum));
    }
    public void handleTransfer(){
        System.out.println("\n--- PROCESS ACCOUNT TRANSFER ---");
        //System.out.println("Enter Source Account (e.g ACC001)");
        String sourceAccNum = validationUtils.getStringInput("Enter SOURCE Account Number (From): (e.g ACC001)", "^ACC\\d{3}$", "Invalid account number format.");
        Account sourceAccount = accountManager.findAccount(sourceAccNum);
        if (sourceAccount == null){
            System.out.println("Error: Source Account not found.");
            return;
        }

        String targetAccNum = validationUtils.getStringInput("Enter TARGET Account Number (To):(e.g ACC002) ","^ACC\\d{3}$", "Invalid account number format.");
        Account targetAccount = accountManager.findAccount(targetAccNum);

        if (targetAccount == null) {
            System.out.println("Error: Target Account not found.");
            return;
        }

        if (sourceAccount.getAccountNumber().equalsIgnoreCase(targetAccount.getAccountNumber())) {
            System.out.println("Error: Cannot transfer funds to the same account.");
            return;
        }

//        System.out.print("Enter Transfer Amount: $");
        double transferAmount = validationUtils.getDoubleInput("Enter Transfer Amount: $",500); // Assumes your helper method for input validation

        if (transferAmount <= 0) {
            System.out.println("Error: Transfer amount must be positive.");
            return;
        }
        // --- 4. EXECUTION ---
        // Attempt Withdrawal FIRST (Triggers all validation rules)
        System.out.println("Attempting withdrawal from source...");
        if (sourceAccount.withdraw(transferAmount)) {
            // Withdrawal successful, now process deposit
            System.out.println("Withdrawal successful. Processing deposit to target...");

            if (targetAccount.deposit(transferAmount)) {

                Transaction withdrawal = new Transaction(sourceAccNum, "WITHDRAW", transferAmount, sourceAccount.getBalance());
                transactionManager.addTransaction(withdrawal);

                // Record Deposit Transaction
                Transaction deposit = new Transaction(targetAccNum, "DEPOSIT", transferAmount, targetAccount.getBalance());
                transactionManager.addTransaction(deposit);

                System.out.printf("\nSUCCESS: Transfer of $%,.2f complete.\n", transferAmount);
                System.out.printf("  Source Balance (%s): %s\n", sourceAccNum, sourceAccount.getBalance());
                System.out.printf("  Target Balance (%s): %s\n", targetAccNum, targetAccount.getBalance());

            } else {
                System.out.println("Withdrawal succeeded, but deposit failed. Balance restored.");
                sourceAccount.deposit(transferAmount);
            }
        } else {
            System.out.println("TRANSFER FAILED: Withdrawal from source account was rejected (Check account rules/limits).");
        }
    }
    public void initializeData() {

        Customer c1 = new RegularCustomer("Alice", 64, "555-555", "111-RWANDA");
        Customer c2 = new RegularCustomer("SHEMA", 54, "544-335", "122-BURUNDI");
        Customer c3 = new RegularCustomer("Bruce", 24, "522-522", "333-CAMERON");
        Customer c4 = new PremiumCustomer("Ange", 30, "115-511", "334-CANADA");
        Customer c5 = new PremiumCustomer("Peace", 54, "235-445", "443-USA");

        Account acc1 = new SavingsAccount(c1, 5000);
        Account acc2 = new SavingsAccount(c2, 2000);
        Account acc3 = new SavingsAccount(c3, 7500);
        Account acc4 = new CheckingAccount(c4, 4000);
        Account acc5 = new CheckingAccount(c5, 8000);

        accountManager.addAccount(acc1);
        accountManager.addAccount(acc2);
        accountManager.addAccount(acc3);
        accountManager.addAccount(acc4);
        accountManager.addAccount(acc5);
        // Create initial Transactions (US-4 initial data)
        acc1.deposit(500.00);
        acc2.deposit(1500.00);
        acc3.withdraw(900.00);
        acc4.deposit(1200.00);
        acc5.withdraw(1500.00);

        transactionManager.addTransaction(new Transaction(acc1.getAccountNumber(), "DEPOSIT", 500.00, acc1.getBalance()));
        transactionManager.addTransaction(new Transaction(acc2.getAccountNumber(), "DEPOSIT", 1500.00, acc2.getBalance()));
        transactionManager.addTransaction(new Transaction(acc3.getAccountNumber(), "WITHDRAW", 900.00, acc3.getBalance()));
        transactionManager.addTransaction(new Transaction(acc4.getAccountNumber(), "DEPOSIT", 1200.00, acc4.getBalance()));
        transactionManager.addTransaction(new Transaction(acc5.getAccountNumber(), "WITHDRAW", 1500.00, acc5.getBalance()));

    }
    public   void runConcurrentSimulation() {
        System.out.println("\nRunning concurrent transaction simulation...");
        String accNo = validationUtils.getStringInput("Enter account for simulation (ACC001): ", "^ACC\\d{3}$", "Invalid account format.");
        Account acc = accountManager.findAccount(accNo);

        if (acc == null) {System.out.println("Account not found.");
            return;
        }
        Thread t1 = new Thread(new TransactionConcurrencySimulator(acc, 500, TransactionType.DEPOSIT,transactionManager), "Thread-1");
        Thread t2 = new Thread(new TransactionConcurrencySimulator(acc, 300, TransactionType.DEPOSIT,transactionManager), "Thread-2");
        Thread t3 = new Thread(new TransactionConcurrencySimulator(acc, 200, TransactionType.WITHDRAW,transactionManager), "Thread-3");
        try {
            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();

            System.out.println("✓ Thread-safe operations completed successfully.");
            System.out.printf("Final Balance for %s: $%.2f%n",
                    acc.getAccountNumber(), acc.getBalance());

        } catch (InterruptedException e) {
            System.out.println("Simulation interrupted.");
        }
    }


    //  Handles saving data
    public void handleSaveData() {
        accountManager.saveAccounts();
        transactionManager.saveTransaction();
        validationUtils.enterToContinue();
    }
    //  Handles load data
    public void handleLoadData() {
        accountManager.loadAccounts();
        transactionManager.loadTransaction();

    }

}
