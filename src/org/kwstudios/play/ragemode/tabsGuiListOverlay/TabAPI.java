package org.kwstudios.play.ragemode.tabsGuiListOverlay;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.mojang.authlib.GameProfile;

public class TabAPI {
	private static ProtocolManager protocolManager;
	
	public TabAPI(ProtocolManager pMan) {
		protocolManager = pMan;
	}
	public static void setTabGuiListOverLayForPlayers(List<String> players) {
		//TODO Tab contents
		/*WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
		info.setPlayerName("GAYY");
		info.setPing((short) 10);
		
		int i = 0;
		int imax = players.size();
		while(i < imax) {
			info.sendPacket(Bukkit.getPlayer(UUID.fromString(players.get(i))));	
			
			i++;
		}*/

//		PlayerInfoData gre = new PlayerInfoData(new WrappedGameProfile(UUID.randomUUID(), "Stuhl"), 1, Bukkit.getPlayer(UUID.fromString(players.get(0))).getGameMode(), new WrappedChatComponent());
//		new Player
		PacketContainer playerRemovefromTab = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);

		playerRemovefromTab.getModifier().write(0, EnumPlayerInfoAction.REMOVE_PLAYER);
		
		int i = 0;
		Collection<Player> op = (Collection<Player>) Bukkit.getOnlinePlayers();
		int imax = op.size();
		Player[] oP = new Player[1];
		op.toArray(oP);
		byte[] baUuid = new byte[16];
		List hhu = new ArrayList();
		while(i < imax) {
			
			PacketPlayOutPlayerInfo.PlayerInfoData data = new PacketPlayOutPlayerInfo.PlayerInfoData(new GameProfile(oP[i].getUniqueId(), ""), 2, oP[i].getGameMode(), null);
			PacketPlayOutPlayerInfo.PlayerInfoData data = new PacketPlayOutPlayerInfo.PlayerInfoData()
			i++;
		}
		playerRemovefromTab.getModifier().write(1, data);
		try {
			protocolManager.sendServerPacket(Bukkit.getPlayer(UUID.fromString(players.get(0))), playerRemovefromTab);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
//		playerRemovefromTab.getS
	}
	
	public static void updateTabGuiListOverlayForGame(String game) {
		//TODO update Tab contents
	}
	
	public static void removeTabGuiListOverlayForPlayer(Player player) {
		//TODO remove Tab contents and set the real ones
	}
}
