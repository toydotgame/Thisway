package io.github.toydotgame.Thisway;

/* 
 * CREATED: 2020-12-26
 * AUTHOR: toydotgame
 * A class dedicated to storing public variables. It's easier than dealing with the mess it creates in the main command class.
 */

public class DataStorage {
	public static String localVersion;

	public static boolean debug;
	public static String facing;
	
	public static int xModDistance = 0;
	public static int zModDistance = 0;
	
	static String[] safeBlocks = {
			"AIR",
			"BROWN_MUSHROOM",
			"CARPET",
			"COCOA",
			"CROPS",
			"DEAD_BUSH",
			"DETECTOR_RAIL",
			"DIODE_BLOCK_OFF",
			"DIODE_BLOCK_ON",
			"FIRE",
			"FLOWER_POT",
			"LADDER",
			"LAVA",
			"LEAVES",
			"LEVER",
			"MELON_STEM",
			"PAINTING",
			"PORTAL",
			"POWERED_RAIL",
			"PUMPKIN_STEM",
			"RAILS",
			"RED_MUSHROOM",
			"RED_ROSE",
			"REDSTONE_COMPARATOR_OFF",
			"REDSTONE_COMPARATOR_ON",
			"REDSTONE_TORCH_OFF",
			"REDSTONE_TORCH_ON",
			"REDSTONE_WIRE",
			"SAPLING",
			"SIGN_POST",
			"SKULL",
			"SNOW",
			"STATIONARY_LAVA",
			"STATIONARY_WATER",
			"WATER",
			"WOOD_PLATE",
			"STRING",
			"TORCH",
			"VINE",
			"WALL_SIGN",
			"WATER_LILY",
			"WEB",
			"WOODEN_DOOR",
			"IRON_DOOR",
			"YELLOW_FLOWER",
			"TRAP_DOOR",
			"LONG_GRASS"
	};
}
