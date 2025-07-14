package banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        int accountNo = -1;

        do {
            System.out.println("\n--- BANKING SYSTEM ---");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("4. Admin Panel");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> BankOperations.createAccount();
                case 2 -> {
                    accountNo = BankOperations.login();
                    if (accountNo != -1) {
                        int innerChoice;
                        do {
                            System.out.println("\n--- MENU ---");
                            System.out.println("1. View Balance");
                            System.out.println("2. Deposit");
                            System.out.println("3. Withdraw");
                            System.out.println("4. Transactions");
                            System.out.println("5. Delete Account");
                            System.out.println("6. Logout");
                            System.out.print("Enter your choice: ");
                            innerChoice = sc.nextInt();

                            switch (innerChoice) {
                                case 1 -> BankOperations.viewBalance(accountNo);
                                case 2 -> BankOperations.deposit(accountNo);
                                case 3 -> BankOperations.withdraw(accountNo);
                                case 4 -> BankOperations.viewTransactions(accountNo);
                                case 5 -> BankOperations.deleteAccount(accountNo);
                                case 6 -> System.out.println("Logged out successfully.");
                                default -> System.out.println("Invalid Option!");
                            }
                        } while (innerChoice != 6);
                    }
                }
                case 3 -> System.out.println("Thanks for using our banking system!");
                case 4 -> {
                    if (BankOperations.adminLogin()) {
                        BankOperations.showAllAccounts();
                    } else {
                        System.out.println("Invalid admin credentials.");
                    }
                }
                default -> System.out.println("Invalid Choice!");
            }
        } while (choice != 3);
    }
}
