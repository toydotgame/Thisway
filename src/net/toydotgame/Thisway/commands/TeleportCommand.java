package net.toydotgame.Thisway.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.toydotgame.Thisway.Configurator;
import net.toydotgame.Thisway.Lang;
import net.toydotgame.Thisway.Option;
import net.toydotgame.Thisway.Thisway;
import net.toydotgame.Thisway.commands.utils.CommandUtils;

/**
 * Static class to handle the actual teleportation function of Thisway.
 * <dl><dt><b>Created on:</b></dt><dd>2020-12-26<br>
 * Originally as {@code io.github.toydotgame.Thisway.Thisway}</dd>
 * <dt><b>Re-created on:</b></dt><dd>2025-07-15</dd></dl>
 * @author toydotgame
 */
public class TeleportCommand {
	//private static Player player;
	//private static boolean debugMode;
	//private static Location destination, destinationEye, destinationGround;
	
	private final Thisway plugin;
	private final Player player;
	private int status = CommandUtils.VALID;
	private int distance = 0;
	private Debugger debug = null; 
	
	public TeleportCommand(Thisway plugin, CommandSender sender, String[] args) {
		this.plugin = plugin;
		this.player = (Player)sender;
		
		if(!CommandUtils.testForPermission(player, "teleport")) {
			this.status = CommandUtils.PERMISSION_ERROR;
			return;
		}
		
		if(args.length < 1 || args.length > 2) {
			CommandUtils.syntaxError(sender, "syntax.args-length");
			this.status = CommandUtils.SYNTAX_ERROR;
			return;
		}
		
		try {
			distance = Integer.parseInt(args[0]);
			if(distance < 1) throw new NumberFormatException();
		} catch(NumberFormatException e) {
			this.status = CommandUtils.SYNTAX_ERROR;
			return;
		}
		
		boolean debugMode = false;
		if(args.length == 2) {
			switch(args[1].toLowerCase()) {
				case "debug":
					// CHECK: Player has permission to use debug mode
					if(!CommandUtils.testForPermission(sender, "debug")) {
						this.status = CommandUtils.PERMISSION_ERROR;
						return;
					}
					
					debugMode = true;
					sender.sendMessage(Lang.create("debug.enabled"));
					break;
				case "true":
				case "false":
					sender.sendMessage(ChatColor.YELLOW+Lang.create("tp.syntax.legacy-debug"));
				default:
					CommandUtils.syntaxError(sender, "tp.syntax.invalid-debug");
					this.status = CommandUtils.SYNTAX_ERROR;
			}
		}
		
		debug = new Debugger(this, player, debugMode);
	}
	
	public boolean run() {
		// First exit if this command instance is invalid:
		Boolean b = CommandUtils.getReturnValueIfError(status);
		if(b != null) return b;
		
		/*
		 * SET UP TELEPORT
		 */
		
		debug.hRule("rule.begin");
		debug.println("begin."+(distance==1 ? "singular":"plural"), distance);
		
		DirectionVector facing = new DirectionVector(this, player.getEyeLocation());
		
		// Define our positions to check stuff about:
		Location start = player.getLocation(); // Player location
		Destination destination = new Destination(start, player.getEyeHeight(), facing, distance);
		
		String teleportDistanceString = Lang.create("debug.distance."+(distance==1 ? "singular":"plural"), distance);
		debug.println("from", locationToArray(start));
		debug.println(Lang.create("debug.to", locationToArray(destination.get()))+teleportDistanceString);
		debug.println("eye-height", player.getEyeHeight());
		
		/*
		 * RUN SAFETY CHECKS
		 */
		
		debug.heading("checks.heading");
		TeleportTests tests = new TeleportTests(this, player, destination);
		
		// CHECK: If the destination is half in the ground, redefine everything
		// to be 1 block up. If this breaks things further down, oh well, we tried.
		boolean feetInNonSolid = tests.testAndLogBoolean("feetInNonSolid",
			!destination.get().getBlock().getType().isSolid());
		if(!feetInNonSolid) {
			debug.println("checks.destination-moved");
			destination = new Destination(destination.get().clone().add(0, 1, 0), player.getEyeHeight(), facing, distance);
		}
		
		if(!tests.testAll()) return true; // Not a syntax error
		debug.println();
		
		/*
		 * TELEPORT & LOG
		 */
		
		String playerName = player.getName();
		String world = player.getWorld().getName();
		debug.println("player", playerName);
		debug.println("world", world);
		
		player.teleport(destination.get());
		
		if(Configurator.fetchToggle(Option.LOG_TELEPORTS))
			plugin.getLogger().info(Lang.create("tp.log", playerName,
				locationToArray(start.subtract(0, (feetInNonSolid ? 0:1), 0)),
				locationToArray(destination.get()))+teleportDistanceString);
		
		debug.println();
		debug.println(ChatColor.RESET+Lang.create("debug.complete"));
		debug.hRule("rule.end");
		
		return true;
	}
	
	private static Object[] locationToArray(Location l) {
		return new Integer[] {l.getBlockX(), l.getBlockY(), l.getBlockZ()};
	}
	
	Debugger getDebugger() {
		return debug;
	}
}
