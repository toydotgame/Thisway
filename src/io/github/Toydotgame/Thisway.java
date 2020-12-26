package io.github.Toydotgame;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Thisway implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 1) { // Checking for exactly ONE argument.
			if(args[0].matches("^[0-9]*$") && args[0].length() >= 1 && args[0] != "0" && Integer.parseInt(args[0]) < 32768) { // Making sure said argument is a number, and is not 0. (Plus a hard limit of 32,767 blocks to be TPed)
				if (sender instanceof Player) { // Checking that the sender is a player.
					Player player = (Player) sender;
					
					/* 
					 * PLAYER FACING DATECTOR
					 * */
					float yaw = player.getEyeLocation().getYaw();
					sender.sendMessage("Yaw is: " + String.valueOf(yaw));
					
					if(yaw < 0) {
				        yaw += 360;
				    }
				    if(yaw >= 315 || yaw < 45) {
				    	sender.sendMessage("SOUTH");
				    	DataStorage.facing = "SOUTH";
				    } else if(yaw < 135) {
				    	sender.sendMessage("WEST");
				    	DataStorage.facing = "WEST";
				    } else if(yaw < 225) {
				    	sender.sendMessage("NORTH");
				    	DataStorage.facing = "NORTH";
				    } else if(yaw < 315) {
				    	sender.sendMessage("EAST");
				    	DataStorage.facing = "EAST";
				    }
				    /* 
				     * END PLAYER FACING DETECTOR
				     * */
				    
				    
					
				    /* 
				     * PLAYER POSITION IN WORLD DETECTOR
				     * */
				     Location location = player.getLocation();
				     int playerX = location.getBlockX();
				     int playerY = location.getBlockY();
				     int playerZ = location.getBlockZ();
				     
				     sender.sendMessage("Position: " + playerX + ", " + playerY + ", " + playerZ + " (X, Y, Z)"); // WORKED FIRST TIME, BABY!
				     // Actually, now that I think of it; if it _didn't_ work first time, that would be more impressive.
				    /* 
				     * END PLAYER POSITION DETECTOR
				     * */
				     
				     
				     
				    /*
				     * PLAYER TELEPORTER
				     * I expect this to go wrong, in so many ways.
				     * */
				     // Player TP Part 1
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
				     
				     @SuppressWarnings("unused")
				     int playerModifiedX = playerX + DataStorage.xModDistance;
				     @SuppressWarnings("unused")
				     int playerModifiedZ = playerZ + DataStorage.zModDistance;
				     
				     sender.sendMessage("Modified TP distance: " + playerModifiedX + ", " + playerY + ", " + playerModifiedZ + " (X, Y, Z)");
				    /* 
				     * END PLAYER TELEPORTER
				     * */
				    
				    
				    
					return true;
				} else { // If the sender is not a player:
					System.out.print("Only players can send this command!");
					return false;
				}
			} else { // If args.length _is_ 1, but it's either 0 or it's not a number.
				if(args[0] == "0") {
					System.out.print("Must be a number more than 0!");
				} else if(Integer.parseInt(args[0]) >= 32768) {
					System.out.print("Maximum 32,767 blocks exceeded!");
				} else {
					System.out.print("Numbers only!");
				}
				return false;
			}
		} else { // If args.length != 1.
			System.out.print("There must be exactly ONE argument!");
			return false;
		}
	}
}
