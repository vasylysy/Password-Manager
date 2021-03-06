package dbCreators;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class usersData {
    public static void main(String[] args) {
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/",
                            "postgres", "1234");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE USERS_DATA " +
                    "(ID SERIAL PRIMARY KEY     NOT NULL," +
                    " LOGIN           CHAR(50)    NOT NULL, " +
                    " PASSWORD           CHAR(50) NOT NULL," +
                    "ANSWER           CHAR(50) NOT NULL)";

            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
}
