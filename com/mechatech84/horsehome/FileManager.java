package com.mechatech84.horsehome;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * The FileManager class is used to manipulate and create folders/files that are
 * essential for HorseHome.
 */
public class FileManager {

	private String sep = File.separator;
	private String suffix = ".yml";
	private NewHorseHome plugin = NewHorseHome.getInstance();
	private File dataFolder = plugin.getDataFolder();

	@SuppressWarnings("unused")
	private void print(String message) {
		plugin.getLogger().log(Level.INFO, message);
	}

	public void deleteHorseHomeFile(String playerName, String horseHomeName) {
		File file = new File(getFolderPath(playerName) + horseHomeName + suffix);
		if (file.exists()) {
			file.delete();
			//return true;
		}
		//return false;

	}

	/** Return all current homes for the specified player. */
	public String[] getTotalHomesArray(String playerName) {
		String path = getFolderPath(playerName);
		File tempFile = new File(path);
		int totalHomes = getTotalHomesNumber(playerName);
		if (totalHomes == 0) {
			return new String[0];
		}
		ArrayList<String> homesArray = new ArrayList<String>();

		File[] allFiles = tempFile.listFiles();
		for (File file : allFiles) {
			if (file.isFile() && file.getName().endsWith(suffix)) {
				String newFileName = (file.getName().substring(0, file
						.getName().length() - suffix.length())).toString();
				homesArray.add(newFileName);
			}
		}
		String[] finalArray = homesArray.toArray(new String[homesArray.size()]);

		return finalArray;
	}

	/** Returns the total number of homes for the specified player. */
	public int getTotalHomesNumber(String playerName) {
		String path = getFolderPath(playerName);
		File tempFile = new File(path);
		int total = 0;
		if (!tempFile.exists()) {
			generateFolderForPlayer(playerName);
			/*plugin.getLogger()
					.log(Level.INFO,
							"An error must've occured, maybe the player named: "
									+ playerName
									+ "doesn't have any homes? If the "
									+ "problem persists, try sending a message to either MechaTech84 or Lazini via the dev.Bukkit.com site.");*/
			return total;
		}
		File[] allFiles = tempFile.listFiles();

		for (File fileName : allFiles) {
			if (fileName.isFile() && fileName.getName().endsWith(suffix)) {
				total++;
			}
		}
		return total;
	}

	/** Return a config file for the player you specify. */
	public ConfigAccessor getConfigForPlayer(String playerName) {
		ConfigAccessor config = new ConfigAccessor(plugin, playerName);
		return config;
	}

	/**
	 * Generates the essential files depending on the player and the home name
	 * you specify.
	 */
	public ConfigAccessor generateHorseHomeForPlayer(String playerName,
			String horseHomeName) {
		ConfigAccessor config = new ConfigAccessor(plugin,
				getProperFolderPath(playerName) + horseHomeName + suffix);
		if (!checkForHorseHomeFolder(playerName))
			generateFolderForPlayer(playerName);
		if (!checkForHorseHomeFile(playerName, horseHomeName)) {
			File tempFile = new File(getFolderPath(playerName) + horseHomeName
					+ suffix);
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				/*
				e.printStackTrace();
				plugin.getLogger()
						.log(Level.INFO,
								"Could not create config file (horse home file) for this player. Please try again.");
				*/
			}
		}
		return config;
	}

	public boolean checkForHorseHomeFile(String playerName, String horseHomeName) {
		File tempFile = new File(getFolderPath(playerName) + horseHomeName
				+ suffix);
		if (tempFile.exists())
			return true;
		return false;
	}

	/** Checks if there is a folder for that player. */
	public boolean checkForHorseHomeFolder(String playerName) {
		File tempFile = new File(getFolderPath(playerName));
		if (tempFile.exists())
			return true;
		return false;
	}

	/**
	 * Generates a folder for a specific player, which later is used to manage
	 * horse homes.
	 * 
	 * @param playerName
	 *            The folder's name.
	 */
	public void generateFolderForPlayer(String playerName) {
		/*ConfigAccessor config = new ConfigAccessor(plugin, dataFolder + "/"
				+ PLUGIN_FOLDER_NAME + "/" + playerName);*/
		File file = new File(getFolderPath(playerName));
		file.mkdirs();
	}

	public String getFolderPath(String playerName) {
		if (playerName.equalsIgnoreCase("") || playerName == null) {
			playerName = "TempHome_NotOwnedByAnyone";
		}
		String folderPath = dataFolder + sep + "Homes" + sep + playerName + sep;
		return folderPath;
	}

	public String getProperFolderPath(String playerName) {
		if (playerName.equalsIgnoreCase("") || playerName == null) {
			playerName = "TempHome_NotOwnedByAnyone";
		}
		String folderPath = "Homes" + sep + playerName + sep;
		return folderPath;
	}
}
