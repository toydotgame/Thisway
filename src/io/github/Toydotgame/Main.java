package io.github.Toydotgame;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {	
	@Override
	public void onEnable() {
		new UpdateChecker(this, 87115).getVersion(version -> { // Huge thanks to the great code on the SpigotMC wiki: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates/
			DataStorage.version = this.getDescription().getVersion();
			
			if(this.getDescription().getVersion().equalsIgnoreCase(version)) {
				DataStorage.isUpToDate = true;
				this.getCommand("thisway").setExecutor(new Thisway()); // Plugin command code, loaded in both cases.
				System.out.print("[Thisway] Plugin successfully loaded.");
			} else {
				DataStorage.isUpToDate = false;
				this.getCommand("thisway").setExecutor(new Thisway());
				System.out.print("[Thisway] Please update Thisway! (Plugin loaded anyway)");
			}
		});
	}

	@Override
	public void onDisable() {
		// No.
	}
}
