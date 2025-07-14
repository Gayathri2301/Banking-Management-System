package banking;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static Connection con;

    public static Connection getConnection() {
        if (con == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/banking_db", "root", "tiger"
                );
                System.out.println("✅ Database Connected!");
            } catch (Exception e) {
                System.out.println("❌ Failed to connect to DB");
                e.printStackTrace();
            }
        }
        return con;
    }
}
