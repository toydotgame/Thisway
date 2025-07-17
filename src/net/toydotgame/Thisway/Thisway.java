package net.toydotgame.Thisway;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for Spigot hooks to execute our code starting from.
 */
public final class Thisway extends JavaPlugin {
	// No need to add a custom constructor/actually extend JavaPlugin. Just take
	// an instance of class Thisway to be a plain ol' JavaPlugin
	
	/**
	 * Check for updates and load our config. Log a simple message to follow the
	 * Spigot logs that only say that they've <i>begun</i> enabling Thisway.
	 * {@inheritDoc}.
	 */
	@Override
	public void onEnable() {
		UpdateChecker.checkForUpdates(this); // Not used after this point
		Configurator.loadConfig(this);
		
		this.getLogger().info("Enabled!");
	}
	
	/**
	 * Main command execution logic.
	 * TODO: Seperate class and .setExecutor()? Or just do parsing and
	 * validation here and pass on teleportation and other exeuction to other
	 * classes (probably do this)?
	 * @return {@code true} for syntatically-correct inputs, {@code false} for
	 * those which are not (thus prints usage)
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// sender - object associated with command sender (instanceof Player desired)
		// command - command object corresponding to entry in plugin.yml
		// label - alias used (/thisway or /tw)
		// args[] - what it says on the tin
		for(PermissionAttachmentInfo i : sender.getEffectivePermissions())
			if(i.getPermission().startsWith("thisway"))
				sender.sendMessage(i.getPermission()+": "+i.getValue());
		return false;
	}
}
