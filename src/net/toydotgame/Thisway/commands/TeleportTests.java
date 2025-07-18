package net.toydotgame.Thisway.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.toydotgame.Thisway.Configurator;
import net.toydotgame.Thisway.Option;

final class TeleportTests {
	// Instance fields:
	private Location destination, eye, ground;
	private Player player;
		
	TeleportTests(Player player, Location destination, Location destinationEye, Location destinationGround) {
		this.destination = destination;
		eye = destinationEye;
		ground = destinationGround;
		this.player = player;
	}
	
	boolean testAll() {
		// Some of the below safety check booleans are redundantly inverted
		// twice, but that's intended since a "Yes" for the debug print of that
		// check should indicate the "desired" value
		
		// CHECK: Destination is safe
		if(!test(
			!destination.getBlock().getType().isSolid() && !destination.getBlock().isLiquid(),
			"footIsSafeBlock",
			"The destination is unsafe! (Probably a solid block)"
		)) return false;		
		if(!test(
			!eye.getBlock().getType().isSolid(),
			"eyeIsNonSolid",
			"The destination is unsafe! (Probably a solid block)"
		)) return false;
		
		if(player.isFlying()) {
			TeleportCommand.debug("Player is flying, ignoring support checks");
			return true;
		}
		
		// Set glass support block if desired:
		if(Configurator.fetch(Option.SUPPORT_BLOCKS)
			&& !testAndLogBoolean("destGroundIsSolid", !ground.getBlock().isEmpty())) {
			TeleportCommand.debug("    Destination isn't solid. Placing a support block");
			ground.getBlock().setType(Material.GLASS);
		}
		// CHECK: If no block was placed, the destination ground must be safe
		else if(!testAndLogBoolean("destHasSupport", ground.getBlock().getType().isSolid())) {
			return error("The destination has no support blocks! "
				+"If you have place-support-blocks enabled, a support block probably failed to place");
		}
		
		return true;
	}
	
	private boolean test(boolean condition, String name, String errorMessage) {
		testAndLogBoolean(name, condition);
		
		if(!condition) return error(errorMessage);		
		return true;
	}
	
	private boolean error(String message) {
		player.sendMessage(ChatColor.RED+message);
		return false;
	}
	
	static boolean testAndLogBoolean(String name, boolean condition) {
		TeleportCommand.debug(name+": "+AboutCommand.boolToWord(condition));
		return condition;
	}
}
