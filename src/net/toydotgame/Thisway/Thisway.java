package net.toydotgame.Thisway;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
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
	
	// Even though this class really isn't static, I'm going to treat it as such
	// given AFAIK, you can't load the same plugin twice (or at least in any way
	// where conflicts could arise). I think
	public static UpdateChecker updates;
	// Permissions can change after enable time, so store a pointer here and get
	// it when needed:
	public static List<Permission> permissionsReference;
	
	/**
	 * Check for updates, then load config, messages, and permissions. Log a
	 * simple message to follow the Spigot logs (that only say that they've
	 * <i>begun</i> enabling Thisway).
	 * {@inheritDoc}, and after the constructor for this class.
	 */
	@Override
	public void onEnable() {
		updates = new UpdateChecker(this);
		updates.logUpdates();
		
		Configurator.loadConfig(this);
		
		Lang.loadMessages(this); // loadMessages() depends on having a loaded config
		
		permissionsReference = getDescription().getPermissions();
		
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
		
		// CHECK: Must have 1 or 2 args
		if(args.length < 1 || args.length > 2)
			return syntaxError(sender, "syntax.args-length");
		
		// BRANCH: If we're running `about`, run and end here
		if(args[0].equalsIgnoreCase("about"))
			return AboutCommand.parseAndRun(sender, args);
		
		// Else, we're just doing a normal teleport
		return TeleportCommand.parseAndRun((Player)sender, args);
	}
	
	/**
	 * Prints an error message in red to the console/player {@code sender}.
	 * Returns {@code false} so that you can do {@code return syntaxError(...)}
	 * in {@link #onCommand(CommandSender, Command, String, String[])
	 * onCommand(...)} as a one-liner.
	 * @param sender {@link org.bukkit.command.CommandSender CommandSender} to
	 * send this error to
	 * @param message Error message
	 * @return {@code false}
	 */
	public static boolean syntaxError(CommandSender sender, String key, Object... args) {
		sender.sendMessage(ChatColor.RED+Lang.create(key, args));
		return false;
	}
	
	/**
	 * Checks if the provided {@link org.bukkit.entity.Player Player} has the
	 * provided {@code thisway.<permission>}. If not, an error message is sent.
	 * @param player Player to check for the given permission, and send an error
	 * to if the permission check fails
	 * @param permission Permission name (excluding "{@code thisway.}")
	 * @return {@code true} if the player has the permission, {@code false} if
	 * not
	 */
	public static boolean testForPermission(CommandSender player, String permission) {
		if(player.hasPermission("thisway."+permission)) return true;
		
		player.sendMessage(ChatColor.RED+Lang.create("no-permission", permission));
		return false;
	}
}
