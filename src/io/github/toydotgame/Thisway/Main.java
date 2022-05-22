package io.github.toydotgame.Thisway;

import org.bukkit.plugin.java.JavaPlugin;

/* 
 * CREATED: 2020-12-26
 * AUTHOR: toydotgame
 * Loader class for the plugin.
 */

public class Main extends JavaPlugin {	
	@Override
	public void onEnable() {
		DataStorage.localVersion = this.getDescription().getVersion();
		this.getCommand("thisway").setExecutor(new Thisway());
		System.out.print("[Thisway] Plugin loaded successfully!");
	}
}
