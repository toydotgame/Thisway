package net.toydotgame.Thisway.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import net.toydotgame.Thisway.Thisway;

public final class AboutCommand {
	private AboutCommand() {} // Static class
	private static CommandSender sender;
	
	/**
	 * Assures that only the argument {@code "about"} exists, then prints the
	 * about screen.
	 * @param sender {@link org.bukkit.command.CommandSender CommandSender} to
	 * speak to
	 * @param args Copy of {@link
	 * Thisway#onCommand(CommandSender, org.bukkit.command.Command, String, String[])
	 * onCommand(...)}'s args
	 * @return {@code true} for normal execution, {@code false} for a syntax
	 * error (meaning that one-liner {@code return parseAndRun(...)}s are
	 * possible.
	 * @see #main()
	 */
	public static boolean parseAndRun(CommandSender sender, String[] args) {
		AboutCommand.sender = sender;
		
		if(args.length != 1)
			return Thisway.syntaxError(sender, "Too many arguments!");
		
		main();
		
		return true;
	}
	
	private static void main() {
		printRule("Thisway");
		
		sender.sendMessage(ChatColor.BOLD+"Thisway version");
		printNamedValue(ChatColor.RESET+"- Local", "v"+Thisway.updates.INSTALLED_VERSION);
		printNamedValue(ChatColor.RESET+"- Latest", "v"+Thisway.updates.SPIGOTMC_VERSION);
		printNamedValue("Up to date?", boolToWord(Thisway.updates.IS_UP_TO_DATE));
		sender.sendMessage((String)null); // Print an empty line because prepending '\n' doesn't work prettily
		
		printNamedValue("Minecraft version", "Release "+Bukkit.getBukkitVersion().split("-")[0]);
		printNamedValue(ChatColor.RESET+"- Vendor", Bukkit.getBukkitVersion());
		printNamedValue(ChatColor.RESET+"- Vendor source", Bukkit.getVersion().split(" ")[0]);
		
		sender.sendMessage((String)null);
		sender.sendMessage(ChatColor.BOLD+"Permissions");
		for(Permission p : Thisway.getPluginPermissions())
			printNamedValue(
				ChatColor.GRAY+"- "+ChatColor.ITALIC+p.getName().replaceFirst("^thisway\\.", ""),
				boolToWord(sender.hasPermission(p)));
		printNamedValue("Operator?", boolToWord(sender.isOp()));
		
		printRule();
	}
	
	private static void printNamedValue(String key, String value) {
		sender.sendMessage(ChatColor.BOLD+key+":"+ChatColor.RESET+" "+ChatColor.GRAY+value);
	}
	
	private static void printRule(String optionalText) {
		String hr = "-----------------------------------------------------"; // Assumes default chat width
		if(optionalText.length() > 0) {
			final int textLength = Math.min(optionalText.length()+4, hr.length()); // Pick smallest one to avoid StringIndexOutOfBounds
			optionalText = "[ "+ChatColor.RESET+ChatColor.DARK_GREEN+ChatColor.BOLD+optionalText+ChatColor.YELLOW+" ]";
			final int startingIndex = (hr.length()-textLength)/2;
			hr = (new StringBuffer(hr))
				.replace(startingIndex, startingIndex+textLength, optionalText)
				.toString()+"-"; // Seems to be consistently off-by-one. Adding this extra char makes it a little over rather than a lot under
		}
		sender.sendMessage(ChatColor.YELLOW+hr);
	}
	private static void printRule() {
		printRule("");
	}
	
	private static String boolToWord(boolean b) {
		ChatColor r = ChatColor.RED, g = ChatColor.GREEN;
		return b ? g+"Yes":r+"No";
	}
}
