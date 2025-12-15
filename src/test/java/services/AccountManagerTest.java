package services;

import modules.Account;
import modules.CheckingAccount;
import modules.SavingsAccount;
import modules.Customer;
import modules.RegularCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountManagerTest {
    private AccountManager manager;
    private Customer testCustomer;
    private Account acc1;
    private Account acc2;

    @BeforeEach
    void setUp() {
        manager = new AccountManager();
        testCustomer = new RegularCustomer("Test Customer", 30, "555-0000", "Address");

        // The manager uses a static counter for account IDs./
        // We'll rely on the actual IDs being created sequentially (e.g., ACC001, ACC002)
        // Note: For deterministic testing, you might reset static counters,/
        // but for integration testing, we use the actual objects.
        acc1 = new SavingsAccount(testCustomer, 5000.00);
        acc2 = new CheckingAccount(testCustomer, 100.00);

        manager.addAccount(acc1);
        manager.addAccount(acc2);
    }

    @Test
    void testAddAccountIncrementsCount() {
        assertEquals(2, manager.getTotalAccounts(), "Manager should contain exactly 2 accounts after setup.");
    }

    @Test
    void testFindExistingAccount() {
        // Test finding the first account by its generated ID
        Account foundAccount = manager.findAccount(acc1.getAccountNumber());
        assertNotNull(foundAccount, "Account 1 should be found.");
        assertEquals(acc1.getAccountNumber(), foundAccount.getAccountNumber(), "Found account ID must match requested ID.");
    }

    @Test
    void testFindNonExistingAccount() {
        // Test searching for a synthetic ID that does not exist
        Account foundAccount = manager.findAccount("ACC999");
        assertNull(foundAccount, "Searching for a non-existent account should return null.");
    }

    @Test
    void testFindAccountCaseInsensitivity() {
        // Test that search is successful even with different casing (e.g., acc001 vs ACC001)
        Account foundAccount = manager.findAccount(acc2.getAccountNumber().toLowerCase());
        assertNotNull(foundAccount, "Search should be case-insensitive.");
        assertEquals(acc2.getAccountNumber(), foundAccount.getAccountNumber(), "Case-insensitive match failed.");
    }
}