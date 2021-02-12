package io.github.Toydotgame;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Thisway implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {		
		if(sender instanceof Player) { // Checks that the command was sent by a player.
			Player playerSender = (Player) sender;
			if(playerSender.hasPermission("thisway.use") == true) {
				if(args.length == 1) { // If there's only _one_ argument; check that: args[0] is all digits, nothing else; args[0] is _not_ 0.
					if(args[0].matches("^[0-9]*$") && args[0] != "0") {
						DataStorage.debug = false;
						thisway(sender, args);
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "Invalid argument!");
						return false;
					}
				} else if(args.length == 2) { // If there's _two_ arguments:
					if(playerSender.hasPermission("thisway.debug") == true) {
						if(args[1].equalsIgnoreCase("true")) { // If the second argument is "true':
							if(args[0].matches("^[0-9]*$") && args[0] != "0") { // If the first argument is an accepted number.
								DataStorage.debug = true;
								
								sender.sendMessage(ChatColor.YELLOW + "=== THISWAY DEBUG START ==="); // Debug header in player chat.
								sender.sendMessage("Plugin version: " + DataStorage.localVersion);
								sender.sendMessage(/* ChatColor.GREY + */ "Always remember to check for updates!");
								thisway(sender, args);
								sender.sendMessage(ChatColor.YELLOW + "=== THISWAY DEBUG END ==="); // Debug footer.
								
								return true;
							} else {
								sender.sendMessage(ChatColor.RED + "Invalid teleportation distance! Not a number!");
								return false;
							} // Ends `if(args[0].matches("^[0-9]*$") && args[0] != "0")`. Ends arg 1 number check.
						} else if(args[1].equalsIgnoreCase("false")) { // If arg 2 is "false":
							if(args[0].matches("^[0-9]*$") && args[0] != "0") { // Number validity check like before.
								DataStorage.debug = false;
								thisway(sender, args);
								return true;
							} else {
								sender.sendMessage(ChatColor.RED + "Invalid teleportation distance! Not a number!");
								return false;
							} // Ends `if(args[0].matches("^[0-9]*$") && args[0] != "0")`. (Number validity check)
						} else {
							sender.sendMessage(ChatColor.RED + "Invalid second argument!");
							return false;
						} // Ends `if(args[1] == "true")` or `else if(args[1] == "false")`. (Debug toggle argument)
					} else { // If they don't have the thisway.debug permission:
						sender.sendMessage(ChatColor.RED + "You don't have the right permissions to use Debug Mode!");
						return true; // Permission errors alway return true.
					}
				} else { // If arguments are invalid, run:
					sender.sendMessage(ChatColor.RED + "Invalid argument amount!");
					return false;
				} // Ends `if(args.length == 2 && args[1] == "true") {}`. Ends _debug = true_ check if there's two arguments. Also ends the other checks about arguments (First level of checks, that is!).
			} else {// If the sender does not have thisway.use:
				sender.sendMessage(ChatColor.RED + "You don't have the right permissions to use this command!");
				return true; // Perms return true, always; whether there's an error or not.
			} // Ends permission check for thisway.use.
		} else { // Else statement for: `if(sender instanceof Player) {}`
			// If sender is not player, run:
			System.out.print("[Thisway] Only players can use this command!");
			return false;
		} // Ends `if(sender instanceof Player) {}`. Ends _player is sender_ check.
	} // Ends `public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {}`. Ends main method (onCommand()).
	
	@SuppressWarnings("deprecation")
	public void thisway(CommandSender sender, String[] args) { // Most likely needed to keep onCommand() method simple and prevent two copies of the code from being needed in one class.
		Player player = (Player) sender;
		
		// This detects which way the player is facing:
		float yaw = player.getEyeLocation().getYaw();
		if(DataStorage.debug == true) { // You'll see these a bunch, they determine if they should send debug stats judged by if DataStorage.debug is true.
			/* Fun story:
			 * I had this if() statement as this:
			 *     if(DataStorage.debug = true) {}
			 * See the problem? It's the "=".
			 * My IDE didn't have a problem with it, but running the command made the debug message show no matter what.
			 * Anyway, I've fixed it now. :) */
			sender.sendMessage("Player Yaw: " + String.valueOf(yaw));
		}
		
		float pitch = player.getEyeLocation().getPitch(); // Used later during teleport script. It's only up here so that debug output has yaw and pitch next to each other.
		if(DataStorage.debug == true) {
			sender.sendMessage("Player Pitch: " + String.valueOf(pitch));
		}
		
		if(yaw < 0) {
			yaw += 360;
			// I don't know _exactly_ what this does, it's some mathematical function from the Bukkit forums though - and it works. ;)
		}
		if(yaw >= 315 || yaw < 45) {
			if(DataStorage.debug == true) {
				sender.sendMessage("Player Facing: SOUTH");
			}
			DataStorage.facing = "SOUTH"; // I really love the DataStorage class, it's so neat!
		} else if(yaw < 135) {
			if(DataStorage.debug == true) {
				sender.sendMessage("Player Facing: WEST");
			}
			DataStorage.facing = "WEST";
		} else if(yaw < 225) {
			if(DataStorage.debug == true) {
				sender.sendMessage("Player Facing: NORTH");
			}
			DataStorage.facing = "NORTH";
		} else if(yaw < 315) {
			if(DataStorage.debug == true) {
				sender.sendMessage("Player Facing: EAST");
			}
			DataStorage.facing = "EAST";
		}
		
		
		
		// _This_ detects the player's coordinates!:
		Location location = player.getLocation();
		int playerX = location.getBlockX();
		int playerY = location.getBlockY();
		int playerZ = location.getBlockZ();
		if(DataStorage.debug == true) {
			sender.sendMessage("Player Position: " + playerX + ", " + playerY + ", " + playerZ); // Simple debug for what coordinates it's looking at.
		}
		
		
		
		// This gets the coordinates it needs to TP to:
		if(DataStorage.facing == "NORTH") {
			// North in Minecraft is on the -Z axis. I'll modify the coordinates as such!
			DataStorage.xModDistance = 0;
			DataStorage.zModDistance = Integer.parseInt("-" + args[0]);
		} else if(DataStorage.facing == "SOUTH") {
			// South is +Z in-game.
			DataStorage.xModDistance = 0;
			DataStorage.zModDistance = Integer.parseInt(args[0]);
		} else if(DataStorage.facing == "EAST") {
			// East is +X.
			DataStorage.xModDistance = Integer.parseInt(args[0]);
			DataStorage.zModDistance = 0;
		} else if(DataStorage.facing == "WEST") {
			// West is -X.
			DataStorage.xModDistance = Integer.parseInt("-" + args[0]);
			DataStorage.zModDistance = 0;
		}
		
		double playerModifiedX = playerX + DataStorage.xModDistance + 0.5;
		/* I hopefully shouldn't _need_ to modify the Y; both due to the plugin being for horizontal teleportation, not vertical - and I'm hoping Bukkit/Spigot uses a different teleportation system than Minecraft 1.6:
		 * In vanilla Minecraft [1.6], when you TP - say - 100 blocks ahead: `/tp @p ~100 ~ ~`
		 * ...and you spam that command in, after a few goes, you'll start going down into the ground.
		 * If Bukkit _does_ use the same TP system as that, and _also_ has that bug; I'll need to make a modified Y which TPs you up 0.2 (Or whatever height difference it actually is) blocks each time you use the command,
		 * thus compensating for the height you descend whilst TPing. */
		double playerModifiedZ = playerZ + DataStorage.zModDistance + 0.5;
		if(DataStorage.debug == true) {
			sender.sendMessage("New Player Position (To TP to): " + playerModifiedX + ", " + playerY + ", " + playerModifiedZ);
		}
		
		
		
		// This doesn't belong anywhere, but it needs to be above the new head blocktype tester.
		// All this does is get the world name and player's pitch.
		String worldName = player.getLocation().getWorld().getName(); // Bloody complicated to just get the world name as a string, innit?
		if(DataStorage.debug == true) {
			sender.sendMessage("Current World: " + worldName);
		}
		/* I get the world name in case the current world isn't called "world",
		 * or is the main world has a different name,
		 * _or_ if the command is being used in a different dimension _or_ world;
		 * due to plugins like Multiverse messing this up. */
		
		
		
		// This bit _should_ check that the player's new location is air:
		int playerNewHeadY = playerY + 1;
		Location newHeadLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerNewHeadY, playerModifiedZ, yaw, pitch); // These coordinates are where the head _will_ be. I need to check to make sure they don't suffocate!
		if(DataStorage.debug == true) {
			sender.sendMessage("New Player Head Location Block Type: " + newHeadLocation.getBlock().getType());
		}
		
		// This checks what block the player will be standing on:
		int playerStandingOnBlockY = playerY - 1;
		Location newStandingLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerStandingOnBlockY, playerModifiedZ, yaw, pitch);
		
		boolean newHeadLocationIsSafe = Arrays.asList(DataStorage.safeBlocks).contains(String.valueOf(newHeadLocation.getBlock().getType()));
		if(DataStorage.debug == true) {
			sender.sendMessage("Is new head location safe? (true/false): " + newHeadLocationIsSafe);
		}
		if(newHeadLocationIsSafe == true) {
			if(newStandingLocation.getBlock().getType() == Material.AIR) {
				player.getWorld().getBlockAt(newStandingLocation).setTypeId(20);
				
				// This bit actaully TPs the player!		
				Location newLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerY, playerModifiedZ, yaw, pitch); // That's why I got the yaw and pitch; so that when you TP, you're looking in the same angle; instead of just resetting it.
				player.teleport(newLocation);
				
				sender.sendMessage("Teleport successful.");
				System.out.print("[Thisway] " + player.getName() + " teleported " + args[0] + " blocks, from " + playerX + ", " + playerY + ", " + playerZ + " to " + playerModifiedX + ", " + playerY + ", " + playerModifiedZ + ".");
			} else { // If the new location is safe to stand on:
				// This bit actaully TPs the player!		
				Location newLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerY, playerModifiedZ, yaw, pitch); // That's why I got the yaw and pitch; so that when you TP, you're looking in the same angle; instead of just resetting it.
				player.teleport(newLocation);
				
				sender.sendMessage("Teleport successful.");
				System.out.print("[Thisway] " + player.getName() + " teleported " + args[0] + " blocks, from " + playerX + ", " + playerY + ", " + playerZ + " to " + playerModifiedX + ", " + playerY + ", " + playerModifiedZ + ".");
			} // Ends new location air check for the block being stood on.
		} else { // If the new head location _isn't_ safe:
			sender.sendMessage(ChatColor.RED + "New location is inside a block! Try again.");
		} // Ends suffocation check.
	} // Ends `public void thisway() {}`. Ends thisway() method.
} // Ends `public class Thisway implements CommandExecutor {}`. Ends class.
