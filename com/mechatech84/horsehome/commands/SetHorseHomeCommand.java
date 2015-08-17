package com.mechatech84.horsehome.commands;

import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RED;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mechatech84.horsehome.ConfigAccessor;
import com.mechatech84.horsehome.FileManager;
import com.mechatech84.horsehome.NewHorseHome;

public class SetHorseHomeCommand implements CommandExecutor {

	NewHorseHome plugin;
	FileManager manager;

	public SetHorseHomeCommand(NewHorseHome plugin, FileManager manager) {
		this.plugin = plugin;
		this.manager = manager;

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().toString().equalsIgnoreCase("sethorsehome")
				&& sender.hasPermission("horsehome.sethorsehome")) {
			if (!(sender instanceof Player)) {
				plugin.getLogger().info("The console cannot use HorseHome!");
				return true;
			}
			Player player = (Player) sender;
			String altHorseHome = "home";
			int allowedHomes = plugin.manageAllowedHomes(player);
			int totalHomes = manager.getTotalHomesNumber(player.getName());
			if (allowedHomes > 1) { // If the player can have many homes, do these below.
				if (totalHomes < allowedHomes) {
					if (args.length == 0) { // If there are 0 arguments
						/*String input1 = altHorseHome; // Horse home name.

						ConfigAccessor config = doFileManagement(
								player.getName(), input1);
						assignHorseHomeProperties(player, config, input1);
						*/
						player.sendMessage(RED
								+ "You'll have to name your horse home!");

						return true;

					} else if (args.length == 1) {
						String input1 = args[0].toLowerCase(); // Horse home name
						if (input1.equalsIgnoreCase("list")) {
							player.sendMessage(RED
									+ "Horse home name cannot be equal to "
									+ DARK_RED + "list" + RED + "!");
							return true;
						} /*else if (input1.equalsIgnoreCase("properties")) {
							player.sendMessage(RED
									+ "Horse home name cannot be equal to "
									+ DARK_RED + "properties"
									+ RED + "!");
							}*/
						if (manager.checkForHorseHomeFile(player.getName(),
								input1)) {
							player.sendMessage(RED
									+ "A home already exists with that name! Overwriting it!");
						}
						ConfigAccessor config = plugin.doFileManagement(
								player.getName(), input1);
						plugin.assignHorseHomeProperties(player, config, input1);
						return true;

					}

				} else { // if player has set more homes than allowed
					player.sendMessage(RED + "You are not allowed more than "
							+ GOLD + allowedHomes + RED + " homes!");
					return true;
				}
			} else if (allowedHomes == 1) { // Else, if allowed homes is equal to 1
				//if (totalHomes <= allowedHomes) {
				/*if (manager.checkForHorseHomeFile(player.getName(),
						altHorseHome)) {
					player.sendMessage(RED
							+ "You already have a horse home!");
					return true;
				}*/
				if (args.length == 0) {
					String input1 = altHorseHome;

					ConfigAccessor config = plugin.doFileManagement(
							player.getName(), input1);
					plugin.assignHorseHomeProperties(player, config, input1);
					return true;

				} else if (args.length == 1) {
					player.sendMessage(RED
							+ "You don't have permission for mutliple homes, trying "
							+ GOLD + "home" + RED + ".");
					String input1 = altHorseHome;

					ConfigAccessor config = plugin.doFileManagement(
							player.getName(), input1);
					plugin.assignHorseHomeProperties(player, config, input1);
					return true;
				}
				/*} else { // if player has set more homes than allowed
					player.sendMessage(RED
							+ "You are not allowed more than "
							+ GOLD + allowedHomes
							+ RED + " homes!");
					return true;
				}*/
			} else if (allowedHomes == 0) {
				player.sendMessage(RED
						+ "You are not allowed to set any horse homes!");
				return true;
			}
			if (args.length >= 2) {
				player.sendMessage(GRAY
						+ "Needless arguments, ignoring command.");
				return true;
			}

			return false;
		}
		return false;

	}
}
