package modules;

public class TransactionConcurrencySimulator implements Runnable{
    private  Account account;
    private String type;
    private double amount;

    public TransactionConcurrencySimulator(Account account, double amount, String type) {
        this.account = account;
        this.amount = amount;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            if(type.equalsIgnoreCase("DEPOSIT")){
                System.out.println(Thread.currentThread().getName() + ":Depositing $" + amount);
                account.deposit(amount);
            } else if (type.equalsIgnoreCase("Withdraw")) {
                System.out.println(Thread.currentThread().getName() + ":Withdrawing $" + amount);
                account.withdraw(amount);
            }
        } catch (Exception e) {
            System.out.println(Thread.currentThread() + ":" + e.getMessage());
        }
    }
}
