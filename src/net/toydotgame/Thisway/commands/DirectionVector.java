package net.toydotgame.Thisway.commands;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

class DirectionVector {
	// Instance fields:
	final float yaw;
	final float pitch;
	final BlockFace direction;
	
	// Constants:
	private static final BlockFace[] directions = { // Minecraft f value goes S→W→N→E when yaw 0→180→-179→-1 (aka clockwise)
		BlockFace.SOUTH,
		BlockFace.SOUTH_WEST,
		BlockFace.WEST,
		BlockFace.NORTH_WEST,
		BlockFace.NORTH,
		BlockFace.NORTH_EAST,
		BlockFace.EAST,
		BlockFace.SOUTH_EAST
	};
	
	DirectionVector(Location l) {
		yaw = l.getYaw();
		pitch = l.getPitch();
		if(pitch < -45) direction = BlockFace.UP;
		else if(pitch > 45) direction = BlockFace.DOWN;
		/* Divide yaw into 45 degree sections, then round the yaw value to the
		 * nearest one. This yields a value from -7→0. Bitwise AND with 111
		 * yields a value from 0→7 (within array bounds), but reversed order.
		 * Code derived from:
		 * 	https://bukkit.org/threads/getting-the-direction-that-the-player-is-facing.400099/#post-3301524
		 */
		else direction = directions[Math.round(yaw/45)&0x7];
	}
	
	Vector createPositionVector(int d) {
		return new Vector(
			direction.getModX()*d, direction.getModY()*d, direction.getModZ()*d
		);
	}
}
