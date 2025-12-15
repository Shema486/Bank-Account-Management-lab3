package modules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;



public class CheckingAccountTest {
    private CheckingAccount account;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new RegularCustomer("Test User", 30, "123", "Anywhere");
        // Start with a small positive balance: $500.00
        account = new CheckingAccount(testCustomer, 500.00);
    }

    @Test
    @DisplayName("WithdrawalIntoNegativeButWithinLimit")
    void testWithdrawalIntoNegativeButWithinLimit() {
        // Withdraw $1200.00. New balance should be -$700.00 (Within -$1000 limit).
        assertTrue(account.withdraw(1200.00), "Withdrawal into overdraft should succeed if within limit.");
        assertEquals(-700.00, account.getBalance(), 0.001, "Balance should be correct negative value.");
    }

    @DisplayName("WithdrawalExactlyToLimit")
    @Test
    void testWithdrawalExactlyToLimit() {
        // Account has $500.00. Withdraw $1500.00. New balance should be -$1000.00 (Exactly the limit).
        assertTrue(account.withdraw(1500.00), "Withdrawal exactly to the overdraft limit should succeed.");
        assertEquals(-1000.00, account.getBalance(),  "Balance should equal the overdraft limit.");
    }



    @Test
    @DisplayName("DepositWhenOverdrawn")
    void testDepositWhenOverdrawn() {
        // Put the account into overdraft first
        account.withdraw(700.00); // Balance is now -$200.00

        // Deposit $300.00. New balance should be $100.00
        assertTrue(account.deposit(300.00), "Deposit should succeed.");
        assertEquals(100.00, account.getBalance(),  "Balance should restore to positive value.");
    }

}