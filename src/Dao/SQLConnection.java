package Dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLConnection {
	private static String user = "yourUsername";
	private static String password = "yourPassword";
	public static Connection getConnection() {
		Connection c = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost/NganHangDeThi?user=" + user + "&password=" + password);
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
		return c;
	}
	
	public static void closeConnection(Connection c) {
		try {
			if(c != null) {
				c.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
