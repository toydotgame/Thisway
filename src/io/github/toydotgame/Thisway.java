package io.github.toydotgame;

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
	/* 
	 * The onCommand() method acts as a command validity check and Debug Mode script.
	 * It'll check for the following:
	 * - The sender is a player.
	 * - The sender has the `thisway.use` permission. It'll also check for `thisway.debug` if Debug Mode is specified in-game.
	 * - The sender uses 1 argument and that 1 argument is a number. It'll check that the second argument is "true" or "false" if there's 2 arguments.
	 * - Argument 1 _is not_ 0.
	 * 
	 * If the command itself is written incorrectly, onCommand() will return false, and Spigot prints usage.
	 * If the command _is_ written right, but something else isn't right, Thisway will return true but not run the next part of the code:
	 * The thisway() method.
	 */
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {		
		if(sender instanceof Player) {
			Player playerSender = (Player) sender;
			if(playerSender.hasPermission("thisway.use") == true) {
				if(args.length == 1) {
					if(args[0].matches("^[0-9]*$") && args[0] != "0") {
						DataStorage.debug = false;
						thisway(sender, args);
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "Invalid argument!");
						return false;
					}
				} else if(args.length == 2) {
					if(playerSender.hasPermission("thisway.debug") == true) {
						if(args[1].equalsIgnoreCase("true")) {
							if(args[0].matches("^[0-9]*$") && args[0] != "0") {
								DataStorage.debug = true;
								
								sender.sendMessage(ChatColor.YELLOW + "=== THISWAY DEBUG START ===");
								sender.sendMessage("Plugin Version: " + DataStorage.localVersion);
								thisway(sender, args);
								sender.sendMessage(ChatColor.YELLOW + "=== THISWAY DEBUG END ===");
								sender.sendMessage("Teleport successful.");
								
								return true;
							} else {
								sender.sendMessage(ChatColor.RED + "Invalid teleportation distance! Not a number!");
								return false;
							}
						} else if(args[1].equalsIgnoreCase("false")) {
							if(args[0].matches("^[0-9]*$") && args[0] != "0") {
								DataStorage.debug = false;
								thisway(sender, args);
								return true;
							} else {
								sender.sendMessage(ChatColor.RED + "Invalid teleportation distance! Not a number!");
								return false;
							}
						} else {
							sender.sendMessage(ChatColor.RED + "Invalid second argument!");
							return false;
						}
					} else {
						sender.sendMessage(ChatColor.RED + "You don't have the right permissions to use Debug Mode!");
						return true;
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Invalid argument amount!");
					return false;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "You don't have the right permissions to use this command!");
				return true;
			}
		} else {
			System.out.print("[Thisway] Only players can use this command!");
			return true;
		}
	}
	
	@SuppressWarnings("deprecation")
	/*
	 * Any variables with "debug" and "humanReadable" in them are used only for readability in Debug Mode and the console respectively.
	 * Thisway will **always** use exact teleportation to the whatever-th decimal place that Spigot provides.
	 */
	public void thisway(CommandSender sender, String[] args) {
		Player player = (Player) sender; // This gives us a "player" variable of type Player. We use it to get location data.
		
		/*
		 * The yaw is used to get the cardinal direction of the player.
		 * Both pitch and yaw are used to preserve the camera's angle from pre-TP to post-TP locations, improving the overall quality of teleporting.
		 */
		float yaw = player.getEyeLocation().getYaw();
		if(DataStorage.debug == true) { // Debug Mode will print data to chat if it's turned on.
			sender.sendMessage("Player Yaw: " + String.valueOf(yaw));
		}
		float pitch = player.getEyeLocation().getPitch();
		if(DataStorage.debug == true) {
			sender.sendMessage("Player Pitch: " + String.valueOf(pitch));
		}
		
		if(yaw < 0) {
			yaw += 360; // This makes the next if() set have workable coords.
		}
		if(yaw >= 315 || yaw < 45) {
			DataStorage.facing = "SOUTH";
		} else if(yaw < 135) {
			DataStorage.facing = "WEST";
		} else if(yaw < 225) {
			DataStorage.facing = "NORTH";
		} else if(yaw < 315) {
			DataStorage.facing = "EAST";
		}
		if(DataStorage.debug == true) {
			sender.sendMessage("Player Facing: " + DataStorage.facing);
		}
		
		Location location = player.getLocation();
		double playerX = location.getX(); // getX() instead of getBlockX() returns a double, which has decimal exacts for coords.
		int playerY = location.getBlockY();
		double playerZ = location.getZ();
		double debugX = rounder(playerX); // rounder() rounds to 5 decimal places, like in F3.
		double debugZ = rounder(playerZ);
		
		if(DataStorage.debug == true) {
			sender.sendMessage("Player Position: " + debugX + ", " + playerY + ", " + debugZ);
		}

		// This will turn the number arguments into an X or Z value to be modified either positively or negatively.
		if(DataStorage.facing == "NORTH") {
			DataStorage.xModDistance = 0;
			DataStorage.zModDistance = Integer.parseInt("-" + args[0]);
		} else if(DataStorage.facing == "SOUTH") {
			DataStorage.xModDistance = 0;
			DataStorage.zModDistance = Integer.parseInt(args[0]);
		} else if(DataStorage.facing == "EAST") {
			DataStorage.xModDistance = Integer.parseInt(args[0]);
			DataStorage.zModDistance = 0;
		} else if(DataStorage.facing == "WEST") {
			DataStorage.xModDistance = Integer.parseInt("-" + args[0]);
			DataStorage.zModDistance = 0;
		}
		
		double playerModifiedX = playerX + DataStorage.xModDistance;
		double playerModifiedZ = playerZ + DataStorage.zModDistance;
		
		double debugNewX = rounder(playerModifiedX);
		double debugNewZ = rounder(playerModifiedZ);
		if(DataStorage.debug == true) {
			sender.sendMessage("New Player Position (To TP to): " + debugNewX + ", " + playerY + ", " + debugNewZ);
		}
		
		/*
		 * The current world is needed to create a Location variable to TP to.
		 * This also allows support for multiple dimensions and indirect Multiverse support.
		 */
		String worldName = player.getLocation().getWorld().getName();
		if(DataStorage.debug == true) {
			sender.sendMessage("Current World: " + worldName);
		}		
		
		int playerNewHeadY = playerY + 1;
		Location newHeadLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerNewHeadY, playerModifiedZ, yaw, pitch);
		if(DataStorage.debug == true) {
			sender.sendMessage("New Player Head Location Block Type: " + newHeadLocation.getBlock().getType());
		}
		
		int playerStandingOnBlockY = playerY - 1;
		Location newStandingLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerStandingOnBlockY, playerModifiedZ, yaw, pitch);
		
		boolean newHeadLocationIsSafe = Arrays.asList(DataStorage.safeBlocks).contains(String.valueOf(newHeadLocation.getBlock().getType()));
		if(DataStorage.debug == true) {
			sender.sendMessage("Is new head location safe? (true/false): " + newHeadLocationIsSafe);
		}
		
		/*
		 * Here's the final teleportation segment. It will check that:
		 * - The new head location won't cause the player to suffocate.
		 * - The new standing location isn't air.
		 *     - If it _is_ air, a glass block will be placed.
		 * 
		 * If the new location is safe, the player is teleported.
		 */
		if(newHeadLocationIsSafe == true) {
			if(newStandingLocation.getBlock().getType() == Material.AIR) {
				player.getWorld().getBlockAt(newStandingLocation).setTypeId(20); // ID 20 is a Glass Block.
			}
			
			Location newLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerY, playerModifiedZ, yaw, pitch);
			player.teleport(newLocation);
			
			if(DataStorage.debug != true) { // If debug is on, the success message gets sent after thisway() back in the onCommand() launcher. If debug mode is off, it'll be printed from this sendMessage() command.
				sender.sendMessage("Teleport successful.");
			}
			
			int humanReadableX = (int) playerX;
			int humanReadableZ = (int) playerZ;
			int humanReadableNewX = (int) playerModifiedX;
			int humanReadableNewZ = (int) playerModifiedZ;
			
			System.out.print("[Thisway] " + player.getName() + " teleported " + args[0] + " blocks, from " + humanReadableX + ", " + playerY + ", " + humanReadableZ + " to " + humanReadableNewX + ", " + playerY + ", " + humanReadableNewZ + ".");
		} else {
			sender.sendMessage(ChatColor.RED + "New location is inside a block!");
		}
	}
	
	public static double rounder(double value) {
		double scale = Math.pow(10, 5);
		return Math.round(value * scale) / scale;
	}
}
