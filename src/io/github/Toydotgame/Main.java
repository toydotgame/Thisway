package io.github.Toydotgame;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {	
	@Override
	public void onEnable() {
		this.getCommand("thisway").setExecutor(new Thisway());
		System.out.print("[Thisway] Plugin successfully loaded.");
	}

	@Override
	public void onDisable() {
		// No.
	}
}
