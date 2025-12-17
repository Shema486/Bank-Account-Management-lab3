package modules;

import services.TransactionManager;

public class TransactionConcurrencySimulator implements Runnable {

    private final Account account;
    private final TransactionType type;
    private final double amount;
    private final TransactionManager transactionManager;

    public TransactionConcurrencySimulator(Account account,
                                           double amount,
                                           TransactionType type,
                                           TransactionManager transactionManager) {
        this.account = account;
        this.amount = amount;
        this.type = type;
        this.transactionManager = transactionManager;
    }

    @Override
    public void run() {
        try {
            synchronized (account) {

                switch (type) {
                    case DEPOSIT -> {
                        account.deposit(amount);
                        System.out.println(
                                Thread.currentThread().getName()
                                        + ": Deposited $" + amount
                        );
                    }
                    case WITHDRAW -> {
                        account.withdraw(amount);
                        System.out.println(
                                Thread.currentThread().getName()
                                        + ": Withdrew $" + amount
                        );
                    }
                }

                transactionManager.addTransaction(new Transaction(
                                account.getAccountNumber(),
                                type.name(),
                                amount,
                                account.getBalance())
                );
            }
        } catch (Exception e) {
            System.out.println(
                    Thread.currentThread().getName() + ": " + e.getMessage()
            );
        }
    }
}
