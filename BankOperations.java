package banking;

import java.sql.*;
import java.util.Scanner;

public class BankOperations {
    static Connection con = DBConnection.getConnection();
    static Scanner sc = new Scanner(System.in);

    public static void createAccount() {
        try {
            System.out.print("Enter Name: ");
            String name = sc.next();
            System.out.print("Enter Email: ");
            String email = sc.next();
            System.out.print("Enter Password: ");
            String password = sc.next();
            System.out.print("Enter Initial Balance: ");
            double balance = sc.nextDouble();

            String sql = "INSERT INTO accounts (name, email, password, balance) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, password);
            pst.setDouble(4, balance);
            pst.executeUpdate();
            System.out.println("Account Created Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int login() {
        try {
            System.out.print("Enter Email: ");
            String email = sc.next();
            System.out.print("Enter Password: ");
            String password = sc.next();

            String sql = "SELECT account_no FROM accounts WHERE email=? AND password=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful!");
                return rs.getInt("account_no");
            } else {
                System.out.println("Invalid email or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void viewBalance(int acc) {
        try {
            String sql = "SELECT balance FROM accounts WHERE account_no=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, acc);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("Current Balance: ₹" + rs.getDouble("balance"));
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deposit(int acc) {
        try {
            System.out.print("Enter Amount to Deposit: ");
            double amt = sc.nextDouble();

            String sql = "UPDATE accounts SET balance = balance + ? WHERE account_no=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDouble(1, amt);
            pst.setInt(2, acc);
            int count = pst.executeUpdate();

            if (count > 0) {
                System.out.println("Amount Deposited!");

                String txn = "INSERT INTO transactions (account_no, type, amount) VALUES (?, 'deposit', ?)";
                PreparedStatement txnPst = con.prepareStatement(txn);
                txnPst.setInt(1, acc);
                txnPst.setDouble(2, amt);
                txnPst.executeUpdate();
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void withdraw(int acc) {
        try {
            System.out.print("Enter Amount to Withdraw: ");
            double amt = sc.nextDouble();

            String checkSql = "SELECT balance FROM accounts WHERE account_no=?";
            PreparedStatement checkPst = con.prepareStatement(checkSql);
            checkPst.setInt(1, acc);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                double bal = rs.getDouble("balance");
                if (bal >= amt) {
                    String sql = "UPDATE accounts SET balance = balance - ? WHERE account_no=?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setDouble(1, amt);
                    pst.setInt(2, acc);
                    pst.executeUpdate();

                    String txn = "INSERT INTO transactions (account_no, type, amount) VALUES (?, 'withdraw', ?)";
                    PreparedStatement txnPst = con.prepareStatement(txn);
                    txnPst.setInt(1, acc);
                    txnPst.setDouble(2, amt);
                    txnPst.executeUpdate();

                    System.out.println("Amount Withdrawn!");
                } else {
                    System.out.println("Insufficient Balance.");
                }
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAccount(int acc) {
        try {
            String sql = "DELETE FROM accounts WHERE account_no=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, acc);
            int count = pst.executeUpdate();

            if (count > 0) System.out.println("Account Deleted!");
            else System.out.println("Account not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void viewTransactions(int acc) {
        try {
            String sql = "SELECT * FROM transactions WHERE account_no=? ORDER BY trans_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, acc);
            ResultSet rs = pst.executeQuery();

            System.out.println("\n--- Transaction History ---");
            while (rs.next()) {
                System.out.println("Type: " + rs.getString("type") +
                        ", Amount: ₹" + rs.getDouble("amount") +
                        ", Date: " + rs.getTimestamp("trans_date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean adminLogin() {
        System.out.print("Enter Admin Username: ");
        String user = sc.next();
        System.out.print("Enter Admin Password: ");
        String pass = sc.next();
        return user.equals("shiva") && pass.equals("shiva123");
    }

    public static void showAllAccounts() {
        try {
            String sql = "SELECT * FROM accounts";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            System.out.println("\n--- All Registered Accounts ---");
            System.out.printf("%-10s %-20s %-25s %-10s\n", "Acc No", "Name", "Email", "Balance");
            System.out.println("-------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-10d %-20s %-25s ₹%-10.2f\n",
                        rs.getInt("account_no"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDouble("balance"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}