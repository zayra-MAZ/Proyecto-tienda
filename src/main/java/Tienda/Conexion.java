package Tienda;
import java.sql.*;

public class Conexion {
    public static Connection conectar() {
        Connection con = null;
        try {
           con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "1234");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return con;
    }
}