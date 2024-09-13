import java.util.Scanner;

public class BankingManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankAccount account = null;

        while (true) {
            System.out.println("\nBanking Management System:");
            System.out.println("1. Create or Load Account");
            System.out.println("2. Display Account Details");
            System.out.println("3. Check Balance");
            System.out.println("4. Deposit Money");
            System.out.println("5. Withdraw Money");
            System.out.println("6. View Transaction History");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            if (choice != 1) {
                if (account != null) {
                    System.out.print("Enter your password: ");
                    String inputPassword = scanner.nextLine();
                    if (!account.verifyPassword(inputPassword)) {
                        System.out.println("Incorrect password. Access denied.");
                        continue;
                    }
                } else {
                    System.out.println("No account found. Please create or load an account first.");
                    continue;
                }
            }

            switch (choice) {
                case 1:
                    // Create or Load Account
                    System.out.print("Enter Account Number: ");
                    String accountNumber = scanner.nextLine();
                    account = new BankAccount(accountNumber);
                    if (!account.isValid()) {
                        // If account does not exist, create a new one
                        System.out.print("Account not found. Enter Account Holder Name to create a new account: ");
                        String accountHolderName = scanner.nextLine();
                        System.out.print("Enter Initial Balance: ₹");
                        double initialBalance = scanner.nextDouble();
                        scanner.nextLine();  // Consume the newline character
                        System.out.print("Set your password: ");
                        String password = scanner.nextLine();
                        account = new BankAccount(accountNumber, accountHolderName, initialBalance, password);
                        System.out.println("Account created successfully.");
                    } else {
                        System.out.println("Account loaded successfully.");
                    }
                    break;

                case 2:
                    // Display Account Details
                    account.displayAccountDetails();
                    break;

                case 3:
                    // Check Balance
                    System.out.println("Current Balance: ₹" + account.checkBalance());
                    break;

                case 4:
                    // Deposit Money
                    System.out.print("Enter amount to deposit: ₹");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    break;

                case 5:
                    // Withdraw Money
                    System.out.print("Enter amount to withdraw: ₹");
                    double withdrawAmount = scanner.nextDouble();
                    account.withdraw(withdrawAmount);
                    break;

                case 6:
                    // View Transaction History
                    account.viewTransactionHistory();
                    break;

                case 7:
                    // Exit
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
