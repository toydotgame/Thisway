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
				if(args[1].equalsIgnoreCase("true")) {
					DataStorage.debug = true;
					
					sender.sendMessage(ChatColor.YELLOW + "=== THISWAY DEBUG START ===");
					sender.sendMessage("Plugin Version: " + DataStorage.localVersion);
					thisway(sender, args);
					sender.sendMessage(ChatColor.YELLOW + "=== THISWAY DEBUG END ===");
					sender.sendMessage("Teleport successful.");
					
					return true;
				} else if(args[1].equalsIgnoreCase("false")) {
					DataStorage.debug = false;
					thisway(sender, args);
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
		float pitch = player.getEyeLocation().getPitch();
		
		if(yaw < 0) {
			yaw += 360; // Adjust yaw to be a positive number. 
		}
		// The `ModDistance` coordinates are relative coordinates to adjust the actual position of the player by.
		if(yaw >= 315 || yaw < 45) {
			DataStorage.facing = "SOUTH";
			DataStorage.xModDistance = 0;
			DataStorage.zModDistance = Integer.parseInt(args[0]);
		} else if(yaw < 135) {
			DataStorage.facing = "WEST";
			DataStorage.xModDistance = Integer.parseInt("-" + args[0]);
			DataStorage.zModDistance = 0;
		} else if(yaw < 225) {
			DataStorage.facing = "NORTH";
			DataStorage.xModDistance = 0;
			DataStorage.zModDistance = Integer.parseInt("-" + args[0]);
		} else if(yaw < 315) {
			DataStorage.facing = "EAST";
			DataStorage.xModDistance = Integer.parseInt(args[0]);
			DataStorage.zModDistance = 0;
		}
		
		Location location = player.getLocation();
		double playerX = location.getX();
		int playerY = location.getBlockY();
		double playerZ = location.getZ();
		
		double playerModifiedX = playerX + DataStorage.xModDistance;
		double playerModifiedZ = playerZ + DataStorage.zModDistance;
		
		String worldName = player.getLocation().getWorld().getName();
		Location newHeadLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerY + 1, playerModifiedZ, yaw, pitch);
		Location newStandingLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerY - 1, playerModifiedZ, yaw, pitch);
		
		boolean newHeadLocationIsSafe = Arrays.asList(DataStorage.safeBlocks).contains(String.valueOf(newHeadLocation.getBlock().getType()));
		if(newHeadLocationIsSafe == false) {
			sender.sendMessage(ChatColor.RED + "New location is inside a block!");
			return;
		}
		
		if(newStandingLocation.getBlock().getType() == Material.AIR) {
			player.getWorld().getBlockAt(newStandingLocation).setTypeId(20);
		}
		
		Location newLocation = new Location(Bukkit.getWorld(worldName), playerModifiedX, playerY, playerModifiedZ, yaw, pitch);
		player.teleport(newLocation);
		
		if(DataStorage.debug == false) {
			sender.sendMessage("Teleport successful.");
		} else {
			sender.sendMessage("Debug Mode: " + DataStorage.debug);
			sender.sendMessage("Player Yaw: " + String.valueOf(rounder(yaw)));
			sender.sendMessage("Player Pitch: " + String.valueOf(rounder(pitch)));
			sender.sendMessage("Player Facing: " + DataStorage.facing);
			sender.sendMessage("Player Position: " + rounder(playerX) + ", " + playerY + ", " + rounder(playerZ));
			sender.sendMessage("New Player Position: " + rounder(playerModifiedX) + ", " + playerY + ", " + rounder(playerModifiedZ));
			sender.sendMessage("Current World: " + worldName);
			sender.sendMessage("New Player Head Location Block Type: " + newHeadLocation.getBlock().getType());
			sender.sendMessage("Is new head location safe? (true/false): " + newHeadLocationIsSafe);
		}
		System.out.print("[Thisway] " + player.getName() + " teleported " + args[0] + " blocks, from " + (int) playerX + ", " + playerY + ", " + (int) playerZ + " to " + (int) playerModifiedX + ", " + playerY + ", " + (int) playerModifiedZ + ".");
	}
	
	public static double rounder(double value) {
		double scale = Math.pow(10, 5);
		return Math.round(value * scale) / scale;
	}
}
