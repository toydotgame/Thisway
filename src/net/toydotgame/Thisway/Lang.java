package net.toydotgame.Thisway;

import java.io.File;
import java.util.List;
import java.util.MissingFormatArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {
	private Lang() {} // Static class
	
	static final String[] languages = {"en-US"};
	private static YamlConfiguration defaults, messages;
	private static String preferredLanguage;
	private static Thisway plugin;
	
	// Few language provisions are made in this class, so most language-fetching
	// -related errors will be in English. This is to avoid catastrophic looping
	// type errors when a hypothetical language error file cannot be found
	// TODO: The above in the Javadoc class comment
	
	private static String translationFailure(String key) {
		return "Couldn't find "+preferredLanguage+" translation for "+key+"! "
			+"Falling back to en-US translation";
	}
	private static String fallbackFailure(String key) {
		return "Couldn't find fallback "
			+"message for \""+key+"\"! If you are getting this error on a "
			+"non-debugging build of Thisway: 1. Check you are up to date, and 2. "
			+"Check that your en-US fallback or desired language file's version "
			+"matches the installed Thisway version. The easiest way to fix this "
			+"is to delete your plugins"+File.separator+"Thisway"+File.separator
			+"lang"+File.separator+" folder and try again";
	}
	
	static void loadMessages(Thisway plugin) {
		Lang.plugin = plugin;
		
		for(String lang : languages)
			plugin.saveResource("lang/"+lang+".yml", true);
		
		preferredLanguage = Configurator.fetchString(Option.LANGUAGE);
		
		defaults = loadLanguage("en-US");
		messages = loadLanguage(preferredLanguage);
		
		// WARN: If mismatch between language and plugin version
		String languageFormat = messages.getString("plugin-format");
		String pluginVersion = plugin.getDescription().getVersion();
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
		if(fetchedMessage == null && !preferredLanguage.equals("en-US")) { // Don't log this error for en-US because it's fallback and will fail again
			plugin.getLogger().warning(translationFailure(key));
			fetchedMessage = defaults.getString(key);
		}
		// If we check the fallback and it's not there, then it needs to be implemented internally:
		if(fetchedMessage == null) {
			plugin.getLogger().severe(fallbackFailure(key));
			return syntaxError(key);
		}
		
		try {
			return String.format(fetchedMessage, args);
		} catch(MissingFormatArgumentException e) {
			Bukkit.getLogger().severe("For key \""+key+"\":"); // Use universal logger because printStackTrace() does too
			e.printStackTrace(); // Ends up in log
			return syntaxError(key, "ARGUMENT MISMATCH");
		}
	}
	
	public static List<String> fetchList(String key) {
		List<String> fetchedList = messages.getStringList(key);
		if(fetchedList.size() == 0 && !preferredLanguage.equals("en-US")) {
			plugin.getLogger().warning(translationFailure(key)
				+"If you are writing your own translation, make sure that "
				+key+" is a list rather than a mapping");
			fetchedList = defaults.getStringList(key);
		}
		// Again, if it's still null, then it needs to be implemented:
		if(fetchedList.size() == 0) {
			plugin.getLogger().severe(fallbackFailure(key));
			fetchedList.add(syntaxError(key));
			return fetchedList;
		}
		
		return fetchedList;
	}
	
	private static String syntaxError(String key, String... errors) {
		String errorString = (errors.length == 0 ? ""
			:": "+String.join(" ", errors));
		return ""+ChatColor.RESET+ChatColor.RED
			+"<"+key+errorString+ChatColor.RESET+ChatColor.RED+">"
			+ChatColor.RESET;
	}
	
	public static boolean isList(String key) {
		return messages.isList(key) || defaults.isList(key);
	}
	
	public static boolean containsKey(String key) {
		return messages.getString(key) != null || defaults.getString(key) != null;
	}
	
	static void log(String key, Object... args) {
		plugin.getLogger().info(create(key, args));
	}
	
	static void logWarning(String key, Object... args) {
		plugin.getLogger().warning(create(key, args));
	}
}
