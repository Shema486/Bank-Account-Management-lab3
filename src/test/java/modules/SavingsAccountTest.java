package modules;
import exceptions.InsufficientFundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SavingsAccountTest {
    private SavingsAccount account;
    // We need a dummy customer object for the account constructor
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        // Initialize a RegularCustomer and a SavingsAccount before each test/
        testCustomer = new RegularCustomer("Test User", 30, "123", "Anywhere");
        // Start with a balance of $1000.00
        account = new SavingsAccount(testCustomer, 1000.00);
    }

    @Test
    @DisplayName("SuccessfulWithdrawalAboveMinimum")
    void testSuccessfulWithdrawalAboveMinimum() {
        // Withdraw $400.00. New balance should be $600.00 (Above $500 min).
        assertTrue(account.withdraw(400.00), "Withdrawal should succeed.");
        assertEquals(600.00, account.getBalance(),  "Balance should be updated correctly.");
    }

    @Test
    @DisplayName("WithdrawalFailsBelowMinimum")
    void testWithdrawalExactlyToMinimum() {
        // Withdraw $500.00. New balance should be $500.00 (Exactly the minimum).
        assertTrue(account.withdraw(500.00), "Withdrawal to minimum should succeed.");
        assertEquals(500.00, account.getBalance(),  "Balance should equal minimum balance.");
    }

    @Test
    @DisplayName("WithdrawalFailsBelowMinimum")
    void testWithdrawalFailsBelowMinimum() {
        Exception exception = assertThrows(InsufficientFundException.class, () -> {
            account.withdraw(500.01);
        });

        assertEquals("Withdrawal denied! Minimum balance $500.0 must be maintained.",
                exception.getMessage());

        // Balance should remain unchanged
        assertEquals(1000.00, account.getBalance(), "Balance should remain unchanged after failed withdrawal.");
    }


    @Test
    @DisplayName("WithdrawalFailsForInsufficientFunds")
    void testWithdrawalFailsForInsufficientFunds() {
        Exception exception = assertThrows(InsufficientFundException.class, () -> {
            account.withdraw(600.01);
        });

        assertEquals("Withdrawal denied! Minimum balance $500.0 must be maintained.",
                exception.getMessage());
    }


    @Test
    @DisplayName("SuccessfulDeposit")
    void testSuccessfulDeposit() {
        // Deposit $50.00
        assertTrue(account.deposit(50.00), "Deposit should succeed.");
        assertEquals(1050.00, account.getBalance(),  "Balance should increase by deposit amount.");
    }
}