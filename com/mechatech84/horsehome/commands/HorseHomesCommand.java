package com.mechatech84.horsehome.commands;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mechatech84.horsehome.FileManager;
import com.mechatech84.horsehome.NewHorseHome;

public class HorseHomesCommand implements CommandExecutor {

	NewHorseHome plugin;
	FileManager manager;

	public HorseHomesCommand(NewHorseHome plugin, FileManager manager) {
		this.plugin = plugin;
		this.manager = manager;

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (!(sender instanceof Player)) {
			plugin.getLogger().info("The console cannot use HorseHome!");
			return true;
		}

		Player player = (Player) sender;
		int allowedHomes = plugin.manageAllowedHomes(player);
		int totalHomes = manager.getTotalHomesNumber(player.getName());
		int homesLeft = allowedHomes - totalHomes;

		if (cmd.getName().toString().equalsIgnoreCase("horsehomes")
				&& player.hasPermission("horsehome.horsehome")) {
			/*if (args[0].equalsIgnoreCase("properties"))
				return true; // TODO Fix this as it returns an error.
			*/
			if (args.length > 0) {
				player.sendMessage(GRAY + "Needless arguments, ingoring them.");
			}
			String[] totalHomesArray = manager.getTotalHomesArray(player
					.getName());
			if (totalHomesArray.length == 0) {
				player.sendMessage(GREEN + "Total horse homes allowed: " + GOLD
						+ allowedHomes + GREEN + ". \n You don't have " + RED
						+ "any" + GREEN
						+ " horse homes. \n Homes left to set: " + GOLD
						+ homesLeft + GREEN + ".");
				return true;
			}
			StringBuilder stringBuilder = plugin.getHomesString(player);

			player.sendMessage(GREEN + "Total horse homes allowed: " + GOLD
					+ allowedHomes + GREEN + ". \n Current homes: " + RED
					+ stringBuilder + GREEN + ". \n Homes left to set: " + GOLD
					+ homesLeft + GREEN + ".");
			return true;
		}
		return false;

	}
}
