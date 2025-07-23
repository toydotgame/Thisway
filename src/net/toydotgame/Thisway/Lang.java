package net.toydotgame.Thisway;

import java.io.File;
import java.util.List;
import java.util.MissingFormatArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {
	private Lang() {} // Static class
	
	static final String[] languages = {"en-US"};
	private static YamlConfiguration defaults;
	private static YamlConfiguration messages;
	private static String preferredLanguage;
	private static Thisway plugin;
	
	static void loadMessages(Thisway plugin) {
		Lang.plugin = plugin;
		
		for(String lang : languages)
			plugin.saveResource("lang/"+lang+".yml", true);
		
		preferredLanguage = Configurator.fetchString(Option.LANGUAGE);
		
		defaults = loadLanguage("en-US");
		messages = loadLanguage(preferredLanguage);
		
		// WARN: If mismatch between language and plugin version
		String languageFormat = messages.getString("plugin-format");
		String pluginVersion = Thisway.updates.INSTALLED_VERSION;
		if(languageFormat != null && !languageFormat.equals(pluginVersion)) {
			log("messages.format-mismatch", preferredLanguage, languageFormat, pluginVersion);
		}
	}
	
	private static YamlConfiguration loadLanguage(String IETFCode) {
		return YamlConfiguration.loadConfiguration(
			new File(plugin.getDataFolder(), "lang"+File.separator+IETFCode+".yml"));
	}
	
	public static String create(String key, Object... args) {
		String fetchedMessage = messages.getString(key);
		if(fetchedMessage == null) {
			Bukkit.getLogger().warning("Couldn't find "+preferredLanguage+" translation for "+key+"! Falling back to en-US translation");
			fetchedMessage = defaults.getString(key);
		}
		// If we check the fallback and it's not there, then it needs to be implemented internally:
		if(fetchedMessage == null) {
			throw new NullPointerException("Couldn't find fallback message for \""
				+key+"\"! If you are getting this error on a non-debugging build "
				+"of Thisway: 1. Check you are up to date, and 2. Check that your "
				+"en-US fallback or desired language file's version matches your "
				+"installed Thisway version—the easiest way to fix this is to "
				+"delete your plugins"+File.separator+"Thisway"+File.separator
				+"lang"+File.separator+" folder and try again");
		}
		
		try {
			return String.format(fetchedMessage, args);
		} catch(MissingFormatArgumentException e) {
			e.printStackTrace(); // Ends up in log
			return "<ARGUMENT MISMATCH FOR LANGUAGE STRING "+key+">";
		}
	}
	
	public static List<String> fetchList(String key) {
		List<String> fetchedList = messages.getStringList(key);
		if(fetchedList.size() == 0) {
			Bukkit.getLogger().warning("Couldn't find "+preferredLanguage+" translations for "+key+"! Falling back to en-US translations");
			fetchedList = defaults.getStringList(key);
		}
		// Again, if it's still null, then it needs to be implemented:
		if(fetchedList.size() == 0) throw new NullPointerException("Couldn't find fallback messages for \""+key+"\"!");
		
		return fetchedList;
	}
	
	static void logFine(String key, Object... args) {
		Bukkit.getLogger().fine(create(key, args));
	}
	
	static void log(String key, Object... args) {
		Bukkit.getLogger().info(create(key, args));
	}
	
	static void logWarning(String key, Object... args) {
		Bukkit.getLogger().warning(create(key, args));
	}
}
