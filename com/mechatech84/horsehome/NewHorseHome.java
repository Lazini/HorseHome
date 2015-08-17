package com.mechatech84.horsehome;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mechatech84.horsehome.commands.DelHorseHomeCommand;
import com.mechatech84.horsehome.commands.HorseHomeCommand;
import com.mechatech84.horsehome.commands.HorseHomesCommand;
import com.mechatech84.horsehome.commands.SetHorseHomeCommand;

public class NewHorseHome extends JavaPlugin {

	private static NewHorseHome instance;
	private FileManager manager;

	@Override
	public void onEnable() {
		super.onEnable();
		instance = this;
		manager = new FileManager();
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		getCommand("sethorsehome").setExecutor(
				new SetHorseHomeCommand(this, manager));
		getCommand("horsehome")
				.setExecutor(new HorseHomeCommand(this, manager));
		getCommand("delhorsehome").setExecutor(
				new DelHorseHomeCommand(this, manager));
		getCommand("horsehomes").setExecutor(
				new HorseHomesCommand(this, manager));
	}

	@Override
	public void onDisable() {
		super.onDisable();
		this.saveConfig();
	}

	/**
	 * Returns a string containing the homes the player you specify. Use it only
	 * when you want to tell the player their homes.
	 */
	public StringBuilder getHomesString(Player player) {
		StringBuilder stringBuilder = new StringBuilder();
		String[] totalHomesArray = manager.getTotalHomesArray(player.getName());

		for (String home : totalHomesArray) {
			stringBuilder.append(home);
			stringBuilder.append(", ");
		}
		int lastIndexOfComma = stringBuilder.lastIndexOf(", ");
		try {
			stringBuilder.delete(lastIndexOfComma, stringBuilder.length());
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return stringBuilder;
	}

	/**
	 * Assigns the XYZ coords at the player's horse home, as well as the world's
	 * name, and then saves the config before notifying the player.
	 */
	public void assignHorseHomeProperties(Player player, ConfigAccessor config,
			String horseHome) {
		config.getConfig().set("x", (int) player.getLocation().getX()); // Save x coord.
		config.getConfig().set("y", (int) player.getLocation().getY()); // Save y coord.
		config.getConfig().set("z", (int) player.getLocation().getZ()); // Save z coord.
		config.getConfig().set("world", player.getWorld().getName()); // Save world name.
		config.saveConfig();
		player.sendMessage(GREEN + "Horse home set! \n Horse home name: "
				+ GOLD + horseHome);
	}

	public ConfigAccessor doFileManagement(String playerName,
			String horseHomeName) {
		ConfigAccessor config = new ConfigAccessor(this,
				manager.getProperFolderPath(playerName) + horseHomeName
						+ ".yml");
		if (!manager.checkForHorseHomeFile(playerName, horseHomeName)) {
			config = manager.generateHorseHomeForPlayer(playerName,
					horseHomeName);
		}
		return config;
	}

	/** Returns a number based on the rank of a player. */
	public int manageAllowedHomes(Player player) {
		this.reloadConfig();
		List<String> rankList = this.getConfig().getStringList("HorseHomes");
		int allowedHomes = 1;

		for (String rankAndNumber : rankList) {
			String[] rankAndNumberArray = rankAndNumber.split(" - ");
			String rank = rankAndNumberArray[0];
			if (player.hasPermission("horsehomes." + rank)) {
				int number = Integer.valueOf(rankAndNumberArray[1]);
				allowedHomes = number;

				break;
			}
		}
		return allowedHomes;
	}

	public static NewHorseHome getInstance() {
		return instance;
	}

}
