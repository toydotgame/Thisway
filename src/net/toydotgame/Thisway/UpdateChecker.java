package net.toydotgame.Thisway;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;

/**
 * Simple update checkver for this plugin on the SpigotMC API. Only exists and
 * is used in plugin init and is left to be discarded after that.
 */
final class UpdateChecker {
	private UpdateChecker() {} // Static class
	
	private static final String RESOURCE_ID = "87115";
	private static final String SPIGOT_API = "https://api.spigotmc.org/legacy/update.php?resource=";
	
	/**
	 * Fetches the latest version from the SpigotMC API on an async Bukkit Task
	 * thread, and passes it to the input lambda.
	 * @param consumer Lambda that takes a single {@code String} argument, which
	 * will be filled with the value of the latest version string from the
	 * SpigotMC API
	 */
	private static void getLatestVersion(Thisway plugin, Consumer<String> consumer) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			String latest = null;
			try(Scanner s = new Scanner(new URL(SPIGOT_API+RESOURCE_ID).openStream())) {
				if(s.hasNext()) latest = s.next();
			} catch(IOException e) {} // If we fail to get a meaningful response, we fall through with `null`
			
			// Then give the value we got to the lambda that called us:
			consumer.accept(latest);
		});
	}
	
	/**
	 * Checks for updates using {@link #getLatestVersion(Consumer)} and does the
	 * following for the following cases:
	 * <ul>
	 * 	<li>Installed version &gt; SpigotMC version?: Prints console log (log
	 * level FINE, so not even seen by default) stating such</li>
	 * 	<li>Installed version = SpigotMC version?: Does nothing</li>
	 * 	<li>Installed version &lt; SpigotMC version?: Prints console log (log
	 * level WARNING) and messages all operators online</li>
	 * </ul>
	 * For cases where the API fetch fails, or checking the greater version
	 * fails, respective messages are printed to the console.
	 */
	static void checkForUpdates(Thisway plugin) {
		Logger logger = plugin.getLogger();
		String localVersion = plugin.getDescription().getVersion(); // We will assume version strings are dot-delimited numbers
		
		getLatestVersion(plugin, latestVersion -> {
			if(latestVersion == null) { // Catch the null value from getLatestVersion()'s error case:
				logger.warning("Couldn't fetch the latest version!");
				return;
			}
			if(localVersion.equals(latestVersion)) return; // Skip further calculations
			
			String greaterVersion = getGreaterVersion(localVersion, latestVersion);
			if(greaterVersion == localVersion) {
				logger.fine("Local version is greater than the one found on SpigotMC");
			} else if(greaterVersion == latestVersion) {
				String message = "Newer version found on SpigotMC! (v"+latestVersion+") You are running v"+localVersion;
				// Broadcast to console and ops:
				logger.warning(message);
				Bukkit.getServer().broadcast("[Thisway] "+ChatColor.YELLOW+message, Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
			} else {
				logger.warning("Couldn't determine the latest version out of these two: "
					+"v"+localVersion+" (local), v"+latestVersion+" (SpigotMC)");
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
