package net.toydotgame.Thisway;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Simple static class to load and manage Thisway's configuration.
 */
public final class Configurator {
	private Configurator() {} // Static class
	
	private static FileConfiguration config;
	
	/**
	 * Loads the configuration for this {@code JavaPlugin} instance, and
	 * appends/creates default values if not found on the disk. Also initalises
	 * internal configuration to be kept in memory, where they can be called
	 * with {@link #fetchToggle(Option)}.
	 * @param plugin {@link org.bukkit.plugin.java.JavaPlugin JavaPlugin}
	 * instance
	 */
	static void loadConfig(Thisway plugin) {
		config = plugin.getConfig();
		if(config.getValues(true).size() == 0)
			Lang.log("config.created");
		
		for(Option option : Option.values()) {
			if(option == Option.LANGUAGE)
				config.addDefault(option.yamlName, attemptCustomLanguage(option.defaultValue.toString()));
			else config.addDefault(option.yamlName, option.defaultValue);
		}
		
		config.options().copyDefaults(true); // Save/append options to config file if they don't already exist
		plugin.saveConfig(); // Write FileConfiguration to disk
	}
	
	/**
	 * Checks if Thisway supports the system's locale out of the box. If not,
	 * then it returns the fallback locale ({@code en-US})
	 * @param defaultValue
	 * @return
	 */
	private static String attemptCustomLanguage(String defaultValue) {
		String locale = Locale.getDefault().toString().replace('_', '-');
		if(Arrays.asList(Lang.languages).contains(locale)) return locale;
		// Else, return default passed in:
		return defaultValue;
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
	public static boolean fetchToggle(Option option) {
		return config.getBoolean(option.yamlName);
	}
	/**
	 * Fetches a String object representation for the given {@link Option}, or
	 * its default value if not found.
	 * @param option {@link Option} to look for
	 * @return The String value of this option (either its user-set value or
	 * its default). Returns {@code null} if not found
	 * @see #fetchToggle(Option)
	 */
	static String fetchString(Option option) {
		return config.getString(option.yamlName);
	}
	
	/**
	 * Takes the loaded configuration file from memory, transmutes it to an
	 * ordered {@link java.util.LinkedHashMap Map}, and returns it.
	 * @return Thisway configuration names and their current values
	 */
	public static Map<String, String> fetchAll() {
		Map<String, String> result = new LinkedHashMap<String, String>();
		config.getValues(true).forEach((k, v) -> {
			//if(k.equalsIgnoreCase("true") || k.equalsIgnoreCase("false"))
			result.put(k, v.toString());
		});
		
		return result;
	}
}
