package org.kwstudios.play.ragemode.statistics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.kwstudios.play.ragemode.database.MySQLConnector;
import org.kwstudios.play.ragemode.gameLogic.PlayerPoints;

public class MySQLStats {

	public static void addPlayerStatistics(PlayerPoints playerPoints, MySQLConnector mySQLConnector) {
		Connection connection = mySQLConnector.getConnection();

		Statement statement = null;
		String query = "SELECT * FROM rm_stats_players WHERE uuid LIKE " + playerPoints.getPlayerUUID() + ";";

		int oldKills = 0;
		int oldAxeKills = 0;
		int oldDirectArrowKills = 0;
		int oldExplosionKills = 0;
		int oldKnifeKills = 0;
		int oldDeaths = 0;
		int oldAxeDeaths = 0;
		int oldDirectArrowDeaths = 0;
		int oldExplosionDeaths = 0;
		int oldKnifeDeaths = 0;
		int oldWins = 0;
		int oldScore = 0;
		int oldGames = 0;

		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				oldKills = rs.getInt("kills");
				oldAxeKills = rs.getInt("axe_kills");
				oldDirectArrowKills = rs.getInt("direct_arrow_kills");
				oldExplosionKills = rs.getInt("explosion_kills");
				oldKnifeKills = rs.getInt("knife_kills");
				oldDeaths = rs.getInt("deaths");
				oldAxeDeaths = rs.getInt("axe_deaths");
				oldDirectArrowDeaths = rs.getInt("direct_arrow_deaths");
				oldExplosionDeaths = rs.getInt("explosion_deaths");
				oldKnifeDeaths = rs.getInt("knife_deaths");
				oldWins = rs.getInt("wins");
				oldScore = rs.getInt("score");
				oldGames = rs.getInt("games");
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong!");
			return;
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		int newKills = oldKills + playerPoints.getKills();
		int newAxeKills = oldAxeKills + playerPoints.getAxeKills();
		int newDirectArrowKills = oldDirectArrowKills + playerPoints.getDirectArrowKills();
		int newExplosionKills = oldExplosionKills + playerPoints.getExplosionKills();
		int newKnifeKills = oldKnifeKills + playerPoints.getKnifeKills();
		int newDeaths = oldDeaths + playerPoints.getDeaths();
		int newAxeDeaths = oldAxeDeaths + playerPoints.getAxeDeaths();
		int newDirectArrowDeaths = oldDirectArrowDeaths + playerPoints.getDirectArrowDeaths();
		int newExplosionDeaths = oldExplosionDeaths + playerPoints.getExplosionDeaths();
		int newKnifeDeaths = oldKnifeDeaths + playerPoints.getKnifeDeaths();
		int newWins = (playerPoints.isWinner()) ? oldWins++ : oldWins;
		int newScore = oldScore + playerPoints.getPoints();
		int newGames = oldGames++;
		double newKD = (newDeaths != 0) ? (newKills / newDeaths) : 1;

		statement = null;
		query = "REPLACE INTO rm_stats_players (kills, axe_kills, direct_arrow_kills, explosion_kills, knife_kills, deaths, axe_deaths, direct_arrow_deaths, explosion_deaths, knife_deaths, wins, score, games, kd) VALUES ("
				+ Integer.toString(newKills) + ", " + Integer.toString(newAxeKills) + ", "
				+ Integer.toString(newDirectArrowKills) + ", " + Integer.toString(newExplosionKills) + ", "
				+ Integer.toString(newKnifeKills) + ", " + Integer.toString(newDeaths) + ", "
				+ Integer.toString(newAxeDeaths) + ", " + Integer.toString(newDirectArrowDeaths) + ", "
				+ Integer.toString(newExplosionDeaths) + ", " + Integer.toString(newKnifeDeaths) + ", "
				+ Integer.toString(newWins) + ", " + Integer.toString(newScore) + ", " + Integer.toString(newGames)
				+ ", " + Double.toString(newKD) + ");";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
