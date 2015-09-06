package org.kwstudios.play.ragemode.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.kwstudios.play.ragemode.toolbox.ConstantHolder;

public class MySQLConnector {

	private Connection connection = null;

	public MySQLConnector(String databaseURL, int port, String database, String userName, String password) {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);

		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://" + databaseURL + ":" + Integer.toString(port) + "/" + database, connectionProps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		connection = conn;

		// System.out.println("RageMode connected successfully to the
		// database!");

		createDefaultTable();
	}

	public void createDefaultTable() {
		if (connection != null) {
			Statement statement = null;
			String query = "CREATE TABLE IF NOT EXISTS `rm_stats_players` (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY , name VARCHAR(255) , uuid VARCHAR(255) , kills INT(11) , axe_kills INT(11) , direct_arrow_kills INT(11) , explosion_kills INT(11) , knife_kills INT(11) , deaths INT(11) , axe_deaths INT(11) , direct_arrow_deaths INT(11) , explosion_deaths INT(11) , knife_deaths INT(11) , wins INT(11) , score INT(11) , games INT(11) , kd DOUBLE, UNIQUE(uuid));";
			try {
				statement = connection.createStatement();
				statement.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("The table was created successfully!");
		} else {
			System.out.println(ConstantHolder.RAGEMODE_PREFIX
					+ "We could not reach the given database url or the authentication failed. Please recheck the config.yml.");
		}
	}

	public Connection getConnection() {
		return connection;
	}

}
