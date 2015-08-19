package org.kwstudios.play.ragemode.gameLogic;

public class PlayerPoints implements Comparable<PlayerPoints> {

	private String playerUUID;
	private Integer points;
	private int kills;
	private int deaths;

	public PlayerPoints(String playerUUID, int points, int kills, int deaths) {
		this.playerUUID = playerUUID;
		this.points = points;
		this.kills = kills;
		this.deaths = deaths;
	}

	public String getPlayerUUID() {
		return playerUUID;
	}

	public Integer getPoints() {
		return points;
	}

	public int getKills() {
		return kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	@Override
	public int compareTo(PlayerPoints anotherPlayerPoints) {
		return anotherPlayerPoints.getPoints().compareTo(this.points);
	}

}
