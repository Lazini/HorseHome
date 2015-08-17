package com.mechatech84.horsehome.commands;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import com.mechatech84.horsehome.ConfigAccessor;
import com.mechatech84.horsehome.FileManager;
import com.mechatech84.horsehome.NewHorseHome;

public class HorseHomeCommand implements CommandExecutor {
	NewHorseHome plugin;
	FileManager manager;

	public HorseHomeCommand(NewHorseHome plugin, FileManager manager) {
		this.plugin = plugin;
		this.manager = manager;

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().toString().equalsIgnoreCase("horsehome")
				&& sender.hasPermission("horsehome.horsehome")) {

			if (!(sender instanceof Player)) {
				plugin.getLogger().info("The console cannot use HorseHome!");
				return true;
			}

			Player player = (Player) sender;
			String altHorseHome = "home";
			int allowedHomes = plugin.manageAllowedHomes(player);
			int totalHomes = manager.getTotalHomesNumber(player.getName());
			/*if (args[0].equalsIgnoreCase("properties") && args.length > 0)
				return true; // TODO Fix this as it returns an error.
			*/
			if (totalHomes > 0) {
				if (args.length == 1) {
					String input1 = args[0].toLowerCase();
					if (input1.equalsIgnoreCase("list")) {
						player.sendMessage(GREEN + "Current homes: " + GOLD
								+ plugin.getHomesString(player));
						return true;
					}
				}
			} else if (totalHomes == 0) {
				player.sendMessage(RED + "You don't have any horse homes!");
				return true;
			}

			if (player.getVehicle() == null) {
				player.sendMessage(RED + "You must be on your horse!");
				return true;
			}

			else if (player.getVehicle().getType() == EntityType.HORSE) {
				Horse horse = (Horse) player.getVehicle();
				if (allowedHomes > 1) {
					if (args.length == 1) {
						String input1 = args[0].toLowerCase();
						if (input1.equalsIgnoreCase("list")) {
							player.sendMessage(GREEN + "Current homes: " + GOLD
									+ plugin.getHomesString(player));
							return true;
						}
						String[] totalHomesArray = manager
								.getTotalHomesArray(player.getName());
						if (totalHomesArray.length == 0) {
							player.sendMessage(RED
									+ "You do not have any horse homes!");
							return true;
						}
						boolean worked = false;
						for (String home : totalHomesArray) {
							if (home.equalsIgnoreCase(input1)) {
								ConfigAccessor config = plugin
										.doFileManagement(player.getName(),
												home);
								int x = config.getConfig().getInt("x");
								int y = config.getConfig().getInt("y");
								int z = config.getConfig().getInt("z");
								String worldName = config.getConfig()
										.getString("world");
								World world = plugin.getServer().getWorld(
										worldName);
								if (!player.getWorld().getName()
										.equalsIgnoreCase(world.getName())) {
									player.sendMessage(RED
											+ "Destination must be the same as current world!");
									return true;
								}
								Location horseHomeLocation = new Location(
										world, x, y, z);

								horse.eject();
								Chunk chunk = horseHomeLocation.getChunk();
								if (!chunk.isLoaded()) {
									horseHomeLocation.getWorld()
											.refreshChunk(
													horseHomeLocation
															.getChunk().getX(),
													horseHomeLocation
															.getChunk().getZ());
									horse.teleport(horseHomeLocation);
								} else {
									horse.teleport(horseHomeLocation);
								}
								player.sendMessage(GREEN + "Horse sent to "
										+ RED + home + GREEN + ".");
								worked = true;
								return true;
							}
						}
						if (!worked) {
							StringBuilder homesArrayString = plugin
									.getHomesString(player);
							player.sendMessage(RED
									+ "You need to insert a valid home name! \n Homes: "
									+ GOLD + homesArrayString);
							return true;
						}
					} else if (args.length == 0) {
						StringBuilder homesArrayString = plugin
								.getHomesString(player);
						player.sendMessage(GREEN + "Current horse homes: "
								+ RED + homesArrayString + GREEN + ".");
						return true;

					} else if (args.length > 1) {
						player.sendMessage(GRAY
								+ "Needless arguments, ignoring command.");
						return true;

					}
				}

				else if (allowedHomes == 1) {
					if (args.length > 0) {
						player.sendMessage(GRAY
								+ "Needless arguments, ignoring command.");
					}
					String input1 = altHorseHome;
					String[] totalHomesArray = manager
							.getTotalHomesArray(player.getName());
					if (totalHomesArray.length == 0) {
						player.sendMessage(RED
								+ "You do not have any horse homes!");
						return true;
					}
					String home = totalHomesArray[0];
					if (home.equalsIgnoreCase(input1)) {
						ConfigAccessor config = plugin.doFileManagement(
								player.getName(), home);
						int x = config.getConfig().getInt("x");
						int y = config.getConfig().getInt("y");
						int z = config.getConfig().getInt("z");
						String worldName = config.getConfig()
								.getString("world");
						World world = plugin.getServer().getWorld(worldName);
						Location horseHomeLocation = new Location(world, x, y,
								z);

						horse.eject();
						Chunk chunk = horseHomeLocation.getChunk();
						if (!chunk.isLoaded()) {
							horseHomeLocation.getWorld().refreshChunk(
									horseHomeLocation.getChunk().getX(),
									horseHomeLocation.getChunk().getZ());
							horse.teleport(horseHomeLocation);
						} else {
							horse.teleport(horseHomeLocation);
						}
						player.sendMessage(GREEN + "Horse sent to " + RED
								+ home + GREEN + ".");

						return true;
					}

				} else {
					player.sendMessage(RED
							+ "You are not allowed to have any horse homes!");
					return true;
				}
			} else {
				player.sendMessage(RED + "You must be on a horse!");
				return true;
			}
		}
		return false;
	}

}
