package org.kwstudios.play.ragemode.runtimeRPP;

import java.util.List;

import org.bukkit.Bukkit;
import org.kwstudios.play.ragemode.loader.PluginLoader;
import org.kwstudios.play.ragemode.scores.PlayerPoints;
import org.kwstudios.play.ragemode.scores.RetPlayerPoints;
import org.kwstudios.play.ragemode.statistics.MySQLStats;
import org.kwstudios.play.ragemode.statistics.YAMLStats;
import org.kwstudios.play.ragemode.toolbox.MergeSort;

public class RuntimeRPPManager {
	
	public static List<RetPlayerPoints> RuntimeRPPList;
	
	public static void getRPPListFromMySQL() {
		RuntimeRPPList = MySQLStats.getAllPlayerStatistics();
		MergeSort ms = new MergeSort();
		ms.sort(RuntimeRPPList);
	}
	
	public static void getRPPListFromYAML() {
		RuntimeRPPList = YAMLStats.getAllPlayerStatistics();
		MergeSort ms = new MergeSort();
		ms.sort(RuntimeRPPList);
	}
	
	public static RetPlayerPoints getRPPForPlayer(String sUUID) {
		RetPlayerPoints rpp = null;
		int i = 0;
		int imax = RuntimeRPPList.size();
		while(i < imax) {
			if(RuntimeRPPList.get(i).getPlayerUUID().equals(sUUID)) {
				rpp = RuntimeRPPList.get(i);
				rpp.setRank(i + 1);
				break;
			}
			i++;
		}
		return rpp;
	}
	
	public synchronized static void updatePlayerEntry(PlayerPoints pp) {
		RetPlayerPoints oldRPP = getRPPForPlayer(pp.getPlayerUUID());
		if(oldRPP == null) {
			int i = RuntimeRPPList.size() - 1;
			while(RuntimeRPPList.get(i).getPoints() < pp.getPoints()) {
				i--;
			}
			RetPlayerPoints newRPP = (RetPlayerPoints) pp;
			newRPP.setRank(i + 2);
			if(pp.isWinner())
				newRPP.setWins(1);
			else
				newRPP.setWins(0);
			
			if(pp.getDeaths() != 0)
				newRPP.setKD(((double)(pp.getKills()))/((double)(pp.getDeaths())));
			else
				newRPP.setKD(1.0d);
			
			newRPP.setGames(1);

			RuntimeRPPList.add(i + 1, newRPP);
		} else if(pp.getPoints() == oldRPP.getPoints())
			return;
		else if(pp.getPoints() > oldRPP.getPoints()) {
			int i = oldRPP.getRank() - 2;
			if(RuntimeRPPList.get(i).getPoints() < pp.getPoints()) {
				RuntimeRPPList.remove(i + 1);
				i--;
			}
			else
				return;
			while(RuntimeRPPList.get(i).getPoints() < pp.getPoints()) {
				i--;
				if(i < 0)
					i = 0;
					break;
			}
			RetPlayerPoints newRPP = (RetPlayerPoints) pp;
			if(pp.isWinner())
				newRPP.setWins(oldRPP.getWins() + 1);
			else
				newRPP.setWins(oldRPP.getWins());
			
			if(oldRPP.getDeaths() + pp.getDeaths() != 0)
				newRPP.setKD(((double)(pp.getKills() + oldRPP.getKills()))/((double)(pp.getDeaths() + oldRPP.getDeaths())));
			else
				newRPP.setKD(1.0d);
			
			newRPP.setGames(oldRPP.getGames() + 1);

			RuntimeRPPList.add(i + 1, newRPP);
		}
		else {
			int i = oldRPP.getRank();
			if(RuntimeRPPList.get(i).getPoints() > pp.getPoints()) {
				RuntimeRPPList.remove(i + 1);
			}
			else
				return;
			int imax = RuntimeRPPList.size();
			boolean last = false;
			while(RuntimeRPPList.get(i).getPoints() > pp.getPoints()) {
				i++;
				if(i > imax) {
					i = imax;
					last = true;
					break;
				}
			}
			RetPlayerPoints newRPP = (RetPlayerPoints) pp;
			if(pp.isWinner())
				newRPP.setWins(oldRPP.getWins() + 1);
			else
				newRPP.setWins(oldRPP.getWins());
			
			if(oldRPP.getDeaths() + pp.getDeaths() != 0)
				newRPP.setKD(((double)(pp.getKills() + oldRPP.getKills()))/((double)(pp.getDeaths() + oldRPP.getDeaths())));
			else
				newRPP.setKD(1.0d);
			
			newRPP.setGames(oldRPP.getGames() + 1);
			if(last)
				RuntimeRPPList.add(newRPP);
			else
				RuntimeRPPList.add(i, newRPP);
		}
	}
}
