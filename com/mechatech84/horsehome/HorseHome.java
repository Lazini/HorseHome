package com.mechatech84.horsehome;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HorseHome extends JavaPlugin {

	public final Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {

		this.getConfig().options().copyDefaults(false);
		this.saveConfig();

		/*
		 * try { //metrics Metrics metrics = new Metrics(this); metrics.start();
		 * } catch (IOException error5) { // Failed to submit the stats :-( }
		 * //end metrics
		 */
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player player = (Player) sender;

		ConfigAccessor playerConfig = new ConfigAccessor(this, "data"
				+ File.separator + "players" + File.separator
				+ player.getName() + ".yml");

		/*
		 if (commandLabel.equalsIgnoreCase("horsestats")){ if
		 (player.getVehicle().getType() == EntityType.HORSE) { LivingEntity
		 horse = (LivingEntity)player.getVehicle(); AttributeInstance
		 attributes = ((EntityInsentient)((CraftLivingEntity)
		 horse).getHandle()).getAttributeInstance(GenericAttributes.d);
		 CraftHorse cHorse = (CraftHorse)horse; NBTTagCompound compound = new
		 NBTTagCompound(); cHorse.getHandle().b(compound); double jumpStrength
		 = -1; double speed = -1; NBTTagList list =
		 compound.getList("Attributes"); for(int i = 0; i < list.size() ;
		 i++){ NBTBase base = list.get(i); //id 10 means it is a
		 NBTTagCompound if(base.getTypeId() == 10){ NBTTagCompound
		 attrCompound = (NBTTagCompound)base;
		 
		 if(base.toString().contains("horse.jumpStrength")){ jumpStrength =
		 attrCompound.getDouble("Base"); } else
		 if(base.toString().contains("generic.movementSpeed")){ speed =
		 attrCompound.getDouble("Base"); } } } attributes.getValue();
		 player.sendMessage(ChatColor.GREEN + "Max Health: " +
		 horse.getMaxHealth()); player.sendMessage(ChatColor.GREEN +
		 "Max Jump: " + jumpStrength); player.sendMessage(ChatColor.GREEN +
		 "Max Speed: " + speed); } else { player.sendMessage(ChatColor.GREEN +
		 "You must be on your horse!"); } }
		 */

		if (commandLabel.equalsIgnoreCase("delhorsehome")) {
			String horseHomeName = "home";
			String inputOne;
			try {
				inputOne = args[0].toLowerCase();
			} catch (ArrayIndexOutOfBoundsException e) {
				inputOne = "home";
			}
			if (inputOne.equalsIgnoreCase("list")) {
				inputOne = "home";
			}
			horseHomeName = inputOne;
			ArrayList<String> arrayList = null;
			try {
				arrayList = (ArrayList<String>) playerConfig.getConfig()
						.getStringList("list");
			} catch (Throwable e) {

			}
			playerConfig.getConfig().set(horseHomeName, null);
			for (String item : arrayList) {
				if (item.equalsIgnoreCase(horseHomeName)) {
					arrayList.remove(item);
					playerConfig.getConfig().set("list", arrayList);
					player.sendMessage(ChatColor.GREEN + horseHomeName
							+ " removed!");
					playerConfig.saveConfig();
					break;
				}
			}

		}

		if (commandLabel.equalsIgnoreCase("sethorsehome")) {
			String horseHomeName = "home";
			String inputOne;
			try {
				inputOne = args[0].toLowerCase();
			} catch (ArrayIndexOutOfBoundsException e) {
				inputOne = "home";
			}
			if (inputOne.equalsIgnoreCase("list")) {
				inputOne = "home";
			}
			horseHomeName = inputOne;
			if (player.hasPermission("horsehome.sethorsehome.multiple")) {
				String allowedHomesString = "1";
				ArrayList<String> rankList = null;
				rankList = (ArrayList<String>) this.getConfig().getStringList(
						"HorseHomes");
				try {
					for (String temp : rankList) {
						String[] tempArray = temp.split(" - ");
						String rank = tempArray[0];
						allowedHomesString = tempArray[1];
						if (player
								.hasPermission("horsehome.sethorsehome.multiple."
										+ rank)) {
							break;
						}
					}
				} catch (Throwable e) {
				}

				int allowedHomes = 1;
				try {
					allowedHomes = Integer.parseInt(allowedHomesString);
				} catch (Throwable e) {
					allowedHomes = 1;
				}
				ArrayList<String> arrayList = null;
				try {
					arrayList = (ArrayList<String>) playerConfig.getConfig()
							.getStringList("list");
				} catch (Throwable e) {
				}
				int currentHomes = arrayList.size();
				if (currentHomes >= allowedHomes) {
					boolean horseHomeExists = false;
					for (String item : arrayList) {
						if (item.equalsIgnoreCase(horseHomeName)) {
							horseHomeExists = true;
							break;
						} else {

						}
					}
					if (horseHomeExists) {
						String[] horseHomeStringArray = new String[4];
						horseHomeStringArray[0] = Integer.toString(player
								.getLocation().getBlockX());
						horseHomeStringArray[1] = Integer.toString(player
								.getLocation().getBlockY());
						horseHomeStringArray[2] = Integer.toString(player
								.getLocation().getBlockZ());
						horseHomeStringArray[3] = player.getWorld().getName();
						playerConfig.getConfig().set(horseHomeName,
								horseHomeStringArray);
						playerConfig.saveConfig();
						player.sendMessage(ChatColor.GREEN + horseHomeName
								+ " set!");
					} else {
						player.sendMessage(ChatColor.RED
								+ "You don't have permission to set this many homes.");
					}
				} else { // (currentHomes < allowedHomes)
					boolean horseHomeExists = false;
					for (String item : arrayList) {
						if (item.equalsIgnoreCase(horseHomeName)) {
							horseHomeExists = true;
							break;
						}
					}
					if (horseHomeExists) {
						String[] horseHomeStringArray = new String[4];
						horseHomeStringArray[0] = Integer.toString(player
								.getLocation().getBlockX());
						horseHomeStringArray[1] = Integer.toString(player
								.getLocation().getBlockY());
						horseHomeStringArray[2] = Integer.toString(player
								.getLocation().getBlockZ());
						horseHomeStringArray[3] = player.getWorld().getName();
						playerConfig.getConfig().set(horseHomeName,
								horseHomeStringArray);
						player.sendMessage(ChatColor.GREEN + horseHomeName
								+ " set!");
						playerConfig.getConfig().set("list", arrayList);
						playerConfig.saveConfig();
					} else {
						String[] horseHomeStringArray = new String[4];
						horseHomeStringArray[0] = Integer.toString(player
								.getLocation().getBlockX());
						horseHomeStringArray[1] = Integer.toString(player
								.getLocation().getBlockY());
						horseHomeStringArray[2] = Integer.toString(player
								.getLocation().getBlockZ());
						horseHomeStringArray[3] = player.getWorld().getName();
						playerConfig.getConfig().set(horseHomeName,
								horseHomeStringArray);
						player.sendMessage(ChatColor.GREEN + horseHomeName
								+ " set!");

						arrayList.add(horseHomeName);
						playerConfig.getConfig().set("list", arrayList);
						playerConfig.saveConfig();
					}
				}

				horseHomeName = inputOne;
			} else if (inputOne != "home") { // if player doesn't have
												// horsehome.sethorsehome.multiple
				ArrayList<String> arrayList = null;
				try {
					arrayList = (ArrayList<String>) playerConfig.getConfig()
							.getStringList("list");
				} catch (Throwable e) {
					// do nothing
				}
				boolean horseHomeExists = false;
				for (String item : arrayList) {
					if (item.equalsIgnoreCase(horseHomeName)) {
						horseHomeExists = true;
						break;
					} else {
						// do nothing
					}
				}
				if (!horseHomeExists) {
					arrayList.add(horseHomeName);
				}
				player.sendMessage(ChatColor.RED
						+ "You don't have permission for multiple homes, trying \"home\".");
				String[] horseHomeStringArray = new String[4];
				horseHomeStringArray[0] = Integer.toString(player.getLocation()
						.getBlockX());
				horseHomeStringArray[1] = Integer.toString(player.getLocation()
						.getBlockY());
				horseHomeStringArray[2] = Integer.toString(player.getLocation()
						.getBlockZ());
				horseHomeStringArray[3] = player.getWorld().getName();
				playerConfig.getConfig().set(horseHomeName,
						horseHomeStringArray);
				player.sendMessage(ChatColor.GREEN + horseHomeName + " set!");
				playerConfig.getConfig().set("list", arrayList);
				playerConfig.saveConfig();
			} else {
				ArrayList<String> arrayList = null;
				try {
					arrayList = (ArrayList<String>) playerConfig.getConfig()
							.getStringList("list");
				} catch (Throwable e) {
					// do nothing.
				}
				boolean horseHomeExists = false;
				for (String item : arrayList) {
					if (item.equalsIgnoreCase(horseHomeName)) {
						horseHomeExists = true;
						break;
					} else {
						// do nothing
					}
				}
				if (!horseHomeExists) {
					arrayList.add(horseHomeName);
				}
				String[] horseHomeStringArray = new String[4];
				horseHomeStringArray[0] = Integer.toString(player.getLocation()
						.getBlockX());
				horseHomeStringArray[1] = Integer.toString(player.getLocation()
						.getBlockY());
				horseHomeStringArray[2] = Integer.toString(player.getLocation()
						.getBlockZ());
				horseHomeStringArray[3] = player.getWorld().getName();
				playerConfig.getConfig().set(horseHomeName,
						horseHomeStringArray);
				player.sendMessage(ChatColor.GREEN + horseHomeName + " set!");
				playerConfig.getConfig().set("list", arrayList);
				playerConfig.saveConfig();
			}
		}

		if (commandLabel.equalsIgnoreCase("horsehome")) {
			String horseHomeName = "home";
			String inputOne;
			Boolean finish = true;
			try {
				inputOne = args[0].toLowerCase();
			} catch (ArrayIndexOutOfBoundsException e) {
				inputOne = "home";
			}
			if (inputOne.equalsIgnoreCase("list")) {
				ArrayList<String> arrayList = null;
				try {
					arrayList = (ArrayList<String>) playerConfig.getConfig()
							.getStringList("list");
				} catch (Throwable e) {
				}
				String list = "HorseHomes: ";
				for (String item : arrayList) {
					list = list + item + ", ";
				}
				player.sendMessage(ChatColor.GREEN + list);
				finish = false;
			}
			if (player.hasPermission("horsehome.sethorsehome.multiple")
					&& finish) {
				horseHomeName = inputOne;
			} else if ((inputOne != "home") && finish) {
				player.sendMessage(ChatColor.RED
						+ "You don't have permission for multiple homes, trying \"home\".");
			}
			if (player.getVehicle() == null && finish) {
				player.sendMessage(ChatColor.GREEN
						+ "You must be on your horse!");
			}
			if (finish) {
				try {
					if (player.getVehicle().getType() == EntityType.HORSE) {
						try {
							String[] horseHomeStringArray = new String[4];
							ArrayList<String> arrayList = (ArrayList<String>) playerConfig
									.getConfig().getStringList(horseHomeName);
							horseHomeStringArray = arrayList
									.toArray(new String[arrayList.size()]);
							if (arrayList.size() != 4) {
								player.sendMessage(ChatColor.GREEN
										+ horseHomeName + " not set!");
							} else {
								int horsehomeX = Integer
										.parseInt(horseHomeStringArray[0]);
								int horsehomeY = Integer
										.parseInt(horseHomeStringArray[1]);
								int horsehomeZ = Integer
										.parseInt(horseHomeStringArray[2]);
								String worldString = horseHomeStringArray[3];
								if (Bukkit.getServer().getWorld(worldString) == player
										.getLocation().getWorld()) {
									World world = Bukkit.getServer().getWorld(
											worldString);

									Location horsehome = new Location(world,
											horsehomeX, horsehomeY, horsehomeZ);
									Horse horse = (Horse) player.getVehicle();

									horse.eject();
									world.getChunkAt(horsehome);

									horse.teleport(horsehome);
									LivingEntity theHorse = horse;
									theHorse.setHealth(((Damageable) theHorse)
											.getHealth());

									world.getChunkAt(horsehome);
									player.sendMessage(ChatColor.GREEN
											+ "Horse sent to " + horseHomeName
											+ "!");
								} else {
									player.sendMessage(ChatColor.GREEN
											+ "Destination must be the same as current world.");
								}
							}
						} catch (NullPointerException error1) {
							player.sendMessage(ChatColor.GREEN + horseHomeName
									+ " not set!");
						}
					} // End If(vehicle is horse)
				} catch (ClassCastException error2) {
					player.sendMessage(ChatColor.GREEN
							+ "You must be on your horse!");
				}
			}
		}

		return false;
	}
}
