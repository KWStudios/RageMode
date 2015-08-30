package org.kwstudios.play.ragemode.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MySQLConnector {

	private Connection connection = null;

	public MySQLConnector(String databaseURL, int port, String userName, String password) {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + databaseURL + ":" + Integer.toString(port) + "/",
					connectionProps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.connection = conn;

		System.out.println("RageMode connected successfully to the database!");
	}

	public void createDefaultTable(String databaseName) {
		Statement statement = null;
		String query = "adas";
		try {
			statement = connection.createStatement();
			statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return connection;
	}

}
