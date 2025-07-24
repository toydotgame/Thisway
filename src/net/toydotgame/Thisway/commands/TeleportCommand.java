package net.toydotgame.Thisway.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.linfoot.bukkit.utils.MessageUtils;
import net.toydotgame.Thisway.Configurator;
import net.toydotgame.Thisway.Lang;
import net.toydotgame.Thisway.Option;
import net.toydotgame.Thisway.Thisway;

/**
 * Static class to handle the actual teleportation function of Thisway.
 * <dl><dt><b>Created on:</b></dt><dd>2020-12-26<br>
 * Originally as {@code io.github.toydotgame.Thisway.Thisway}</dd>
 * <dt><b>Re-created on:</b></dt><dd>2025-07-15</dd></dl>
 * @author toydotgame
 */
public final class TeleportCommand {
	private TeleportCommand() {} // Static class
	
	private static Player player;
	private static boolean debugMode;
	private static Location destination, destinationEye, destinationGround;
	
	/**
	 * Validates the inputs of a teleport command and runs it if the syntax is
	 * correct. Syntactically speaking, by now we know we have:
	 * <ul>
	 * 	<li>1 or 2 arguments</li>
	 * 	<li>An instance of {@link org.bukkit.entity.Player Player} sending the
	 * command</li>
	 * 	<li>By elimination, this command is a teleport command, not an about
	 * command</li>
	 * </ul>
	 * However, we do <b>not</b> know whether or not—provided the player has
	 * entered the correct syntax for enabling debug mode—the player has debug
	 * permissions. Nor do we know if they have teleport permissions. Therefore,
	 * these, and the first argument being ∈ℕ∖{0}, must be checked.
	 * @param player {@link org.bukkit.entity.Player (Player)} casted {@link
	 * org.bukkit.command.CommandSender CommandSender} instance to speak to
	 * @param args Copy of {@link
	 * Thisway#onCommand(CommandSender, org.bukkit.command.Command, String,
	 * String[]) onCommand(...)}'s args
	 * @return {@code true} for normal execution, {@code false} for a syntax
	 * error (meaning that one-liner {@code return parseAndRun(...)}s are
	 * possible.
	 * @see #main(int)
	 */
	public static boolean parseAndRun(Player player, String[] args) {
		TeleportCommand.player = player;
		int distance = 0;
		
		// CHECK: Root teleport permission
		if(!Thisway.testForPermission(player, "teleport")) return true; // Not a syntax error
		
		// CHECK: First argument is a positive int
		try {
			distance = Integer.parseInt(args[0]);
			if(distance < 1) throw new NumberFormatException();
		} catch(NumberFormatException e) {
			return Thisway.syntaxError(player, "tp.syntax.distance");
		}
		
		// CHECK: Second argument is "debug" and not a legacy or invalid value
		debugMode = false; // Debug mode status carries over from last execution
		                   // (due to static field). So reset it
		if(args.length == 2) {
			switch(args[1].toLowerCase()) {
				case "debug":
					// CHECK: Player has permission to use debug mode
					if(!Thisway.testForPermission(player, "debug")) return true;
					
					debugMode = true;
					player.sendMessage(Lang.create("debug.enabled"));
					break;
				case "true":
				case "false":
					player.sendMessage(ChatColor.YELLOW+Lang.create("tp.syntax.legacy-debug"));
				default:
					return Thisway.syntaxError(player, "tp.syntax.invalid-debug");
			}
		}
		
		// Syntax good, teleport:
		teleport(distance);
		return true;
	}
	
	/**
	 * Teleports the player {@code teleportDistance} blocks in the direction
	 * they're facing
	 * @param teleportDistance Distance to teleport
	 */
	private static void teleport(int teleportDistance) {
		debugRule("rule.begin");
		if(teleportDistance == 1) debugLine("begin.singular", teleportDistance);
		else debugLine("begin.plural", teleportDistance);
		
		DirectionVector facing = new DirectionVector(player.getEyeLocation());
		
		// Define our positions to check stuff about:
		Location start = player.getLocation(); // Player location
		defineDestination(facing, start, teleportDistance);
		
		String teleportDistanceString = (teleportDistance == 1
			?Lang.create("debug.distance.singular", teleportDistance)
			:Lang.create("debug.distance.plural", teleportDistance));
		debugLine("from", locationToArray(start));
		debug(Lang.create("debug.to", locationToArray(destination))
			+teleportDistanceString);
		debugLine("eye-height", player.getEyeHeight());
		
		debugHeading("checks.heading");
		
		// CHECK: If the destination is half in the ground, redefine everything
		// to be 1 block up. If this breaks things further down, oh well, we tried.
		boolean feetInNonSolid = TeleportTests.testAndLogBoolean(player, "feetInNonSolid",
			!destination.getBlock().getType().isSolid());
		if(!feetInNonSolid) {
			debugLine("checks.destination-moved");
			start = start.add(0, 1, 0);
			defineDestination(facing, start, teleportDistance); // Redefine destination
		}
		
		TeleportTests tests = new TeleportTests(player, destination, destinationEye, destinationGround);
		if(!tests.testAll()) return;
		debug((String)null);
		
		String playerName = player.getName();
		String world = player.getWorld().getName();
		debugLine("player", playerName);
		debugLine("world", world);
		
		player.teleport(destination);
		
		if(Configurator.fetchToggle(Option.LOG_TELEPORTS))
			Thisway.logger.info(Lang.create("tp.log", playerName,
				locationToArray(start.subtract(0, (feetInNonSolid ? 0:1), 0)),
				locationToArray(destination))+teleportDistanceString);
		
		debug((String)null);
		debug(ChatColor.RESET+Lang.create("debug.complete"));
		debugRule("rule.end");
	}
	
	static void debug(String s) {
		if(!debugMode) return;
		
		if(s == null || s.equals("")) player.sendMessage((String)null);
		else player.sendMessage(""+ChatColor.GRAY+s);
	}
	
	/**
	 * Prints a debug message if debug mode is enabled.
	 * @param message Message to print
	 */
	static void debugLine(String translationKey, Object... args) {
		if(!debugMode) return;
		
		if(translationKey == null || translationKey == "")
			debug(translationKey);
		else debug(Lang.create("debug."+translationKey, args));
	}
	
	static void debugHeading(String translationKey, Object... args) {
		if(!debugMode) return;
		
		debug((String)null);
		String heading = ""+ChatColor.RESET+ChatColor.BOLD+Lang.create("debug."+translationKey, args);
		
		// Mimic of AboutCommand#printHeading(String, Object...):
		// TODO: Move this code to AboutCommand because its redundantly specified twice now
		int i = heading.indexOf(':');
		if(i >= 0) { // If the heading contains a key-value pair, format it as such:
			heading = heading.substring(0, ++i)+ChatColor.RESET+ChatColor.GRAY
				+heading.substring(i);
		}
		
		debug(heading);
	}
	
	private static void debugRule(String translationKey) {
		if(!debugMode) return;
		
		MessageUtils.sendHorizontalRule(
			player, '-', ChatColor.YELLOW, Lang.create("debug."+translationKey), ChatColor.DARK_GREEN, ChatColor.BOLD);
	}
	
	private static Object[] locationToArray(Location l) {
		return new Integer[] {l.getBlockX(), l.getBlockY(), l.getBlockZ()};
	}
	
	/**
	 * Redefines {@link #destination}, {@link #destinationEye}, and {@link
	 * #destinationGround} in terms of the input {@link Location} {@code start}.
	 * It applies a {@link DirectionVector} ({@code facing}, of magnitude {@code
	 * distance}) to {@code start}, and applies the appropriate modifications to
	 * the destination Location to yield the other two.<br>
	 * <br>
	 * It is useful to delegate this call to the method because these operations
	 * are called twice:
	 * <ol>
	 * 	<li>(Always) when getting the teleport destination for the first time</li>
	 * 	<li>(Only if {@link #destination} is an invalid spot) when the teleport
	 * destination is clipped into terrain, we try moving it up by 1 block and
	 * redefining it</li>
	 * </ol>
	 * @param facing {@link DirectionVector} to use as a mask/to create a
	 * movement vector to determine <i>where</i> the destination is
	 * @param start The player's current location
	 * @param distance Distance the player is teleporting to. (How far away the
	 * destination should be)
	 * @see DirectionVector#createPositionVector(int)
	 */
	private static void defineDestination(DirectionVector facing, Location start, int distance) {
		destination = start.clone()                                // Target location (=start+distance)
			.add(facing.createPositionVector(distance));
		destinationEye = destination.clone()                       // Target location+eye height
			.add(0, player.getEyeHeight(), 0);
		destinationGround = destination.clone().subtract(0, 1, 0); // Target location-1 y (standing on block)
	}
}
