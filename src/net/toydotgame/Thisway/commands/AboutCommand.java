package net.toydotgame.Thisway.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import dev.linfoot.bukkit.utils.MessageUtils;
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
		MessageUtils.sendHorizontalRule(sender, '-', ChatColor.YELLOW, "Thisway", ChatColor.DARK_GREEN, ChatColor.BOLD);
		
		// Copyright and history
		sender.sendMessage("(c) 2020, 2025 toydotgame");
		sender.sendMessage(""+ChatColor.GRAY+ChatColor.ITALIC+Lang.create("about.spiel"));
		
		// Attributions
		printHeading("about.section.attributions.heading");
		printValue("about.section.attributions.list");
		
		// Thisway version info
		printHeading("about.section.thisway.heading");
		printValue("about.section.thisway.installed",
			(Thisway.updates.SPIGOTMC_VERSION != null                            // Colour for local version
				?(Thisway.updates.IS_UP_TO_DATE ? ChatColor.GREEN:ChatColor.RED) // If SpigotMC version is valid, we can use IS_UP_TO_DATE to format our local version as good or not
				:ChatColor.YELLOW)                                               // If SpigotMC version is _invalid_, then go yellow instead
			+Thisway.updates.INSTALLED_VERSION);
		printValue("about.section.thisway.spigotmc",
			(Thisway.updates.SPIGOTMC_VERSION != null
			?ChatColor.GREEN+Thisway.updates.SPIGOTMC_VERSION // If SpigotMC version is valid, make it green always
			:ChatColor.YELLOW+"<unknown>"));                  // If not, say "<unknown>" in yellow
		
		// Minecraft/server version info
		printHeading("about.section.minecraft.heading", Bukkit.getBukkitVersion().split("-")[0]);
		printValue("about.section.minecraft.vendor", Bukkit.getBukkitVersion());
		printValue("about.section.minecraft.source", Bukkit.getVersion().split(" ")[0]);
		
		// Permissions
		printHeading("about.section.permissions.heading");
		for(Permission p : getPluginPermissions())
			printValue("about.section.permissions.permission",
				p.getName().replaceFirst("^thisway\\.", ""),
				boolToWord(sender.hasPermission(p)));
		printValue("about.section.permissions.is-op", boolToWord(sender.isOp()));
		
		// Config
		printHeading("about.section.config.heading");
		Configurator.fetchAll().forEach((k, v) -> {
			v = convertPossibleBooleanToPrettyPrint(v);
			String d = convertPossibleBooleanToPrettyPrint(
				Option.get(k).defaultValue.toString());
			
			printValue("about.section.config.option", k, v, d);
		});
		
		MessageUtils.sendHorizontalRule(sender, '-', ChatColor.YELLOW, null, ChatColor.DARK_GREEN, ChatColor.BOLD);
	}
	
	private static void printHeading(String translationKey, Object... args) {
		// Print empty line before heading. Using a null string causes less
		// visual issues than "\n" and uses less memory than ""
		sender.sendMessage((String)null);
		
		String heading = ChatColor.BOLD+Lang.create(translationKey, args);
		int i = heading.indexOf(':');
		if(i >= 0) { // If the heading contains a key-value pair, format it as such:
			heading = heading.substring(0, ++i)+ChatColor.RESET+ChatColor.GRAY
				+heading.substring(i);
		}
		
		sender.sendMessage(heading);
	}
	
	static void printValue(CommandSender cs, String translationKey, Object... args) {
		String line = "", linePrefix = ChatColor.GRAY+"- ";
		if(Lang.isList(translationKey))
			for(String s : Lang.fetchList(translationKey))
				cs.sendMessage(linePrefix+s);
		else {
			for(int i = 0; i < args.length; i++) // Fix formatting resets:
				args[i] += ChatColor.GRAY.toString();
			line = linePrefix+Lang.create(translationKey, args);
			cs.sendMessage(line);
		}
	}
	private static void printValue(String translationKey, Object... args) {
		printValue(sender, translationKey, args);
	}
	
	private static String convertPossibleBooleanToPrettyPrint(String s) {
		if(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"))
			return boolToWord(Boolean.parseBoolean(s));
		// Else, not a boolean
		return s;
	}
	
	/**
	 * Converts an input boolean value to a coloured text string "Yes" or "No".
	 * @param b Input formula or expression, or boolean constant to evaluate
	 * @return A green "Yes" for printing to chat if {@code b == true}, or a red
	 * "No" if {@code b == false}
	 */
	static String boolToWord(boolean b) {
		return (b ? ChatColor.GREEN+Lang.create("boolean.true")
			:ChatColor.RED+Lang.create("boolean.false"))+ChatColor.RESET;
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
