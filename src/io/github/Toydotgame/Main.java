package io.github.Toydotgame;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		this.getCommand("thisway").setExecutor(new Thisway());
	}
	
	@Override
	public void onDisable() {
		// No.
	}
}