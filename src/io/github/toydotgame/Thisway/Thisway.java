package io.github.toydotgame.Thisway;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/* 
 * CREATED: 2020-12-26
 * AUTHOR: toydotgame
 * This is the main class run by the `/thisway` command. 
 */

public class Thisway implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			System.out.print("[Thisway] Only players can use Thisway!");
			return true;
		}
		
		Player playerSender = (Player) sender;
		if(playerSender.hasPermission("thisway.use") != true) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use Thisway!");
			return true;
		}
		
		if(args.length == 0 || args.length > 2) {
			sender.sendMessage(ChatColor.RED + "Invalid argument amount!");
			return false;
		}
		
		if(!(args[0].matches("^[0-9]*$")) || Integer.parseInt(args[0]) == 0) {
			sender.sendMessage(ChatColor.RED + "Invalid teleportation distance!");
			return false;
		}
		
		switch(args.length) {
			case 1:
				DataStorage.debug = false;
				thisway(sender, args);
				return true;
			case 2:
				if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")) {
					DataStorage.debug = true;
					
					sender.sendMessage(ChatColor.YELLOW + "=== THISWAY DEBUG START ===");
					sender.sendMessage("Plugin Version: " + DataStorage.localVersion);
					thisway(sender, args);
					sender.sendMessage(ChatColor.YELLOW + "=== THISWAY DEBUG END ===");
					sender.sendMessage("Teleport successful.");
					
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "Invalid debug argument!");
					return false;
				}
			default:
				sender.sendMessage(ChatColor.RED + "Invalid number of arguments!");
				return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void thisway(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		float yaw = player.getEyeLocation().getYaw();
		if(DataStorage.debug == true) {
			sender.sendMessage("Player Yaw: " + String.valueOf(rounder(yaw)));
		}
		float pitch = player.getEyeLocation().getPitch();
		if(DataStorage.debug == true) {
			sender.sendMessage("Player Pitch: " + String.valueOf(rounder(pitch)));
		}
		
		if(yaw < 0) {
			yaw += 360;
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
		double playerX = location.getX();
		int playerY = location.getBlockY();
		double playerZ = location.getZ();
		double debugX = rounder(playerX);
		double debugZ = rounder(playerZ);
		
		if(DataStorage.debug == true) {
			sender.sendMessage("Player Position: " + debugX + ", " + playerY + ", " + debugZ);
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
		
		if(newHeadLocationIsSafe == true) {
			if(newStandingLocation.getBlock().getType() == Material.AIR) {
				player.getWorld().getBlockAt(newStandingLocation).setTypeId(20);
			}
			
			Location newLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerY, playerModifiedZ, yaw, pitch);
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
