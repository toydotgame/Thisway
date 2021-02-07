package io.github.Toydotgame;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateChecker {
	private JavaPlugin plugin;
	
	public UpdateChecker(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void getVersion(final Consumer<String> consumer) {
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
			try(InputStream inputStream = new URL("https://raw.githubusercontent.com/toydotgame/Thisway/main/update.php").openStream(); Scanner scanner = new Scanner(inputStream)) {
				if(scanner.hasNext()) {
					consumer.accept(scanner.next());
				}
			} catch(IOException exception) {
				System.out.print("[Thisway] Failed to check for updates!");
			}
		});
	}
}
