package com.mechatech84.horsehome.commands;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mechatech84.horsehome.FileManager;
import com.mechatech84.horsehome.NewHorseHome;

public class DelHorseHomeCommand implements CommandExecutor {

	NewHorseHome plugin;
	FileManager manager;

	public DelHorseHomeCommand(NewHorseHome plugin, FileManager manager) {
		this.plugin = plugin;
		this.manager = manager;

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().toString().equalsIgnoreCase("delhorsehome")
				&& sender.hasPermission("horsehome.delhorsehome")) {
			if (!(sender instanceof Player)) {
				plugin.getLogger().info("The console cannot use HorseHome!");
				return true;
			}

			Player player = (Player) sender;
			String altHorseHome = "home";
			int allowedHomes = plugin.manageAllowedHomes(player);

			/* //TODO Fix more homes than allowed! 
			  	if (totalHomes > allowedHomes) {
								player.sendMessage(RED
										+ "You have more homes than allowed!");
								String[] homesArray = manager.getTotalHomesArray(player
										.getName());
								if (allowedHomes == 0) {
									for (String home : homesArray) {
										if (totalHomes == allowedHomes) {
											player.sendMessage(RED + "Done!");
											return true;
										}
										manager.deleteHorseHomeFile(player.getName(), home);

									}
									return true;
								} else if (allowedHomes > 0) {
									int deletedHomes = 0;
									String[] reversedHomes = homesArray.clone();
									Collections.reverse(Arrays.asList(reversedHomes));
									for (String home : reversedHomes) {
										if (totalHomes == allowedHomes) {
											player.sendMessage(RED + "Deleted "
													+ GOLD + deletedHomes
													+ RED + " homes.");
											return true;
										} else {
											manager.deleteHorseHomeFile(player.getName(),
													home);
											deletedHomes++;
											continue;
										}
									}
								}
								return false;
							}
			*/

			if (allowedHomes == 1) {
				if (manager.checkForHorseHomeFile(player.getName(),
						altHorseHome)) {
					manager.deleteHorseHomeFile(player.getName(), altHorseHome);
					player.sendMessage(GREEN + "Horse home " + GOLD
							+ altHorseHome + GREEN + " deleted.");
					return true;
				}
				player.sendMessage(RED
						+ "You don't have a horse home to delete!");
				return true;

			}

			else if (allowedHomes > 1) {
				if (args.length > 1) {
					player.sendMessage(GRAY
							+ "Needless arguments, ignoring command.");
					return true;
				}
				String[] totalHomesArray = manager.getTotalHomesArray(player
						.getName());
				if (totalHomesArray.length == 0) {
					player.sendMessage(RED + "You don't have any horse homes!");
					return true;
				}
				ArrayList<String> allHomes = new ArrayList<String>(
						Arrays.asList(totalHomesArray));
				StringBuilder stringBuilder = plugin.getHomesString(player);
				if (args.length == 0) {
					player.sendMessage(RED
							+ "You'll have to define which home you want to delete! \n Current homes: "
							+ GOLD + stringBuilder + RED + ".");
					return true;

				} else if (args.length == 1) {
					boolean worked = false;
					for (String home : allHomes) {
						if (args[0].equalsIgnoreCase(home)) {
							manager.deleteHorseHomeFile(player.getName(), home);
							player.sendMessage(GREEN + "Horse home " + GOLD
									+ home + GREEN + " was deleted.");
							worked = true;
							return true;
						}

					}
					if (!worked) {
						player.sendMessage(RED
								+ "You must insert a valid horse home name! \n Current homes: "
								+ GOLD + plugin.getHomesString(player));
						return true;
					}
					return false;

				}
			} else {
				player.sendMessage(RED
						+ "You are not allowed to have any horse homes!");
				return true;
			}

			return false;
		}
		return false;

	}
}
