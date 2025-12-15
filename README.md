# Bank-Account-Management-2
🏦 Bank Management System
Java • OOP • SOLID • DS&A • JUnit Testing

📘 Overview
This project is a console-based Bank Management System built using Java, demonstrating:
🧱 Object-Oriented Programming
📐 SOLID Principles
⚙️ Data Structures & Algorithms
🧪 JUnit Testing
The system manages Customers, Accounts, and Transactions, supporting operations such as deposits, withdrawals, account creation, transaction history, and summary reports.

🚀 Features
🏦 Account Management
Create Savings and Checking accounts
Auto-generated Account IDs
View all accounts
Search accounts (Linear Search)

💸 Transaction Processing
Deposit
Withdraw
Balance validation rules
Overdraft handling
Save every transaction with timestamp

📜 Transaction History
View full transaction history per account
Newest → Oldest order
Summary:
Total Deposits
Total Withdrawals
Net Change

🧠 OOP Concepts Used
Concept	: How It’s Used
Inheritance	SavingsAccount and CheckingAccount inherit from Account
Polymorphism	Overridden withdraw() and deposit() behave differently
Encapsulation	Private fields + getters/setters
Abstraction	Base Account class defines shared functionality

🧩 SOLID Principles Applied
Principle	Implementation
S – Single Responsibility	Managers handle data; Accounts handle logic
O – Open/Closed	Easily add new account types without modifying the core system
L – Liskov Substitution: All account types behave as Account safely
I – Interface Segregation	(If included) separate interfaces: Depositable, Withdrawable
D – Dependency Inversion	Managers depend on List<> abstraction, not ArrayList

🧠 Data Structures & Algorithms
✔️ 1. ArrayList
Used to store:
Accounts
Transactions
✔️ 2. Linear Search
Used to find accounts:
for (Account account: accounts) {
    if (account.getAccountNumber().equalsIgnoreCase(accountNumber)) {
        return account;
    }
}

✔️ 3. Reverse Iteration
Show newest transactions first:
for (int i = history.size() - 1; i >= 0; i--) {
    history.get(i).displayTransactionDetails();
}

✔️ 4. Composition / Aggregation
AccountManager has a list of Accounts
TransactionManager has a list of Transactions

🧪 JUnit Tests
Tests ensure the system is reliable and correct.
Unit Tests Included:
SavingsAccountTest
CheckingAccountTest
AccountManagerTest
TransactionManagerTest

Tests Cover:
Minimum balance rules
Overdraft limits
Finding accounts
Deposits & withdrawals
Transaction filtering
Calculation of totals
Net balance change

