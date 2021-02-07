package io.github.Toydotgame;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {	
	@Override
	public void onEnable() {
		new UpdateChecker(this).getVersion(version -> { // Huge thanks to the great code on the SpigotMC wiki: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates/
			if(this.getDescription().getVersion().equalsIgnoreCase(version)) {
				System.out.print("[Thisway] Plugin successfully loaded.");
				DataStorage.isUpToDate = true;
			} else {
				System.out.print("[Thisway] Please update Thisway! (Plugin loading anyway)");
				DataStorage.isUpToDate = false;
			}
		});
		DataStorage.version = this.getDescription().getVersion();
		this.getCommand("thisway").setExecutor(new Thisway());
	}

	@Override
	public void onDisable() {
		// No.
	}
}
