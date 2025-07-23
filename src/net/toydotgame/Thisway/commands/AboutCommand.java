package net.toydotgame.Thisway.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import net.toydotgame.Thisway.Configurator;
import net.toydotgame.Thisway.Lang;
import net.toydotgame.Thisway.Option;
import net.toydotgame.Thisway.Thisway;

/**
 * Static class to handle the {@code /thisway about} command, that prints
 * information about the executor and the plugin.
 * <dl><dt><b>Created on:</b></dt><dd>2025-07-15</dd></dl>
 * @author toydotgame
 */
public final class AboutCommand {
	private AboutCommand() {} // Static class
	private static CommandSender sender;
	
	/**
	 * Assures that only the argument {@code "about"} exists, then prints the
	 * about screen.
	 * @param sender {@link org.bukkit.command.CommandSender CommandSender} to
	 * speak to
	 * @param args Copy of {@link
	 * Thisway#onCommand(CommandSender, org.bukkit.command.Command, String,
	 * String[]) onCommand(...)}'s args
	 * @return {@code true} for normal execution, {@code false} for a syntax
	 * error (meaning that one-liner {@code return parseAndRun(...)}s are
	 * possible.
	 * @see #main()
	 */
	public static boolean parseAndRun(CommandSender sender, String[] args) {
		AboutCommand.sender = sender;
		
		// CHECK: Sender has permissions
		if(!Thisway.testForPermission(sender, "about")) return true; // Not a syntax error
		
		// CHECK: Only 1 argument (not 2) for about
		if(args.length != 1)
			return Thisway.syntaxError(sender, "syntax.args-length");
		
		// Syntax good, run main:
		main();		
		return true;
	}
	
	/**
	 * Print some version information, and fetch permissions for this plugin,
	 * listing which the player does and doesn't have.
	 */
	private static void main() {
		printRule("Thisway");
		
		sender.sendMessage("(c) 2020, 2025 toydotgame");
		sender.sendMessage(ChatColor.GRAY+String.join("\n"+ChatColor.RESET+ChatColor.GRAY,
			ChatColor.ITALIC+Lang.create("about.spiel"),
			ChatColor.BOLD+Lang.create("about.section.attributions.heading"),
			"- "+String.join("\n- ", Lang.fetchList("about.section.attributions.list"))));
		sender.sendMessage((String) null); // Print an empty line because printing whitespace only causes weird display
		
		sender.sendMessage(ChatColor.BOLD+Lang.create("about.section.thisway.heading"));
		sender.sendMessage("- "+Lang.create("about.section.thisway.installed", Thisway.updates.INSTALLED_VERSION));
		sender.sendMessage("- "+Lang.create("about.section.thisway.spigotmc", Thisway.updates.SPIGOTMC_VERSION));
		sender.sendMessage(ChatColor.BOLD+Lang.create("about.section.thisway.up-to-date", boolToWord(Thisway.updates.IS_UP_TO_DATE)));
		sender.sendMessage((String)null);
		
		sender.sendMessage(ChatColor.BOLD+Lang.create("about.section.minecraft.heading", Bukkit.getBukkitVersion().split("-")[0]));
		sender.sendMessage("- "+Lang.create("about.section.minecraft.vendor", Bukkit.getBukkitVersion()));
		sender.sendMessage("- "+Lang.create("about.section.minecraft.source", Bukkit.getVersion().split(" ")[0]));
		sender.sendMessage((String)null);
		
		sender.sendMessage(ChatColor.BOLD+Lang.create("about.section.permissions.heading"));
		for(Permission p : getPluginPermissions())
			sender.sendMessage("- "+Lang.create("about.section.permissions.permission",
				p.getName().replaceFirst("^thisway\\.", ""), boolToWord(sender.hasPermission(p))));
		sender.sendMessage(ChatColor.BOLD+Lang.create("about.section.permissions.is-op", boolToWord(sender.isOp())));
		sender.sendMessage((String)null);
		
		sender.sendMessage(ChatColor.BOLD+Lang.create("about.section.config.heading"));
		Configurator.fetchAll().forEach((k, v) -> {
			v = convertPossibleBooleanToPrettyPrint(v);
			String d = convertPossibleBooleanToPrettyPrint(
				Option.get(k).defaultValue.toString());
			
			sender.sendMessage("- "+Lang.create("about.section.config.option", k, v, d));
				
			/*printNamedValue(ChatColor.GRAY+"- "+ChatColor.ITALIC+k,
				boolToWord(v)+ChatColor.RESET+ChatColor.GRAY+ChatColor.ITALIC
				+(Option.get(k)!=null // Catch NullPointerException
					? " (defaults to "+boolToWord((Boolean)Option.get(k).defaultValue)
						+ChatColor.RESET+ChatColor.GRAY+ChatColor.ITALIC+")"
					:""));*/
		});
		
		printRule();
	}
	
	private static String convertPossibleBooleanToPrettyPrint(String s) {
		if(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"))
			return boolToWord(Boolean.parseBoolean(s));
		// Else, not a boolean
		return s;
	}
	
	/**
	 * Prints a key-value pair to chat as a bold key with a dimmed value.
	 * @param key Name of the thing
	 * @param value Value of the thing
	 */
	private static void printNamedValue(String key, String value) {
		sender.sendMessage(ChatColor.BOLD+key+":"+ChatColor.RESET+" "+ChatColor.GRAY+value);
	}
	
	/**
	 * Print a horizontal rule to chat. Works best with strings that
	 * <i>aren't</i> super long and fill most of a line.
	 * @param optionalText Text to print in the middle of the horizontal line
	 * (i.e. as a heading/title). If this string is empty, no text is overlaid
	 * @see #printRule()
	 */
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
	/**
	 * Overload for {@link #printRule(String)} that passes an empty string to
	 * {@code printRule(...)} to accomplish drawing a plain ol' rule with no
	 * text.
	 */
	private static void printRule() {
		printRule("");
	}
	
	/**
	 * Converts an input boolean value to a coloured text string "Yes" or "No".
	 * @param b Input formula or expression, or boolean constant to evaluate
	 * @return A green "Yes" for printing to chat if {@code b == true}, or a red
	 * "No" if {@code b == false}
	 */
	static String boolToWord(boolean b) {
		return
			(b ? ChatColor.GREEN+Lang.create("boolean.true")
				:ChatColor.RED+Lang.create("boolean.false"))
			+ChatColor.RESET;
	}
	
	/**
	 * Returns a copy of {@link
	 * org.bukkit.plugin.PluginDescriptionFile#getPermissions()
	 * this.getDecription().getPermission()}, sorted alphabetically. The value
	 * yield directly from {@code getPermissions()} isn't able to be sorted
	 * because it is an object reference to the live server permissions in
	 * memory. Therefore, when {@link #onEnable()} is reached for Thisway, we
	 * store that reference statically, and then duplicate it as a new {@link
	 * java.util.ArrayList} which we then sort and return.
	 * @return Permissions from {@code plugin.yml}, sorted alphabetically
	 */
	private static List<Permission> getPluginPermissions() {
		// Create a copy of the live permissions:
		List<Permission> permissions = new ArrayList<Permission>(Thisway.permissionsReference);
		// Returned list has a random order, so fix it:
		permissions.sort((a, b) -> {
			return a.getName().compareTo(b.getName());
		});
		
		return permissions;
	}
}
