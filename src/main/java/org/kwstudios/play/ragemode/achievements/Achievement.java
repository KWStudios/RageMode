package org.kwstudios.play.ragemode.achievements;

public class Achievement {

	public enum AchievementReason {
		FIRST_KILL, FIRST_STREAK, FIRST_BLOOD, FIRST_WIN
	}

	private String name;
	private int points;
	private AchievementReason reason;

	public Achievement(String name, int points, AchievementReason reason) {
		this.name = name;
		this.points = points;
		this.reason = reason;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public AchievementReason getReason() {
		return reason;
	}

	public void setReason(AchievementReason reason) {
		this.reason = reason;
	}

}
