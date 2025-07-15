package net.toydotgame.Thisway;

import org.bukkit.plugin.java.JavaPlugin;

public final class Thisway extends JavaPlugin {
	// No need to add a custom constructor/actually extend JavaPlugin. Just take
	// an instance of class Thisway to be a plain ol' JavaPlugin
	
	/**
	 * Log a simple message to follow the Spigot logs that only say that they've
	 * <i>begun</i> enabling Thisway. {@inheritDoc}.
	 */
	@Override
	public void onEnable() {
		this.getLogger().info("Enabled!");
	}
}
