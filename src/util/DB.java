package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DB {

	private static DB instance = null;
	private static String url = "";
	private static String username = "";
	private static String password = "";
	private static ResourceBundle rb = ResourceBundle.getBundle("jdbc");
	
	static {
		try {
			url = rb.getString("url");
			username = rb.getString("username");
			password = rb.getString("password");
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	
	public static DB getInstance() {
		if (instance == null) {
			synchronized (DB.class){
				if (instance == null) {
					instance = new DB();
				}
			}
		}
		return instance;
	}

	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}
	
	

	/**
	 * 释放资源
	 * @param rs
	 * @param st
	 * @param conn
	 */
	public void free(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		}
	}
	
}
