package org.kwstudios.play.ragemode.achievements;

public class Achievement {

	public enum AchievementReason {
		FIRST_KILL, FIRST_STREAK_3, FIRST_STREAK_5, FIRST_STREAK_10, FIRST_STREAK_15, FIRST_BLOOD, FIRST_WIN, FARE_WELL, EVIL, SWORDMASTER, EXPLOSIVE, SURVIVOR, REVENGE, FAIR_PLAY, TAKTICAL_BACON, BOOM, WHY_ME, EASY_GOING, ONEHUNDRED 
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
