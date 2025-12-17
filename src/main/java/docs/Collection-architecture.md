# Collection Architecture Documentation
## Bank Account Management System - Lab 3

---

##  Purpose
This document explains the strategic use of Java Collections Framework in the Bank Account Management System, detailing implementation decisions, performance characteristics, and concurrency considerations for a teaching lab environment.

---

##  Collections Used in This Project

### 1. **HashMap<String, Account>** in AccountManager
- **Declaration**: `private Map<String, Account> accountsMap;`
- **Initialization**: `accountsMap = new HashMap<>();`
- **Purpose**: Store and manage all bank accounts with account number as key
- **Location**: `services/AccountManager.java`

### 2. **ArrayList<Transaction>** in TransactionManager  
- **Declaration**: `private List<Transaction> transactions;`
- **Initialization**: `this.transactions = new ArrayList<>();`
- **Purpose**: Maintain chronological record of all banking transactions
- **Location**: `services/TransactionManager.java`

---

## 🎯 Why These Collections Were Chosen

### HashMap<String, Account> — Account Storage

**Why HashMap?**
- ✅ **Fast Lookup**: O(1) average-case time complexity for `get()` and `put()` operations
- ✅ **Key-Based Access**: Account number serves as natural unique identifier
- ✅ **Efficient Updates**: Direct access to accounts without linear search
- ✅ **Memory Efficient**: Low overhead for lab-scale data (dozens to hundreds of accounts)
- ✅ **Simple API**: Intuitive methods for CRUD operations

**Real-World Operations**:
```java
// Adding account: O(1)
accountsMap.put(newAccount.getAccountNumber(), newAccount);

// Finding account: O(1)
Account account = accountsMap.get(accountNumber);

// Iterating all accounts: O(n)
accountsMap.values().stream().forEach(Account::displayAccountDetails);
```

**Performance Characteristics**:
| Operation | Time Complexity | Use Case in Lab |
|-----------|----------------|------------------|
| Add account | O(1) avg | Account creation |
| Find account | O(1) avg | Deposits, withdrawals, transfers |
| Remove account | O(1) avg | Account closure |
| Iterate all | O(n) | View all accounts, calculate totals |

---

### ArrayList<Transaction> — Transaction History

**Why ArrayList?**
- ✅ **Insertion Order**: Preserves chronological sequence of transactions
- ✅ **Fast Append**: Amortized O(1) for `add()` operation at end of list
- ✅ **Indexed Access**: O(1) random access for reverse iteration (newest-first display)
- ✅ **Cache Locality**: Contiguous memory improves iteration performance
- ✅ **Stream-Friendly**: Excellent integration with Java Stream API for filtering and aggregation

**Real-World Operations**:
```java
// Adding transaction: O(1) amortized
transactions.add(newTransaction);

// Filtering by account: O(n)
List<Transaction> history = transactions.stream()
    .filter(t -> t.getAccountNumber().equals(accountNumber))
    .toList();

// Reverse iteration (newest first): O(n)
for (int i = history.size() - 1; i >= 0; i--) {
    history.get(i).displayTransactionDetails();
}
```

**Performance Characteristics**:
| Operation | Time Complexity | Use Case in Lab |
|-----------|----------------|------------------|
| Add transaction | O(1) amortized | Every deposit/withdrawal |
| Filter by account | O(n) | View transaction history |
| Calculate totals | O(n) | Deposit/withdrawal summaries |
| Reverse iteration | O(n) | Display newest-first |

---

## 🔍 How the Code Relies on Collection Properties

### Order Preservation (ArrayList)
- **Requirement**: Transaction history must display in chronological order (newest first)
- **Implementation**: ArrayList maintains insertion order; reverse iteration shows recent transactions first
- **Code Example**:
  ```java
  // Display newest transactions first
  for (int i = history.size() - 1; i >= 0; i--) {
      history.get(i).displayTransactionDetails();
  }
  ```

### Fast Key-Based Lookup (HashMap)
- **Requirement**: Quickly find accounts during deposits, withdrawals, and transfers
- **Implementation**: HashMap provides O(1) lookup by account number
- **Code Example**:
  ```java
  public Account findAccount(String accountNumber) {
      return accountsMap.get(accountNumber); // O(1) lookup
  }
  ```

### Stream API Integration
- **Requirement**: Calculate totals, filter transactions, aggregate data
- **Implementation**: Both collections work seamlessly with Java Streams
- **Code Examples**:
  ```java
  // Total balance across all accounts
  double total = accountsMap.values().stream()
      .mapToDouble(Account::getBalance)
      .sum();
  
  // Total deposits for an account
  double deposits = transactions.stream()
      .filter(t -> t.getAccountNumber().equals(accountNumber))
      .filter(t -> t.getType().equals("DEPOSIT"))
      .mapToDouble(Transaction::getAmount)
      .sum();
  ```

### Persistence and Serialization
- **Requirement**: Save and load data from text files
- **Implementation**: Collections serialize to CSV-style format using Streams
- **Files**: `src/main/java/data/accounts.txt` and `transactions.txt`
- **Code Example**:
  ```java
  // Save accounts
  List<String> lines = accountsMap.values().stream()
      .map(Account::toString)
      .toList();
  Files.write(Paths.get(filePath), lines);
  
  // Load accounts
  Files.readAllLines(path).stream()
      .map(Account::fromString)
      .forEach(account -> accountsMap.put(account.getAccountNumber(), account));
  ```

---

## 🔒 Concurrency & Thread Safety

### Current Lab-Level Protections

#### 1. Synchronized Transaction Addition
```java
public synchronized void addTransaction(Transaction newTransaction) {
    this.transactions.add(newTransaction);
}
```
- **Protection**: Prevents concurrent modification of the ArrayList
- **Scope**: Method-level synchronization on TransactionManager instance
- **Benefit**: Multiple threads can safely add transactions without data corruption

#### 2. Account-Level Synchronization
```java
@Override
public void run() {
    synchronized (account) {
        // Deposit or withdraw
        // Then add transaction
    }
}
```
- **Protection**: Locks individual Account object during operations
- **Scope**: Block-level synchronization in TransactionConcurrencySimulator
- **Benefit**: Prevents race conditions on account balance updates

### Known Limitations & Risks

#### ⚠️ HashMap is Not Thread-Safe
- **Issue**: Concurrent reads and writes to `accountsMap` can cause:
  - Data corruption
  - Infinite loops during rehashing
  - Lost updates
- **Current Risk Level**: Low (lab uses single-threaded account management)
- **Production Fix**: Use `ConcurrentHashMap<String, Account>`

#### ⚠️ Lack of Transactional Atomicity
- **Issue**: Account update and transaction logging are separate synchronized blocks
- **Scenario**: If transaction logging fails after account update, system state becomes inconsistent
- **Example**:
  ```java
  synchronized (account) {
      account.withdraw(100); // ✅ Succeeds
  }
  // ❌ If JVM crashes here, transaction is lost
  transactionManager.addTransaction(tx);
  ```
- **Production Fix**: Use database transactions with ACID guarantees

#### ⚠️ ArrayList Iteration During Modification
- **Issue**: If one thread iterates while another adds, `ConcurrentModificationException` may occur
- **Current Risk Level**: Low (reporting happens outside concurrent operations)
- **Production Fix**: Use `CopyOnWriteArrayList` or synchronized iteration

---

## 🚀 Recommended Improvements for Production

### 1. Thread-Safe Collections

**Replace HashMap with ConcurrentHashMap**:
```java
// Before (Lab)
private Map<String, Account> accountsMap = new HashMap<>();

// After (Production)
private final ConcurrentMap<String, Account> accountsMap = new ConcurrentHashMap<>();
```
- ✅ Lock-free reads
- ✅ Fine-grained locking for writes
- ✅ No external synchronization needed

**Replace ArrayList with ConcurrentLinkedDeque**:
```java
// Before (Lab)
private List<Transaction> transactions = new ArrayList<>();
public synchronized void addTransaction(Transaction t) {
    transactions.add(t);
}

// After (Production)
private final Deque<Transaction> transactions = new ConcurrentLinkedDeque<>();
public void addTransaction(Transaction t) {
    transactions.addLast(t); // No synchronization needed
}
```
- ✅ Lock-free append operations
- ✅ Better performance under high concurrency
- ✅ No method-level synchronization required

### 2. Defensive Copying & Immutability

**Return Unmodifiable Views**:
```java
// Prevent external modification of internal collections
public List<Transaction> getTransactionsByAccount(String accountNumber) {
    return Collections.unmodifiableList(
        transactions.stream()
            .filter(t -> t.getAccountNumber().equals(accountNumber))
            .toList()
    );
}

public Collection<Account> getAllAccounts() {
    return Collections.unmodifiableCollection(accountsMap.values());
}
```

### 3. Performance Optimizations

**Pre-size Collections**:
```java
// Reduce rehashing overhead
private Map<String, Account> accountsMap = new HashMap<>(100); // Expected size
private List<Transaction> transactions = new ArrayList<>(1000);
```

**Index for Frequent Queries**:
```java
// Avoid O(n) scans for transaction lookups
private final Map<String, List<Transaction>> transactionsByAccount = new ConcurrentHashMap<>();

public void addTransaction(Transaction t) {
    transactions.addLast(t);
    transactionsByAccount
        .computeIfAbsent(t.getAccountNumber(), k -> new CopyOnWriteArrayList<>())
        .add(t);
}
```

### 4. Snapshot Pattern for Reporting
```java
// Create consistent view for iteration
public void generateReport() {
    List<Transaction> snapshot = new ArrayList<>(transactions);
    // Safe to iterate without locking
    snapshot.forEach(Transaction::displayTransactionDetails);
}
```

---

## 📊 Performance Analysis

### Time Complexity Summary

| Operation | HashMap | ArrayList | Impact |
|-----------|---------|-----------|--------|
| Add | O(1) avg | O(1) amortized | ✅ Excellent |
| Find by key | O(1) avg | O(n) | ✅ HashMap wins |
| Iterate all | O(n) | O(n) | ✅ Both efficient |
| Filter | O(n) | O(n) | ⚠️ Linear scan |
| Remove | O(1) avg | O(n) | ⚠️ ArrayList slow |

### Space Complexity
- **HashMap**: O(n) with load factor overhead (~1.33x for 0.75 load factor)
- **ArrayList**: O(n) with capacity overhead (grows by ~1.5x when full)
- **Total Memory**: Acceptable for lab scale (< 10,000 records)

### Optimization Tips

1. **Pre-size Collections**:
   ```java
   new HashMap<>(expectedSize)  // Avoid rehashing
   new ArrayList<>(expectedSize) // Avoid array copying
   ```

2. **Avoid Repeated Filtering**:
   ```java
   // Bad: O(n) every time
   transactions.stream().filter(t -> t.getAccountNumber().equals(acc)).toList();
   
   // Good: O(1) with index
   transactionIndex.get(accountNumber);
   ```

3. **Batch Operations**:
   ```java
   // Process multiple transactions in one pass
   transactions.stream()
       .collect(Collectors.groupingBy(Transaction::getAccountNumber));
   ```

---

## 🎓 When to Move Beyond In-Memory Collections

### Use a Database When:

1. **Data Volume Exceeds Memory**
   - Current: Hundreds of accounts, thousands of transactions
   - Threshold: > 100,000 records or > 1GB data
   - Solution: PostgreSQL, MySQL, or embedded H2/SQLite

2. **Durability is Critical**
   - Current: File-based persistence (not crash-safe)
   - Requirement: ACID transactions, write-ahead logging
   - Solution: Relational database with transaction support

3. **Multi-Instance Deployment**
   - Current: Single JVM, in-memory state
   - Requirement: Multiple application servers, shared state
   - Solution: Centralized database with connection pooling

4. **Complex Queries Needed**
   - Current: Stream API filtering (O(n) scans)
   - Requirement: Joins, aggregations, indexed searches
   - Solution: SQL with proper indexes

5. **Audit & Compliance**
   - Current: Simple CSV files
   - Requirement: Immutable audit logs, regulatory compliance
   - Solution: Database with triggers, event sourcing

---

## 📝 Summary & Conclusion

### Why Current Design Works for Lab 3

✅ **Simplicity**: Easy to understand and implement  
✅ **Performance**: Excellent for small-to-medium datasets  
✅ **Teachability**: Demonstrates core collection concepts  
✅ **Maintainability**: Minimal dependencies, straightforward debugging  
✅ **Testability**: Easy to mock and unit test  

### Key Takeaways

1. **HashMap** provides O(1) account lookups — essential for banking operations
2. **ArrayList** maintains transaction order — critical for history display
3. **Stream API** enables declarative data processing — cleaner code
4. **Synchronization** prevents data corruption — basic thread safety
5. **File persistence** demonstrates serialization — simple durability

### Evolution Path

```
Lab 3 (Current)          →  Production System
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
HashMap                  →  ConcurrentHashMap
ArrayList                →  ConcurrentLinkedDeque
File persistence         →  Database (PostgreSQL)
Synchronized methods     →  Database transactions
In-memory state          →  Distributed cache (Redis)
Single JVM               →  Microservices
```

---

## 🔧 Next Steps for Enhancement

If you want to extend this lab, consider:

1. **Implement ConcurrentHashMap + ConcurrentLinkedDeque** and update tests
2. **Add transaction index** (`Map<String, List<Transaction>>`) for O(1) lookups
3. **Generate PlantUML diagram** showing collection relationships
4. **Add performance benchmarks** comparing collection choices
5. **Implement database persistence** using JDBC or JPA

---

**Document Version**: 1.0  
**Last Updated**: Lab 3 Implementation  
**Author**: Bank Account Management System Team
