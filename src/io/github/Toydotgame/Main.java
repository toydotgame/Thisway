package io.github.Toydotgame;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private Main plugin;
	private File customConfigFile;
	private FileConfiguration customConfig;
	
	@Override
	public void onEnable() {
		createCustomConfig(); // This DOES NOT overwrite any existing config files.
		System.out.print("[Thisway] Plugin successfully loaded.");
		this.getCommand("thisway").setExecutor(new Thisway());
	}
	
	public FileConfiguration getCustomConfig() {
		return this.customConfig;
	}
	
	private void createCustomConfig() {
		customConfigFile = new File(getDataFolder(), "config.yml");
		if(!customConfigFile.exists()) {
			customConfigFile.getParentFile().mkdirs(); // Creates parent directories.
			plugin.saveResource("config.yml", false); // Creates config file.
		}
		
		customConfig = new YamlConfiguration();
		try {
			customConfig.load(customConfigFile);
		} catch(IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		// No.
	}
}
