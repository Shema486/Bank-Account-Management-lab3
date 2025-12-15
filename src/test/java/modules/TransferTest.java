package modules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.AccountManager;
import services.TransactionManager;

import static org.junit.jupiter.api.Assertions.*;

// Assuming you have access to the managers and accounts defined in your project
public class TransferTest {
    private AccountManager accountManager;
    private TransactionManager transactionManager;
    private Account sourceSavings;
    private Account targetChecking;

    // Constants for testing
    private final double SOURCE_START_BALANCE = 1000.00;
    private final double TARGET_START_BALANCE = 50.00;

    @BeforeEach
    void setUp() {
        // 1. Initialize Managers
        accountManager = new AccountManager();
        transactionManager = new TransactionManager();

        // 2. Setup Accounts
        Customer cust = new RegularCustomer("Transfer", 30, "123", "TestAddress");
        sourceSavings = new SavingsAccount(cust, SOURCE_START_BALANCE);
        targetChecking = new CheckingAccount(cust, TARGET_START_BALANCE);

        accountManager.addAccount(sourceSavings);
        accountManager.addAccount(targetChecking);


    }


    @Test
    void testTransferFailsDueToSavingsMinBalance() {
        // Transfer amount that would leave the savings account with $499.99 (violates $500 min)
        double amountToTransfer = 500.01;

        // NOTE: You need to mock or isolate the logic from Main.handleTransfer() here.
        // For demonstration, we simulate the core logic:
        boolean withdrawalSuccess = sourceSavings.withdraw(amountToTransfer);

        // 1. Assert Withdrawal Failure
        assertFalse(withdrawalSuccess, "Withdrawal should fail due to minimum balance rule.");

        // 2. Assert Balances Are Unchanged
        assertEquals(SOURCE_START_BALANCE, sourceSavings.getBalance(),  "Source balance must be unchanged.");
        assertEquals(TARGET_START_BALANCE, targetChecking.getBalance(),  "Target balance must be unchanged.");

        // 3. Assert NO Transactions Were Recorded (Assuming this is checked separately or via a manager method)
        // You would typically assert that the transaction manager's count is zero.
    }

    // --- Successful Test ---
    @Test
    void testSuccessfulTransferAndTransactionRecording() {
        double amountToTransfer = 100.00;

        // Simulate the successful transfer logic from Main.handleTransfer()
        boolean withdrawalSuccess = sourceSavings.withdraw(amountToTransfer);
        targetChecking.deposit(amountToTransfer);

        // Record transactions (essential part of the transfer logic to test)
        transactionManager.addTransaction(new Transaction(sourceSavings.getAccountNumber(), "WITHDRAW", amountToTransfer, sourceSavings.getBalance()));
        transactionManager.addTransaction(new Transaction(targetChecking.getAccountNumber(), "DEPOSIT", amountToTransfer, targetChecking.getBalance()));

        // 1. Assert Success
        assertTrue(withdrawalSuccess, "Withdrawal should succeed.");

        // 2. Assert Balances Changed Correctly
        assertEquals(900.00, sourceSavings.getBalance(),  "Source balance should decrease.");
        assertEquals(150.00, targetChecking.getBalance(),  "Target balance should increase.");

        // 3. Assert Two Transactions Recorded
        assertEquals(2, transactionManager.getTransactionCount(), "Exactly two transactions  must be recorded.");
    }
}
