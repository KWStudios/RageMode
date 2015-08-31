package org.kwstudios.play.ragemode.gameLogic;

public class RetPlayerPoints extends PlayerPoints {

	private int wins = 0;
	private int games = 0;
	private double kd = 0d;
	
	public RetPlayerPoints(String playerUUID) {
		super(playerUUID);
	}
	
	public void setWins(int w) {
		this.wins = w;		
	}
	
	public int getWins() {
		return this.wins;
	}
	
	public void setGames(int g) {
		this.games = g;
	}
	
	public int getGames() {
		return this.games;
	}
	
	public void setKD(double k) {
		this.kd = k;
	}
	
	public double getKD() {
		return this.kd;
	}
}
