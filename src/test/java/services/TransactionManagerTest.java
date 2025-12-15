package services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import modules.Transaction;

public class TransactionManagerTest {
    private TransactionManager manager;
    private final String ACC_ID = "TSTMGR01"; // Fixed ID for testing

    @BeforeEach
    void setUp() {
        manager = new TransactionManager();

        // 1. Initial Deposit/
        manager.addTransaction(new Transaction(ACC_ID, "DEPOSIT", 1000.00, 1000.00));
        // 2. Withdrawal
        manager.addTransaction(new Transaction(ACC_ID, "WITHDRAW", 200.00, 800.00));
        // 3. Second Deposit
        manager.addTransaction(new Transaction(ACC_ID, "DEPOSIT", 50.00, 850.00));

        // Add a transaction for a different account to test filtering
        manager.addTransaction(new Transaction("OTHERID", "DEPOSIT", 500.00, 500.00));
    }

    @Test
    void testAddTransactionCount() {
        // We added 4 transactions in total (3 for ACC_ID, 1 for OTHERID)
        assertEquals(4, manager.getTransactionCount(), "Manager should contain exactly 4 transactions.");
    }

    @Test
    void testCalculateTotalDeposits() {
        // Deposits for ACC_ID: 1000.00 + 50.00 = 1050.00
        double totalDeposits = manager.calculateTotalDeposit(ACC_ID);
        assertEquals(1050.00, totalDeposits, "Total deposits should be 1050.00.");
    }

    @Test
    void testCalculateTotalWithdrawals() {
        // Withdrawals for ACC_ID: 200.00
        double totalWithdrawals = manager.calculateTotalWithdraw(ACC_ID);
        assertEquals(200.00, totalWithdrawals, "Total withdrawals should be 200.00.");
    }

    @Test
    void testCalculateNetChange() {
        // Net Change = Deposits - Withdrawals = 1050.00 - 200.00 = 850.00
        double totalDeposits = manager.calculateTotalDeposit(ACC_ID);
        double totalWithdrawals = manager.calculateTotalWithdraw(ACC_ID);
        double netChange = totalDeposits - totalWithdrawals;

        assertEquals(850.00, netChange, 0.001, "Net change should be 850.00.");
    }

    // Note: Testing the console output for viewTransactionsByAccount()
    // requires advanced techniques (like output stream capture) and is often
    // skipped in basic unit tests, as long as the underlying calculation methods pass.
}