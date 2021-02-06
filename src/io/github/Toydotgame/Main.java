package io.github.Toydotgame;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {	
	@Override
	public void onEnable() {
		this.getCommand("thisway").setExecutor(new Thisway());
		
		new UpdateChecker(this, 87115).getVersion(version -> {
			if(this.getDescription().getVersion().equalsIgnoreCase(version)) {
				System.out.print("[Thisway] Plugin successfully loaded.");
			} else {
				System.out.print("[Thisway] Please update Thisway! (Plugin loaded anyway)");
			}
		});
	}

	@Override
	public void onDisable() {
		// No.
	}
}
