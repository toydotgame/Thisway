package net.toydotgame.Thisway;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;

/**
 * Simple update checkver for this plugin on the SpigotMC API.<br>
 * <br>
 * The primary reason that this class isn't static like the other init classes
 * is that this class uses Bukkit Runnables that are ever-so-slightly kinder to
 * deal with in an instantiated context. Also making this class static to save
 * one line of code in {@link Thisway#onEnable() onEnable()} isn't worth the
 * effort.
 * <dl><dt><b>Created on:</b></dt><dd>2025-07-15</dd></dl>
 * @author toydotgame
 */
public final class UpdateChecker {
	// Instance fields:
	public final String INSTALLED_VERSION;
	private final Thisway PLUGIN_INSTANCE;
	public boolean IS_UP_TO_DATE = false;
	// Set later:
	public String SPIGOTMC_VERSION;
	
	UpdateChecker(Thisway plugin) {
		PLUGIN_INSTANCE = plugin;
		INSTALLED_VERSION = plugin.getDescription().getVersion();
	}
	
	// Constants:
	private static final String RESOURCE_ID = "87115";
	private static final String SPIGOT_API = "https://api.spigotmc.org/legacy/update.php?resource=";
	
	/**
	 * Asynchronously gets the latest plugin version string from the SpigotMC
	 * legacy API. Runs an input function (no arguments or return) when the
	 * request is done.
	 * @param r {@link java.lang.Runnable Runnable} lambda to run when the API
	 * request has finished. Think of this as the callback
	 */
	private void getLatestVersionFromAPI(Runnable r) {
		Bukkit.getScheduler().runTaskAsynchronously(PLUGIN_INSTANCE, () -> {
			try(Scanner s = new Scanner(new URL(SPIGOT_API+RESOURCE_ID).openStream())) {
				if(s.hasNext()) SPIGOTMC_VERSION = s.next();
			} catch(IOException e) {} // If we fail to get a meaningful response, we fall through with `null`
			
			// Run the lambda:
			r.run();
		});
	}
	
	/**
	 * Prints logs depending on if the provided plugin instance is ahead of,
	 * behind, or equal to the plugin version found on SpigotMC.<br>
	 * <br>
	 * This method runs async, so as to not hurt server boot or {@code /reload}
	 * performance.
	 * @see #getLatestVersionFromAPI(Runnable)
	 */
	void logUpdates() {
		// getLatestVersionFromAPI(Runnable r) fetches the latest version and
		// sets this instance's SPIGOTMC_VERSION field accordingly (can be null
		// if failed). It then runs the input lambda as a kind of callback:
		getLatestVersionFromAPI(() -> {
			// We get null if the fetch failed:
			if(SPIGOTMC_VERSION == null) {
				Lang.logWarning("updates.fetch-failure");
				return;
			}
			
			// Check if up-to-date:
			if(INSTALLED_VERSION.equals(SPIGOTMC_VERSION)) {
				IS_UP_TO_DATE = true;
				return;
			}
			// Else, inform the user
			
			// Die if we don't have version alerts enabled:
			if(!Configurator.fetchToggle(Option.VERSION_ALERTS)) return;
			
			// If not up-to-date, are we ahead or behind?:
			String greaterVersion = getGreaterVersion(INSTALLED_VERSION, SPIGOTMC_VERSION);
			if(greaterVersion == INSTALLED_VERSION) {
				Thisway.logger.fine(Lang.create("updates.version-ahead"));
			} else if(greaterVersion == SPIGOTMC_VERSION) {
				String message = Lang.create("updates.version-behind", SPIGOTMC_VERSION, INSTALLED_VERSION);					
				// Broadcast to console and ops:
				if(Configurator.fetchToggle(Option.BROADCAST_VERSION_ALERTS))
					Bukkit.getServer().broadcast("[Thisway] "+ChatColor.YELLOW+message, Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
				else Thisway.logger.warning(message);
			} else {
				Lang.logWarning("updates.version-unknown", INSTALLED_VERSION, SPIGOTMC_VERSION);
			}
		});
	}
	
	/**
	 * Compares two {@code .}-delimited version numbers and returns the greater
	 * object reference. Works ideally with an equal count of
	 * major/minor/patch/etc numbers; otherwise, the comparison will only be
	 * done to the number of places of the shorter version string.
	 * @param v1Str Version string to compare
	 * @param v2Str Version string to compare
	 * @return Reference to the greater version number {@code String} object, or
	 * {@code null} if the inputs are invalid/equal
	 */
	private static String getGreaterVersion(String v1Str, String v2Str) {
		String[] v1 = v1Str.split("\\."), v2 = v2Str.split("\\.");
		
		for(int i = 0; i < Math.min(v1.length, v2.length); i++) {
			try {
				final int a = Integer.parseInt(v1[i]), b = Integer.parseInt(v2[i]);
				if(a > b) return v1Str;
				if(a < b) return v2Str;
			} catch(NumberFormatException e) {
				return null; // Garbled input
			}
		}
		return null; // Iteration completed where all elements checked were equal
	}
}
