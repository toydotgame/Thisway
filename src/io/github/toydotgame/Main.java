package io.github.toydotgame;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {	
	@Override
	public void onEnable() {
		DataStorage.localVersion = this.getDescription().getVersion();
		System.out.print("[Thisway] Plugin loaded successfully!");
		this.getCommand("thisway").setExecutor(new Thisway());
	}

	@Override
	public void onDisable() {
		// No.
	}
}
