package modules;
import exceptions.InsufficientFundException;
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
        double amountToTransfer = 500.01;

        InsufficientFundException ex = assertThrows(
                InsufficientFundException.class,
                () -> sourceSavings.withdraw(amountToTransfer)
        );

        // Optional: verify message
        assertEquals(
                "Withdrawal denied! Minimum balance $500.0 must be maintained.",
                ex.getMessage()
        );

        // Balances must remain unchanged
        assertEquals(SOURCE_START_BALANCE, sourceSavings.getBalance());
        assertEquals(TARGET_START_BALANCE, targetChecking.getBalance());
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
