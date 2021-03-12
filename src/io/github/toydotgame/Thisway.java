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
	public void thisway(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		float yaw = player.getEyeLocation().getYaw();
		if(DataStorage.debug == true) {
			sender.sendMessage("Player Yaw: " + String.valueOf(yaw));
		}
		
		float pitch = player.getEyeLocation().getPitch();
		if(DataStorage.debug == true) {
			sender.sendMessage("Player Pitch: " + String.valueOf(pitch));
		}
		
		if(yaw < 0) {
			yaw += 360;
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
		
		Location location = player.getLocation();
		double playerX = location.getX();
		int playerY = location.getBlockY();
		double playerZ = location.getZ();

		double debugX = rounder(playerX);
		double debugZ = rounder(playerZ);
		
		if(DataStorage.debug == true) {
			sender.sendMessage("Player Position: " + debugX + ", " + playerY + ", " + debugZ); // Simple debug for what coordinates it's looking at.
		}

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
		
		String worldName = player.getLocation().getWorld().getName(); // Bloody complicated to just get the world name as a string, innit?
		if(DataStorage.debug == true) {
			sender.sendMessage("Current World: " + worldName);
		}		
		
		int playerNewHeadY = playerY + 1;
		Location newHeadLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerNewHeadY, playerModifiedZ, yaw, pitch); // These coordinates are where the head _will_ be. I need to check to make sure they don't suffocate!
		if(DataStorage.debug == true) {
			sender.sendMessage("New Player Head Location Block Type: " + newHeadLocation.getBlock().getType());
		}
		
		int playerStandingOnBlockY = playerY - 1;
		Location newStandingLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerStandingOnBlockY, playerModifiedZ, yaw, pitch);
		
		boolean newHeadLocationIsSafe = Arrays.asList(DataStorage.safeBlocks).contains(String.valueOf(newHeadLocation.getBlock().getType()));
		if(DataStorage.debug == true) {
			sender.sendMessage("Is new head location safe? (true/false): " + newHeadLocationIsSafe);
		}
		if(newHeadLocationIsSafe == true) {
			if(newStandingLocation.getBlock().getType() == Material.AIR) {
				player.getWorld().getBlockAt(newStandingLocation).setTypeId(20);
			}
			
			Location newLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerY, playerModifiedZ, yaw, pitch); // That's why I got the yaw and pitch; so that when you TP, you're looking in the same angle; instead of just resetting it.
			player.teleport(newLocation);
			
			if(DataStorage.debug != true) {
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
