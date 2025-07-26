package net.toydotgame.Thisway;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.toydotgame.Thisway.commands.AboutCommand;
import net.toydotgame.Thisway.commands.TeleportCommand;

/**
 * Main class for Spigot hooks to execute our code starting from.
 * <dl><dt><b>Created on:</b></dt><dd>2020-12-26<br>
 * Originally as {@code io.github.toydotgame.Thisway.Main}</dd>
 * <dt><b>Re-created on:</b></dt><dd>2025-07-15</dd></dl>
 * @author toydotgame
 */
public final class Thisway extends JavaPlugin {
	// Don't create a constructor. Spigot will run the default JavaPlugin()
	// constructor instead and that's all we really need
	
	private UpdateChecker updates;
	
	/**
	 * Check for updates, then load config, messages, and permissions. Log a
	 * simple message to follow the Spigot logs (that only say that they've
	 * <i>begun</i> enabling Thisway).
	 * {@inheritDoc}, and after the constructor for this class.
	 */
	@Override
	public void onEnable() {
		updates = new UpdateChecker(this);
		updates.logUpdates(); // TODO: ugh make nicer
		
		Configurator.loadConfig(this);
		
		Lang.loadMessages(this); // loadMessages() depends on having a loaded config
		
		Lang.log("on-enable");
	}
	
	/**
	 * Main command execution logic.
	 * @return {@code true} for syntatically-correct inputs, {@code false} for
	 * incorrect inputs (thus prints usage)
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {		
		// CHECK: Sender must be a player
		if(!(sender instanceof Player)) {
			sender.sendMessage("[Thisway] "+ChatColor.RED+Lang.create("syntax.console-sender"));
			return true; // Not a syntax error
		}
		
		// If first arg is "about", go parse the rest over there and run if
		// possible:
		if(args[0].equalsIgnoreCase("about"))
			return new AboutCommand(this, sender, args).run();
		
		// Otherwise, it's probably a teleport:
		return new TeleportCommand(this, sender, args).run();
	}
	
	public UpdateChecker getUpdates() {
		return updates;
	}
}
