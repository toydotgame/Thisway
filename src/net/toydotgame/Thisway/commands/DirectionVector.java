package net.toydotgame.Thisway.commands;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

/**
 * Wrapper class to encapsulate logic to convert player eye rotation values into
 * cardinal directions, then to use that cardinal direction as a mask to create
 * a movement vector (of equal lengths {@code n} on all three axes) as the
 * vector of which to modify the player's direction in {@link TeleportCommand}.
 * <dl><dt><b>Created on:</b></dt><dd>2025-07-18</dd></dl>
 * @author toydotgame
 * @see TeleportCommand#main(int)
 * @see #DirectionVector(Location)
 */
class DirectionVector {
	// Instance fields:
	private final float yaw;
	private final float pitch;
	private final BlockFace direction;
	
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
	
	/**
	 * Create a {@link DirectionVector} instance given a {@link Location}
	 * (generally the player's eye location). The yaw and pitch fields of this
	 * location are extracted into this object (kept for debug purposes), and
	 * is used to determine which cardinal direction the player is facing in.<br>
	 * <br>
	 * Priority is also given to the {@linkplain BlockFace#UP up} and
	 * {@linkplain BlockFace#DOWN down} directions, which may be assigned
	 * before and instead of a cardinal octant.
	 * @param l {@link Location} to use
	 */
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
		
		TeleportCommand.debugHeading("direction-vector.heading");
		TeleportCommand.debugLine("direction-vector.values", direction, yaw, pitch);
	}
	
	/**
	 * Creates a new {@link Vector} with an axis value of one of {{@code -d},
	 * {@code 0}, {@code d}}, depending on the direction mask created when this
	 * object was instantiated.<br>
	 * <br>
	 * The Vector returned is useful due to the {@link Location#add(Vector)}
	 * method, which can be used to take the player's location, adding this
	 * Vector, to yield a teleportation along the direction mask's axis.
	 * @param d Magnitude
	 * @return New Vector object with desired offset of {@code d} blocks along
	 * this instance's direction axis
	 * @see DirectionVector
	 * @see Location#add(Vector)
	 */
	Vector createPositionVector(int d) {
		return new Vector(
			direction.getModX()*d, direction.getModY()*d, direction.getModZ()*d
		);
	}
}
