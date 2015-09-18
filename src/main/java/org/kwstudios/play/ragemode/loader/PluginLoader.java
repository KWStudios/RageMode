package org.kwstudios.play.ragemode.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.kwstudios.play.ragemode.bossbar.BossbarLib;
import org.kwstudios.play.ragemode.commands.CommandParser;
import org.kwstudios.play.ragemode.commands.StopGame;
import org.kwstudios.play.ragemode.database.MySQLConnector;
import org.kwstudios.play.ragemode.events.EventListener;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.locale.Messages;
import org.kwstudios.play.ragemode.metrics.Metrics;
import org.kwstudios.play.ragemode.signs.SignConfiguration;
import org.kwstudios.play.ragemode.signs.SignCreator;
import org.kwstudios.play.ragemode.statistics.YAMLStats;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.ConstantHolder;
import org.kwstudios.play.ragemode.toolbox.GetGames;
import org.kwstudios.play.ragemode.updater.Updater;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;

/*import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;*/

public class PluginLoader extends JavaPlugin {

	private static PluginLoader instance = null;
	private static MySQLConnector mySqlConnector = null;
	private static Messages messages = null;
	private static boolean holographicDiaplaysAvailable = false;
	// private ProtocolManager protocolManager;

	@Override
	public void onEnable() {
		super.onEnable();

		PluginLoader.instance = this;

		BossbarLib.setPluginInstance(this);
		
		if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			holographicDiaplaysAvailable = true;
		}

		/*
		 * protocolManager = ProtocolLibrary.getProtocolManager();
		 * protocolManager.addPacketListener( new PacketAdapter(this,
		 * ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO) {
		 * 
		 * @Override public void onPacketSending(PacketEvent event) { if
		 * (PlayerList.isPlayerPlaying(event.getPlayer().getUniqueId().toString(
		 * )) &&
		 * PlayerList.isGameRunning(PlayerList.getPlayersGame(event.getPlayer())
		 * )) { if
		 * (!TabAPI.allowedPackets.contains(event.getPacket().toString())) {
		 * event.setCancelled(true); } else {
		 * TabAPI.allowedPackets.remove(event.getPacket().toString()); } } } });
		 * new TabAPI(protocolManager);
		 */

		PluginDescriptionFile pluginDescriptionFile = getDescription();
		Logger logger = Logger.getLogger("Minecraft");

		logger.info(pluginDescriptionFile.getName() + " was loaded successfully! (Version: "
				+ pluginDescriptionFile.getVersion() + ")");
		// getConfig().options().copyDefaults(true);
		// saveConfig();
		new PlayerList(getConfig());
		new EventListener(this, getConfig());

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			// Failed to submit the stats :-(
		}

		new Updater(this, "http://mc.kwstudios.org/plugin-updater/ragemode.html");

		initStatistics();

		loadMessages();

		initStatusMessages();

		SignConfiguration.initSignConfiguration();

		String[] games = GetGames.getGameNames(getConfig());
		for (String game : games) {
			SignCreator.updateAllSigns(game);
		}

		saveConfig();

	}

	@Override
	public void onDisable() {

		super.onDisable();
		PluginDescriptionFile pluginDescriptionFile = getDescription();
		Logger logger = Logger.getLogger("Minecraft");

		// StopGame.stopAllGames(getConfig(), logger);

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				StopGame.stopAllGames(getConfig(), Logger.getLogger("Minecraft"));
			}
		});

		thread.start();
		while (thread.isAlive()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		logger.info(pluginDescriptionFile.getName() + " was unloaded successfully! (Version: "
				+ pluginDescriptionFile.getVersion() + ")");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(messages.NOT_A_PLAYER);
			return false;
		}

		Player player = (Player) sender;

		CommandParser commandParser = new CommandParser(player, command, label, args, getConfig());

		if (!commandParser.isCommand()) {
			return false;
		}

		saveConfig();
		return true;
	}

	public void initStatistics() {
		if (getConfig().isSet("settings.global.statistics")) {
			if (getConfig().isSet("settings.global.statistics.type")) {
				if (getConfig().getString("settings.global.statistics.type").equals("yaml"))
					YAMLStats.initS();

				if (getConfig().getString("settings.global.statistics.type").equals("mySQL")) {
					String databaseURL = getConfig().getString("settings.global.statistics.mySQL.url");
					int port = getConfig().getInt("settings.global.statistics.mySQL.port");
					String database = getConfig().getString("settings.global.statistics.mySQL.database");
					String username = getConfig().getString("settings.global.statistics.mySQL.username");
					String password = getConfig().getString("settings.global.statistics.mySQL.password");
					mySqlConnector = new MySQLConnector(databaseURL, port, database, username, password);
				}
			} else
				getConfig().set("settings.global.statistics.type", "yaml");

		} else {
			getConfig().set("settings.global.statistics.type", "yaml");
			getConfig().set("settings.global.statistics.mySQL",
					"to enable mySQL change the value of \"type\" to mySQL");
			getConfig().set("settings.global.statistics.mySQL.url", "put.your.databaseURL.here");
			getConfig().set("settings.global.statistics.mySQL.port", "put.your.databasePort.here");
			getConfig().set("settings.global.statistics.mySQL.database", "put.your.databaseName.here");
			getConfig().set("settings.global.statistics.mySQL.username", "put.your.databaseUsername.here");
			getConfig().set("settings.global.statistics.mySQL.password", "put.your.databasePassword.here");
		}
	}

	public void loadMessages() {
		HashMap<String, Boolean> fileNames = new HashMap<String, Boolean>();
		for (File file : listFilesForFolder(new File(getDataFolder(), "locale"))) {
			if (!fileNames.containsKey(file.getName())) {
				fileNames.put(file.getName(), true);
			}
		}

		if (!fileNames.containsKey("en.json")) {
			InputStream input = getClass().getResourceAsStream("/locale/en.json");
			try {
				File localeFolder = new File(getDataFolder(), "locale");
				if (!localeFolder.exists()) {
					localeFolder.mkdirs();
				}
				File enFile = new File(localeFolder, "en.json");
				Path destination = Paths.get(enFile.getAbsolutePath());
				Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!fileNames.containsKey("fr.json")) {
			InputStream input = getClass().getResourceAsStream("/locale/fr.json");
			try {
				File localeFolder = new File(getDataFolder(), "locale");
				if (!localeFolder.exists()) {
					localeFolder.mkdirs();
				}
				File enFile = new File(localeFolder, "fr.json");
				Path destination = Paths.get(enFile.getAbsolutePath());
				Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		fileNames.clear();
		for (File file : listFilesForFolder(new File(getDataFolder(), "locale"))) {
			if (!fileNames.containsKey(file.getName())) {
				fileNames.put(file.getName(), true);
				Bukkit.getConsoleSender().sendMessage(ConstantHolder.RAGEMODE_PREFIX + "Found \""
						+ ChatColor.DARK_PURPLE + file.getName() + ChatColor.RESET + "\" localization file.");
			}
		}

		String localeFile;
		if (getConfig().isSet("settings.global.locale")) {
			String setName = ConfigFactory.getString("settings.global", "locale", getConfig()) + ".json";
			if (setName != null && setName != "") {
				if (fileNames.containsKey(setName)) {
					localeFile = setName;
				} else {
					localeFile = "en.json";
				}
			} else {
				localeFile = "en.json";
			}
		} else {
			ConfigFactory.setString("settings.global", "locale", "en", getConfig());
			localeFile = "en.json";
		}

		File folder = new File(getDataFolder(), "locale");
		File file = new File(folder, localeFile);
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(input, "UTF8"));
			Gson gson = new Gson();
			messages = gson.fromJson(reader, Messages.class);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public List<File> listFilesForFolder(File folder) {
		List<File> fileList = new ArrayList<File>();
		if (!folder.exists()) {
			folder.mkdirs();
		}
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				for (File innerFile : listFilesForFolder(fileEntry)) {
					fileList.add(innerFile);
				}
			} else {
				fileList.add(fileEntry);
			}
		}
		return fileList;
	}

	public void initStatusMessages() {
		if (getConfig().isSet("settings.global.bossbar")) {
			if (ConfigFactory.getBoolean("settings.global", "bossbar", getConfig()) == null) {
				ConfigFactory.setBoolean("settings.global", "bossbar", false, getConfig());
			}
		} else {
			ConfigFactory.setBoolean("settings.global", "bossbar", false, getConfig());
		}

		if (getConfig().isSet("settings.global.actionbar")) {
			if (ConfigFactory.getBoolean("settings.global", "actionbar", getConfig()) == null) {
				ConfigFactory.setBoolean("settings.global", "actionbar", true, getConfig());
			}
		} else {
			ConfigFactory.setBoolean("settings.global", "actionbar", true, getConfig());
		}
	}

	public static PluginLoader getInstance() {
		return PluginLoader.instance;
	}

	public static MySQLConnector getMySqlConnector() {
		return mySqlConnector;
	}

	public static Messages getMessages() {
		return messages;
	}
	
	public static boolean getHolographicDisplaysAvailable() {
		return holographicDiaplaysAvailable;
	}

}
