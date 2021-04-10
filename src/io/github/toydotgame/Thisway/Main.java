package io.github.toydotgame.Thisway;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {	
	@Override
	public void onEnable() {
		DataStorage.localVersion = this.getDescription().getVersion();
		this.getCommand("thisway").setExecutor(new Thisway());
		System.out.print("[Thisway] Plugin loaded successfully!");
	}
}
