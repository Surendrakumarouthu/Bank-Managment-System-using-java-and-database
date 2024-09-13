import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private String password;

    // Constructor for loading an existing account
    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
        loadFromDatabase();
    }

    // Constructor for creating a new account
    public BankAccount(String accountNumber, String accountHolderName, double initialBalance, String password) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.password = password;
        saveToDatabase();
    }

    private void loadFromDatabase() {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                this.accountHolderName = rs.getString("account_holder_name");
                this.balance = rs.getDouble("balance");
                this.password = rs.getString("password");
            } else {
                // If account not found, set accountNumber to null
                this.accountNumber = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isValid() {
        return accountNumber != null;
    }

    public boolean verifyPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    private void saveToDatabase() {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO accounts (account_number, account_holder_name, balance, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, accountHolderName);
            pstmt.setDouble(3, balance);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAccountDetails() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + accountHolderName);
        System.out.println("Balance: ₹" + balance);
    }

    public double checkBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            updateBalanceInDatabase();
            saveTransaction("Deposit", amount);
            System.out.println("₹" + amount + " deposited successfully. New Balance: ₹" + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            updateBalanceInDatabase();
            saveTransaction("Withdraw", amount);
            System.out.println("₹" + amount + " withdrawn successfully. New Balance: ₹" + balance);
        } else {
            System.out.println("Invalid or insufficient funds.");
        }
    }

    private void updateBalanceInDatabase() {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, balance);
            pstmt.setString(2, accountNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveTransaction(String transactionType, double amount) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO transactions (account_number, transaction_type, amount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, transactionType);
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewTransactionHistory() {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM transactions WHERE account_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Transaction History:");
            while (rs.next()) {
                System.out.println(rs.getString("transaction_type") + " of ₹" + rs.getDouble("amount") + " on " + rs.getTimestamp("transaction_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
