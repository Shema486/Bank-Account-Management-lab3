# Bank Account Management — Lab 3

the Lab 3 implementation of a console-based **Bank Management System** written in Java.

---

##  Overview
This lab implements a minimal banking model that demonstrates:
- Object-oriented design 
- Validation and domain exceptions
- Simple file-based persistence for demonstration purposes
- Unit testing for correctness
- Concurrency simulation to exercise thread-safety

### Capabilities
- Create and manage **Savings** and **Checking** accounts
- Deposit, withdraw, and transfer funds with validation and exception handling
- Record transactions with timestamps and view transaction history (newest first)
- Persist account and transaction data to simple  text files
- Simulate concurrent transactions to validate basic synchronization

---

##  Primary Components
- **`modules`** — domain objects: `Account`, `SavingsAccount`, `CheckingAccount`, `Customer`, `Transaction`, `TransactionType`, `TransactionConcurrencySimulator`
- **`services`** — business logic: `AccountManager`, `TransactionManager`
- **`utils`** — CLI and helpers: `ConsoleMenu`, `Functions`, `ValidationUtils`
- **`exceptions`** — domain exceptions used for validation and business rules
- **`interfaces`** — `Depositable`, `Withdrawable`, `Transactable`

---

##  Project Structure
```plaintext
BankAccountManagement-Lab3/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Main.java
│   │   │   ├── data/
│   │   │   │   ├── accounts.txt
│   │   │   │   └── transactions.txt
│   │   │   ├── exceptions/
│   │   │   │   ├── InsufficientFundException.java
│   │   │   │   ├── InvalidAmountException.java
│   │   │   │   └── OverdraftExceededException.java
│   │   │   ├── interfaces/
│   │   │   │   ├── Depositable.java
│   │   │   │   ├── Transactable.java
│   │   │   │   └── Withdrawable.java
│   │   │   ├── modules/
│   │   │   │   ├── Account.java
│   │   │   │   ├── SavingsAccount.java
│   │   │   │   ├── CheckingAccount.java
│   │   │   │   ├── Customer.java
│   │   │   │   ├── PremiumCustomer.java
│   │   │   │   ├── RegularCustomer.java
│   │   │   │   ├── Transaction.java
│   │   │   │   ├── TransactionType.java
│   │   │   │   └── TransactionConcurrencySimulator.java
│   │   │   ├── services/
│   │   │   │   ├── AccountManager.java
│   │   │   │   └── TransactionManager.java
│   │   │   └── utils/
│   │   │       ├── ConsoleMenu.java
│   │   │       ├── Functions.java
│   │   │       └── ValidationUtils.java
│   └── test/
│       └── java/
│           ├── modules/
│           │   ├── CheckingAccountTest.java
│           │   ├── SavingsAccountTest.java
│           │   └── TransferTest.java
│           └── services/
│               ├── AccountManagerTest.java
│               └── TransactionManagerTest.java




```

---

## Notable Implementation Details

- **Account Hierarchy**:
    - `Account` is abstract with synchronized `deposit()` and abstract `withdraw()` implemented per account type.
    - `SavingsAccount` and `CheckingAccount` enforce rules like minimum balances, interest, overdraft limits, and fees.

- **Transactions**:
    - `Transaction` objects are immutable, include an auto-generated ID, timestamp, and CSV-style `toString()` for persistence.
    - `TransactionManager` manages transactions in-memory, supports reporting, and provides thread-safe addition with `synchronized addTransaction()`.

- **Concurrency Simulation**:
    - `TransactionConcurrencySimulator` synchronizes on `Account` when performing deposits/withdrawals and logs transactions.
    - Demonstrates basic locking but is not production-grade concurrency control.

- **Persistence**:
    - Accounts and transactions are saved as CSV-like text files under `src/main/java/data/`.
    - Loading and saving use `Stream` and simple file I/O.

---

## Concurrency and Safety Notes

- Thread-safety is illustrated by:
    - Synchronizing `Account` operations
    - Synchronizing transaction list updates

> Note: These patterns are suitable for teaching labs. Production systems require database-backed transactions and proper transactional isolation.

---

## Prerequisites

- Java 11+ JDK
- Apache Maven 3.x

---

## Setup Instructions

1. Ensure `JAVA_HOME` points to a Java 11+ JDK and `mvn` is on your PATH.
2. Optional: Reset demo data by replacing `src/main/java/data/accounts.txt` and `transactions.txt`.

---

## Build

```bash
mvn -q package

