package net.toydotgame.Thisway;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for Spigot hooks to execute our code starting from.
 */
public final class Thisway extends JavaPlugin {
	// No need to add a custom constructor/actually extend JavaPlugin. Just take
	// an instance of class Thisway to be a plain ol' JavaPlugin
	
	/**
	 * Check for updates and load our config. Log a simple message to follow the
	 * Spigot logs that only say that they've <i>begun</i> enabling Thisway.
	 * {@inheritDoc}.
	 */
	@Override
	public void onEnable() {
		UpdateChecker.checkForUpdates(this);
		Configurator.loadConfig(this);
		
		this.getLogger().info("Enabled!");
	}
}
