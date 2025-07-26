package net.toydotgame.Thisway.commands.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import net.toydotgame.Thisway.Lang;

public class CommandUtils {
	private CommandUtils() {} // Static class
		
	public static final int VALID = 0;
	public static final int PERMISSION_ERROR = 1;
	public static final int SYNTAX_ERROR = 2;
	
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
	
	public static Boolean getReturnValueIfError(int status) {
		switch(status) {
			default: // Shouldn't occur but just in case
			case SYNTAX_ERROR:
				return false;
			case PERMISSION_ERROR:
				return true;
			case VALID:
				return null; // Null indicates to continue with execution
		}
	}
}
