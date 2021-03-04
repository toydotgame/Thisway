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
							} // Ends arg 1 number check.
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
						} // Ends arg 2 true/false checker.
					} else { // If player lacks thisway.debug
						sender.sendMessage(ChatColor.RED + "You don't have the right permissions to use Debug Mode!");
						return true; // It should return true because the command usage was correct, but the permissions weren't.
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Invalid argument amount!");
					return false;
				} // Argument amount check closing brace.
			} else { // if player lacks thisway.use
				sender.sendMessage(ChatColor.RED + "You don't have the right permissions to use this command!");
				return true;
			}
		} else {
			// If sender is not player:
			System.out.print("[Thisway] Only players can use this command!");
			return true; // Returning true will stop command usage from being printed to console.
			// We don't want to mess with the console.
		} // Ends player = sender check.
	} // Ends onCommand() method.
	
	@SuppressWarnings("deprecation")
	public void thisway(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		// This detects which way the player is facing:
		float yaw = player.getEyeLocation().getYaw();
		if(DataStorage.debug == true) {
			// This will print debug info if debug mode is activated. No need for an `else` in order to be quiet if there's no need for debug.
			sender.sendMessage("Player Yaw: " + String.valueOf(yaw));
		}
		
		float pitch = player.getEyeLocation().getPitch();
		// Used later during teleport script.
		// It's only up here so that debug output has yaw and pitch next to each other.
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
			DataStorage.facing = "SOUTH";
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
		double playerModifiedZ = playerZ + DataStorage.zModDistance + 0.5;
		if(DataStorage.debug == true) {
			sender.sendMessage("New Player Position (To TP to): " + playerModifiedX + ", " + playerY + ", " + playerModifiedZ);
		}
		
		// This needs to be above the suffocation detector.
		// All this does is get the world name and player's pitch.
		String worldName = player.getLocation().getWorld().getName(); // Bloody complicated to just get the world name as a string, innit?
		if(DataStorage.debug == true) {
			sender.sendMessage("Current World: " + worldName);
		}		
		
		
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
				
				// This bit actually TPs the player!		
				Location newLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerY, playerModifiedZ, yaw, pitch); // That's why I got the yaw and pitch; so that when you TP, you're looking in the same angle; instead of just resetting it.
				player.teleport(newLocation);
				
				if(DataStorage.debug != true) { // This success message comes after the footer in chat. Look at the `onCommand()` method above.
					sender.sendMessage("Teleport successful.");
				}
				System.out.print("[Thisway] " + player.getName() + " teleported " + args[0] + " blocks, from " + playerX + ", " + playerY + ", " + playerZ + " to " + playerModifiedX + ", " + playerY + ", " + playerModifiedZ + ".");
			} else { // If the new location is safe to stand on:	
				Location newLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerY, playerModifiedZ, yaw, pitch); // That's why I got the yaw and pitch; so that when you TP, you're looking in the same angle; instead of just resetting it.
				player.teleport(newLocation);
				
				if(DataStorage.debug != true) {
					sender.sendMessage("Teleport successful.");
				}
				System.out.print("[Thisway] " + player.getName() + " teleported " + args[0] + " blocks, from " + playerX + ", " + playerY + ", " + playerZ + " to " + playerModifiedX + ", " + playerY + ", " + playerModifiedZ + ".");
			}
		} else { // If the new head location _isn't_ safe:
			sender.sendMessage(ChatColor.RED + "New location is inside a block!");
		}
	} // Closes off thisway() method.
} // Ends class.
