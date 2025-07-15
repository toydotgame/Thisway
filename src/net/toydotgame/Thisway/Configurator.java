package net.toydotgame.Thisway;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Simple static class to load and manage Thisway's configuration.
 */
final class Configurator {
	private Configurator() {} // Static class
	
	private static Map<Option, Boolean> defaults = loadDefaults();
	private static Map<Option, Boolean> loadDefaults() {
		Map<Option, Boolean> map = new HashMap<Option, Boolean>();
		
		map.put(Option.VERSION_ALERTS, true);
		map.put(Option.BROADCAST_VERSION_ALERTS, true);
		map.put(Option.LOG_TELEPORTS, false);
		
		return map;
	}
	private static FileConfiguration config;
	
	/**
	 * Loads the configuration for this {@code JavaPlugin} instance, and
	 * appends/creates default values if not found on the disk. Also initalises
	 * internal configuration to be kept in memory, where they can be called
	 * with {@link #fetch(Option)}.
	 * @param plugin {@link org.bukkit.plugin.java.JavaPlugin JavaPlugin}
	 * instance
	 */
	static void loadConfig(Thisway plugin) {
		config = plugin.getConfig();
		
		for(Option key : defaults.keySet())
			config.addDefault(key.yamlName, defaults.get(key));
		
		config.options().copyDefaults(true); // Save/append options to config file if they don't already exist
		plugin.saveConfig(); // Write FileConfiguration to disk
	}
	
	/**
	 * Fetches the configuration value set for the given {@link Option}, or the
	 * default (as established earlier) if it is somehow not found in memory.
	 * <br><br>
	 * Generally, if {@link #loadConfig(Thisway)} has been called beforehand,
	 * then there should be no scenario where this option won't be found, given
	 * defaults have already been added to the in-memory cache of config
	 * <i>and</i> have been written to disk.
	 * @param option {@link Option} to look for
	 * @return {@code true} or {@code false} depending on the user's setting
	 */
	static boolean fetch(Option option) {
		return config.getBoolean(option.yamlName);
	}
}
