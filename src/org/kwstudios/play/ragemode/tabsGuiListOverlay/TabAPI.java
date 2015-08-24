package org.kwstudios.play.ragemode.tabsGuiListOverlay;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.conversations.Conversation.ConversationState;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.mojang.authlib.GameProfile;

public class TabAPI {
	private static ProtocolManager protocolManager;
	
	public static List<String> allowedPackets = new ArrayList<String>();
	public TabAPI(ProtocolManager pMan) {
		protocolManager = pMan;
	}
	
	private static EnumGamemode convertGameMode(GameMode gm) {
		if(gm.equals(GameMode.SURVIVAL))
			return EnumGamemode.SURVIVAL;
		
		if(gm.equals(GameMode.CREATIVE))
			return EnumGamemode.CREATIVE;
		
		if(gm.equals(GameMode.ADVENTURE))
			return EnumGamemode.ADVENTURE;
		
		if(gm.equals(GameMode.SPECTATOR))
			return EnumGamemode.SPECTATOR;
		
		return EnumGamemode.NOT_SET;
		
	}
	
	public static void setTabGuiListOverLayForPlayers(List<String> players) {
		//TODO Tab contents

		Collection<Player> op = (Collection<Player>) Bukkit.getOnlinePlayers();
		Player[] oP = new Player[1];
		Player[] oP2 = op.toArray(oP);

		PacketContainer remove = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
		remove.getModifier().write(0, EnumPlayerInfoAction.REMOVE_PLAYER);
		
		net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo ff = new net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo();
		net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData datatoRemove;
		
		List<net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData> toRemove = new ArrayList<net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData>();
		int d = 0;
		int dmax = oP2.length;
		while(d < dmax) {
			datatoRemove = ff.new PlayerInfoData(new GameProfile(oP2[d].getUniqueId(), oP2[d].getName()), 1, EnumGamemode.NOT_SET, ChatSerializer.a(oP2[d].getName()));/*(new WrappedGameProfile(oP2[d].getUniqueId(), oP2[d].getName()), 420, NativeGameMode.NOT_SET, WrappedChatComponent.fromText(oP2[d].getName())));*/
			toRemove.add(datatoRemove);
			
			d++;
		}
		
		int c = 0;
		int cmax = players.size();
		
		remove.getModifier().write(1, toRemove);
		allowedPackets.add(remove.toString());
		while(c < cmax) {
			try {
				protocolManager.sendServerPacket(Bukkit.getPlayer(UUID.fromString(players.get(c))), remove);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			c++;
		}
		
		/*
		int i = 0;
		int imax = oP2.length;
		
		int n = 0;
		int nmax = players.size();
//		Bukkit.broadcastMessage("Starting removal: " + nmax + imax);
		while(n < nmax) {
			while(i < imax) {
				WorldServer wS = ((CraftWorld) Bukkit.getPlayer(UUID.fromString(players.get(n))).getWorld()).getHandle();
				PacketPlayOutPlayerInfo capket = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(), wS, new GameProfile(UUID.randomUUID(), "HERBERT"), new PlayerInteractManager(wS)));
				((CraftPlayer)Bukkit.getPlayer(UUID.fromString(players.get(n)))).getHandle().playerConnection.sendPacket(capket);
				
				Bukkit.broadcastMessage("Removed: " + oP2[i].getName());
				PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)oP2[i]).getHandle());
				
				Bukkit.broadcastMessage("for Player: " + Bukkit.getPlayer(UUID.fromString(players.get(n))).getName());
				((CraftPlayer)Bukkit.getPlayer(UUID.fromString(players.get(n)))).getHandle().playerConnection.sendPacket(packet);
				

				
				i++;
			}
			n++;
		}*/
	}
	
	public static void updateTabGuiListOverlayForGame(String game) {
		//TODO update Tab contents
	}
	
	public static void removeTabGuiListOverlayForPlayer(Player player) {
		//TODO remove Tab contents and set the real ones
		Collection<Player> op = (Collection<Player>) Bukkit.getOnlinePlayers();
		Player[] oP = new Player[1];
		Player[] oP2 = op.toArray(oP);

		PacketContainer restore = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
		restore.getModifier().write(0, EnumPlayerInfoAction.ADD_PLAYER);
		
		net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo ff = new net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo();
		net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData datatoRestore;
		
		List<net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData> toRestore = new ArrayList<net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData>();
		
		int d = 0;
		int dmax = oP2.length;
		while(d < dmax) {
			datatoRestore = ff.new PlayerInfoData(new GameProfile(oP2[d].getUniqueId(), oP2[d].getName()), 1, convertGameMode(oP2[d].getGameMode()), ChatSerializer.a(oP2[d].getName()));/*(new WrappedGameProfile(oP2[d].getUniqueId(), oP2[d].getName()), 420, NativeGameMode.NOT_SET, WrappedChatComponent.fromText(oP2[d].getName())));*/
			toRestore.add(datatoRestore);
			
			d++;
		}
		
		restore.getModifier().write(1, toRestore);
		allowedPackets.add(restore.toString());
			try {
				protocolManager.sendServerPacket(player, restore);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
	}
	

}
