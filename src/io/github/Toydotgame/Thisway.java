package io.github.Toydotgame;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Thisway implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
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
		    } else if(yaw < 135) {
		    	sender.sendMessage("WEST");
		    } else if(yaw < 225) {
		    	sender.sendMessage("NORTH");
		    } else if(yaw < 315) {
		    	sender.sendMessage("EAST");
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
		     
		     sender.sendMessage("Position: " + playerX + ", " + playerY + ", " + playerZ + " (X, Y, Z)");
		    /* 
		     * END PLAYER POSITION DETECTOR
		     * */
		    
		    
		    
			return true;
		} else {
			System.out.println("Command Failed!");
			return false;
		}
	}
}
