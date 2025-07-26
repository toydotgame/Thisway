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
import net.toydotgame.Thisway.UpdateChecker;
import net.toydotgame.Thisway.commands.utils.CommandUtils;
import net.toydotgame.Thisway.commands.utils.MessageUtils;

/**
 * Static class to handle the {@code /thisway about} command, that prints
 * information about the executor and the plugin.
 * <dl><dt><b>Created on:</b></dt><dd>2025-07-15</dd></dl>
 * @author toydotgame
 */
public class AboutCommand {
	private final Thisway plugin;
	private final CommandSender sender;
	private int status = CommandUtils.VALID;
	
	/**
	 * Creates a new {@link AboutCommand} instance. This constructor also acts
	 * as a parser to check command syntax, so will probably send syntax errors
	 * and the like to {@code sender} if applicable. As constructor's return
	 * their object only, we use the {@link #isValid} boolean, which on
	 * initalisation is {@code true}, and is set {@code false} if one of the
	 * aforementioned errors is hit. This way, instance methods of this class
	 * will know to fail if the command construction had failed here.
	 * @param plugin {@link Thisway} plugin instance that called us
	 * @param sender Player who executed the command
	 * @param args Arguments sent to {@code /thisway} (including "{@code
	 * about}")
	 */
	public AboutCommand(Thisway plugin, CommandSender sender, String[] args) {
		this.plugin = plugin;
		this.sender = sender;
		
		if(!CommandUtils.testForPermission(sender, "about")) { // Prints an error to chat if false
			this.status = CommandUtils.PERMISSION_ERROR;
			return; // Don't bother checking further
		}
		
		// Syntax error is more severe and so due to execution order,
		// PERMISSION_ERROR will be overriden by this if applicable:
		if(args.length != 1) {
			CommandUtils.syntaxError(sender, "syntax.args-length");
			this.status = CommandUtils.SYNTAX_ERROR;
		}
	}
	
	public boolean run() {
		// First exit if this command instance is invalid:
		Boolean b = CommandUtils.getReturnValueIfError(status);
		if(b != null) return b;
		
		MessageUtils.sendHorizontalRule(sender, '-', ChatColor.YELLOW, "Thisway", ChatColor.DARK_GREEN, ChatColor.BOLD);
		
		// Copyright and history
		sender.sendMessage("(c) 2020, 2025 toydotgame");
		sender.sendMessage(""+ChatColor.GRAY+ChatColor.ITALIC+Lang.create("about.spiel"));
		
		// Attributions
		printHeading("section.attributions.heading");
		printValue("section.attributions.list");
		
		// Thisway version info
		UpdateChecker u = plugin.getUpdates();
		printHeading("section.thisway.heading");
		printValue("section.thisway.installed",
			(u.SPIGOTMC_VERSION != null                            // Colour for local version
				?(u.IS_UP_TO_DATE ? ChatColor.GREEN:ChatColor.RED) // If SpigotMC version is valid, we can use IS_UP_TO_DATE to format our local version as good or not
				:ChatColor.YELLOW)                                               // If SpigotMC version is _invalid_, then go yellow instead
			+u.INSTALLED_VERSION);
		printValue("section.thisway.spigotmc",
			(u.SPIGOTMC_VERSION != null
			?ChatColor.GREEN+u.SPIGOTMC_VERSION // If SpigotMC version is valid, make it green always
			:ChatColor.YELLOW+"<unknown>"));                  // If not, say "<unknown>" in yellow
		
		// Minecraft/server version info
		printHeading("section.minecraft.heading", Bukkit.getBukkitVersion().split("-")[0]);
		printValue("section.minecraft.vendor", Bukkit.getBukkitVersion());
		printValue("section.minecraft.source", Bukkit.getVersion().split(" ")[0]);
		
		// Permissions
		printHeading("section.permissions.heading");
		for(Permission p : getPluginPermissions())
			printValue("section.permissions.permission",
				p.getName().replaceFirst("^thisway\\.", ""),
				MessageUtils.boolToWord(sender.hasPermission(p)));
		printValue("section.permissions.is-op", MessageUtils.boolToWord(sender.isOp()));
		
		// Config
		printHeading("section.config.heading");
		Configurator.fetchAll().forEach((k, v) -> {
			v = convertPossibleBooleanToPrettyPrint(v);
			String d = convertPossibleBooleanToPrettyPrint(
				Option.get(k).defaultValue.toString());
			
			printValue("section.config.option", k, v, d);
		});
		
		MessageUtils.sendHorizontalRule(sender, '-', ChatColor.YELLOW, null, ChatColor.DARK_GREEN, ChatColor.BOLD);
		
		return true;
	}
	
	private void printHeading(String translationKey, Object... args) {
		MessageUtils.printHeading(sender, "about."+translationKey, args);
	}
	
	private void printValue(String translationKey, Object... args) {
		MessageUtils.printKeyValue(sender, "about."+translationKey, args);
	}
	
	private static String convertPossibleBooleanToPrettyPrint(String s) {
		if(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"))
			return MessageUtils.boolToWord(Boolean.parseBoolean(s));
		// Else, not a boolean:
		return s;
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
	private List<Permission> getPluginPermissions() {
		// Create a copy of the live permissions:
		List<Permission> permissions = new ArrayList<Permission>(plugin.getDescription().getPermissions());
		// Returned list has a random order, so fix it:
		permissions.sort((a, b) -> {
			return a.getName().compareTo(b.getName());
		});
		
		return permissions;
	}
}
