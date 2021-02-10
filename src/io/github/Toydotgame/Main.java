package io.github.Toydotgame;

import java.util.concurrent.CompletableFuture;

import org.bukkit.plugin.java.JavaPlugin;

import me.robertlit.spigotresources.api.Resource;
import me.robertlit.spigotresources.api.SpigotResourcesAPI;

public class Main extends JavaPlugin {	
	@Override
	public void onEnable() {
		System.out.println("asdfdsafdsaf");
		
		SpigotResourcesAPI api = new SpigotResourcesAPI(false);
		
		CompletableFuture<Resource> future = api.getResource(87115);
		future.thenAccept(resource -> {
			DataStorage.serverVersion = resource.getVersion();
			DataStorage.version = this.getDescription().getVersion();
			
			if(DataStorage.serverVersion.equals(DataStorage.version)) {
				System.out.print("[Thisway] Plugin loaded successfully!");
			} else {
				System.out.print("[Thisway] Thisway is out-of-date or you are using a beta version of the plugin. If it's the former; I strongly suggest that you update in case of any potential bugs in your copy of the plugin.");
				System.out.print("[Thisway] The plugin was loaded anyway.");
			}
		});
		this.getCommand("thisway").setExecutor(new Thisway());
	}

	@Override
	public void onDisable() {
		// No.
	}
}
