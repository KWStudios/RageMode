package org.kwstudios.play.ragemode.scores;

public class PlayerPoints implements Comparable<PlayerPoints> {

	private String playerUUID;
	private int kills = 0;
	private int axeKills = 0;
	private int directArrowKills = 0;
	private int explosionKills = 0;
	private int knifeKills = 0;
	private int deaths = 0;
	private int axeDeaths = 0;
	private int directArrowDeaths = 0;
	private int explosionDeaths = 0;
	private int knifeDeaths = 0;
	private Integer points = 0;
	private boolean isWinner = false;

	public PlayerPoints(String playerUUID) {
		this.playerUUID = playerUUID;
	}

	public String getPlayerUUID() {
		return playerUUID;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getAxeKills() {
		return axeKills;
	}

	public void setAxeKills(int axeKills) {
		this.axeKills = axeKills;
	}

	public int getDirectArrowKills() {
		return directArrowKills;
	}

	public void setDirectArrowKills(int directArrowKills) {
		this.directArrowKills = directArrowKills;
	}

	public int getExplosionKills() {
		return explosionKills;
	}

	public void setExplosionKills(int explosionKills) {
		this.explosionKills = explosionKills;
	}

	public int getKnifeKills() {
		return knifeKills;
	}

	public void setKnifeKills(int knifeKills) {
		this.knifeKills = knifeKills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getAxeDeaths() {
		return axeDeaths;
	}

	public void setAxeDeaths(int axeDeaths) {
		this.axeDeaths = axeDeaths;
	}

	public int getDirectArrowDeaths() {
		return directArrowDeaths;
	}

	public void setDirectArrowDeaths(int directArrowDeaths) {
		this.directArrowDeaths = directArrowDeaths;
	}

	public int getExplosionDeaths() {
		return explosionDeaths;
	}

	public void setExplosionDeaths(int explosionDeaths) {
		this.explosionDeaths = explosionDeaths;
	}

	public int getKnifeDeaths() {
		return knifeDeaths;
	}

	public void setKnifeDeaths(int knifeDeaths) {
		this.knifeDeaths = knifeDeaths;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public boolean isWinner() {
		return isWinner;
	}

	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}

	@Override
	public int compareTo(PlayerPoints anotherPlayerPoints) {
		return anotherPlayerPoints.getPoints().compareTo(this.points);
	}

}
