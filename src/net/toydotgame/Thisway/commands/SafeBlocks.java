package net.toydotgame.Thisway.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Class that lists {@link org.bukkit.Material Material}s that are safe to
 * teleport the player into.<br>
 * For determining the condition of whether or not a block should be placed at
 * their feet, see {@link org.bukkit.Material#isSolid() Material.isSolid()}.
 * <dt><b>Created on:</b></dt><dd>2020-12-26<br>
 * Originally as {@code io.github.toydotgame.Thisway.DataStorage}</dd>
 * <dt><b>Re-created on:</b></dt><dd>2025-07-18</dd>
 * @author toydotgame
 */
enum SafeBlocks {
	AIR(Material.AIR),
	BROWN_MUSHROOM(Material.BROWN_MUSHROOM),
	CARPET(Material.CARPET),
	COCOA(Material.COCOA),
	WHEAT(Material.CROPS),
	DEAD_BUSH(Material.DEAD_BUSH),
	DETECTOR_RAIL(Material.DETECTOR_RAIL),
	REPEATER_OFF(Material.DIODE_BLOCK_OFF),
	REPEATER_ON(Material.DIODE_BLOCK_ON),
	FIRE(Material.FIRE),
	FLOWER_POT(Material.FLOWER_POT),
	LADDER(Material.LADDER),
	LEAVES(Material.LEAVES),
	LEVER(Material.LEVER),
	MELON_STEM(Material.MELON_STEM),
	PAINTING(Material.PAINTING),
	NETHER_PORTAL(Material.PORTAL),
	POWERED_RAIL(Material.POWERED_RAIL),
	PUMPKIN_STEM(Material.PUMPKIN_STEM),
	RAILS(Material.RAILS),
	RED_MUSHROOM(Material.RED_MUSHROOM),
	ROSE(Material.RED_ROSE),
	COMPARATOR_OFF(Material.REDSTONE_COMPARATOR_OFF),
	COMPARATOR_ON(Material.REDSTONE_COMPARATOR_ON),
	REDSTONE_TORCH_OFF(Material.REDSTONE_TORCH_OFF),
	REDSTONE_TORCH_ON(Material.REDSTONE_TORCH_ON),
	REDSTONE_WIRE(Material.REDSTONE_WIRE),
	SAPLING(Material.SAPLING),
	FLOOR_SIGN(Material.SIGN_POST),
	SKULL(Material.SKULL),
	SNOW_LAYER(Material.SNOW),
	WATER_SOURCE(Material.STATIONARY_WATER),
	FLOWING_WATER(Material.WATER),
	WOODEN_PRESSURE_PLATE(Material.WOOD_PLATE),
	STRING(Material.STRING),
	TORCH(Material.TORCH),
	VINE(Material.VINE),
	WALL_SIGN(Material.WALL_SIGN),
	LILYPAD(Material.WATER_LILY),
	COBWEB(Material.WEB),
	DOOR(Material.WOODEN_DOOR),
	IRON_DOOR(Material.IRON_DOOR),
	FLOWER(Material.YELLOW_FLOWER),
	TRAPDOOR(Material.TRAP_DOOR),
	TALL_GRASS(Material.LONG_GRASS);
	
	private final Material material;
	
	SafeBlocks(Material material) { // Private
		this.material = material;
	}
	
	/**
	 * Checks if the speficied material is a safe to teleportt into block.
	 * Searches this enum linearly.
	 * @param b {@link org.bukkit.block.Block Block} in queston
	 * @return {@code true} if the block is safe, {@code false} if it was not
	 * found
	 * @see SafeBlocks
	 */
	static boolean contains(Block b) {
		for(SafeBlocks s : values())
			if(s.material == b.getType()) return true;
		return false;
	}
}
