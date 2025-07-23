package net.toydotgame.Thisway.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.toydotgame.Thisway.Configurator;
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
			return Thisway.syntaxError(player, "Invalid distance value! Must be at least 1");
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
					player.sendMessage("Debug mode enabled for this teleport");
					break;
				case "true":
				case "false":
					player.sendMessage(ChatColor.YELLOW+"Using \"true\" or \"false\" to specify debug mode was removed "
						+"in v1.4. Please use the argument \"debug\" to turn on debug mode, or omit it to disable");
				default:
					return Thisway.syntaxError(player, "Invalid debug mode toggle value!");
			}
		}
		
		// Syntax good, teleport:
		main(distance);
		return true;
	}
	
	/**
	 * Teleports the player {@code teleportDistance} blocks in the direction
	 * they're facing
	 * @param teleportDistance Distance to teleport
	 */
	private static void main(int teleportDistance) {
		debug("Teleporting "+teleportDistance+" blocks...");
		
		DirectionVector facing = new DirectionVector(player.getEyeLocation());
		
		// Define our positions to check stuff about:
		Location start = player.getLocation(); // Player location
		defineDestination(facing, start, teleportDistance);
		
		String teleportDistanceString = " ("+teleportDistance+" block"+(teleportDistance==1 ? "" : "s")+")"; // Used later
		debug("Teleporting from: "+toBlockString(start));
		debug("Teleporting to: "+toBlockString(destination)+teleportDistanceString);
		debug("    Eye height: "+player.getEyeHeight());
		
		debug("");
		debug(ChatColor.BOLD+"Safety tests:");
		
		// CHECK: If the destination is half in the ground, redefine everything
		// to be 1 block up. If this breaks things further down, oh well, we tried.
		boolean feetInNonSolid = TeleportTests.testAndLogBoolean("feetInNonSolid",
			!destination.getBlock().getType().isSolid());
		if(!feetInNonSolid) {
			debug("    Destination is solid, attempting to move 1 block up");
			start = start.add(0, 1, 0);
			defineDestination(facing, start, teleportDistance); // Redefine destination
		}
		
		TeleportTests tests = new TeleportTests(player, destination, destinationEye, destinationGround);
		if(!tests.testAll()) return;
		debug("");
		
		String playerName = player.getName();
		String world = player.getWorld().getName();
		debug("Player: "+playerName);
		debug("World: "+world);
		
		player.teleport(destination);
		
		if(Configurator.fetchToggle(Option.LOG_TELEPORTS))
			/*Bukkit.getServer().broadcast(""+ChatColor.GRAY+ChatColor.ITALIC+"[Thisway: " // Concat empty string otherwise the `ChatColor`s clash
				+playerName+" teleported from ("
				+toBlockString(start.subtract(0, (feetInNonSolid ? 0 : 1), 0))+") to ("
				+toBlockString(destination)+")"+teleportDistanceString+"]",
				Server.BROADCAST_CHANNEL_ADMINISTRATIVE);*/
			Bukkit.getLogger().info(playerName+" teleported from ("
				+toBlockString(start.subtract(0, (feetInNonSolid ? 0 : 1), 0))+") to ("
				+toBlockString(destination)+")"+teleportDistanceString);
		
		if(debugMode) player.sendMessage("Teleport complete");
	}
	
	/**
	 * Prints a debug message if debug mode is enabled.
	 * @param message Message to print
	 */
	static void debug(String message) {
		if(!debugMode) return;
		
		player.sendMessage(""+ChatColor.GRAY+ChatColor.ITALIC+message);
	}
	
	/**
	 * Acts as an alternative of {@link Location#toString()}. World and rotation
	 * information, as well as the double precision decimals, and the formatting
	 * of the regular Location String is cluttered and irrelevant when we just
	 * want to print out a location as a coordinate in the current world.
	 * @param l {@link Location} to use
	 * @return String of X, Y, and Z coordinates of this Location, delimited by
	 * "{@code , }"
	 */
	private static String toBlockString(Location l) {
		return l.getBlockX()+", "+l.getBlockY()+", "+l.getBlockZ();
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
