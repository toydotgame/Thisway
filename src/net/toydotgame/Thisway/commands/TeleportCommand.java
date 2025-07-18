package net.toydotgame.Thisway.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.toydotgame.Thisway.Configurator;
import net.toydotgame.Thisway.Thisway;

/**
 * Static class to handle the actual teleportation function of Thisway.
 * <dt><b>Created on:</b></dt><dd>2020-12-26<br>
 * Originally as {@code io.github.toydotgame.Thisway.Thisway}</dd>
 * <dt><b>Re-created on:</b></dt><dd>2025-07-15</dd>
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
	 * @param player {@link org.bukkit.entity.Player (Player)} casted {@link
	 * org.bukkit.command.CommandSender CommandSender} instance to speak to
	 * @param args Copy of {@link
	 * Thisway#onCommand(CommandSender, Command, String, String[])
	 * onCommand(...)}'s args
	 * @return {@code true} for normal execution, {@code false} for a syntax
	 * error (meaning that one-liner {@code return parseAndRun(...)}s are
	 * possible.
	 * @see #main()
	 */
	public static boolean parseAndRun(Player player, String[] args) {
		TeleportCommand.player = player;
		int distance = 0;
		
		// CHECK: First argument is a positive int
		try {
			distance = Integer.parseInt(args[0]);
			if(distance < 1) throw new NumberFormatException();
		} catch(NumberFormatException e) {
			return Thisway.syntaxError(player, "Invalid distance value! Must be at least 1");
		}
		
		// CHECK: Second argument is "debug" and not a legacy or invalid value
		if(args.length == 2) {
			switch(args[1].toLowerCase()) {
				case "debug":
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
	 * @param teleportDistance
	 */
	private static void main(int teleportDistance) {
		debug("Teleporting "+teleportDistance+" blocks...");
		debug(ChatColor.BOLD+"Configuration:");
		Configurator.fetchAll().forEach((k, v) -> {
			debug("- "+k+": "+AboutCommand.boolToWord(v));
		});
		
		// TODO: Option.LOG_TELEPORTS to log player who ran this, start and end positions (int coordinates r good)
		//Bukkit.getLogger();
		//SafeBlocks.contains(Block.getType());
		
		DirectionVector facing = new DirectionVector(player.getEyeLocation());
		debug(ChatColor.BOLD+"Facing vector: Server-side values");
		debug("    f="+facing.direction+", yaw="+facing.yaw+", pitch="+facing.pitch);
		
		// Define our positions to check stuff about:
		Location start = player.getLocation(); // Player location
		defineDestination(facing, start, teleportDistance);
		
		
		debug("Teleporting from: "+toBlockString(start));
		debug("Teleporting to: "+toBlockString(destination)
			+" ("+teleportDistance+" "+(teleportDistance==1 ? "block)" : "blocks)"));
		debug("    Eye height: "+player.getEyeHeight());
		
		debug(ChatColor.BOLD+"Safety tests:");
		
		// CHECK: If the destination is half in the ground, redefine everything
		// to be 1 block up. If this breaks things further down, oh well, we tried.
		if(!TeleportTests.testAndLogBoolean("feetInAir", !destination.getBlock().getType().isSolid())) {
			debug("    Destination is solid, attempting to move 1 block up");
			start = start.add(0, 1, 0);
			defineDestination(facing, start, teleportDistance); // Redefine destination
		}
		
		TeleportTests tests = new TeleportTests(player, destination, destinationEye, destinationGround);
		if(!tests.testAll()) return;
		
		player.teleport(destination);
	}
	
	static void debug(String message) {
		if(!debugMode) return;
		
		player.sendMessage(""+ChatColor.GRAY+ChatColor.ITALIC+message); // Concat empty string otherwise the `ChatColor`s clash
	}
	
	private static String toBlockString(Location l) {
		return l.getBlockX()+", "+l.getBlockY()+", "+l.getBlockZ();
	}
	
	private static void defineDestination(DirectionVector facing, Location start, int distance) {
		destination = start.clone()                                // Target location (=start+distance)
			.add(facing.createPositionVector(distance));
		destinationEye = destination.clone()                       // Target location+eye height
			.add(0, player.getEyeHeight(), 0);
		destinationGround = destination.clone().subtract(0, 1, 0); // Target location-1 y (standing on block)
	}
}
