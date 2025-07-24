package net.toydotgame.Thisway.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.toydotgame.Thisway.Configurator;
import net.toydotgame.Thisway.Lang;
import net.toydotgame.Thisway.Option;

/**
 * Class that encapsulates the general block checks for a teleportation to
 * occur. Checks and makes accomidations given a player and their destination
 * location, split into checks for the location foot, eye, and support
 * block/ground position.
 * <dl><dt><b>Created on:</b></dt><dd>2025-07-18</dd></dl>
 * @author toydotgame
 * @see #TeleportTests(Player, Location, Location, Location)
 */
class TeleportTests {
	// Instance fields:
	private Location destination, eye, ground;
	private Player player;
	
	/**
	 * Creates a new test instance for the provided references to a destination
	 * location, with a {@link Player} being provided for messaging.<br>
	 * <br>
	 * It is generally more useful to {@link Location#clone() .clone()} the
	 * destination, apply the required transforms, and store them in their own
	 * objects (which are passed in as references to this constructor's
	 * parameters), rather than the checking methods creating clones and
	 * applying transformations on-demand—as multiple checks will quickly stack
	 * up ever so slightly more memory that really doesn't need to be consumed.
	 * @param player Player to talk to
	 * @param destination Destination block where the player's foot will
	 * be/where they will be teleported to
	 * @param destinationEye {@code destination} +{@link Player#getEyeHeight()}
	 * on Y
	 * @param destinationGround {@code destination} -1 on Y
	 */
	TeleportTests(Player player, Location destination, Location destinationEye, Location destinationGround) {
		this.destination = destination;
		eye = destinationEye;
		ground = destinationGround;
		this.player = player;
	}
	
	/**
	 * Launches the test suite of safety/suitability checks for the destination.
	 * @return {@code true} if the destination is safe and ready to be
	 * teleported to, {@code false} if not
	 * @see #TeleportTests(Player, Location, Location, Location)
	 */
	boolean testAll() {
		// Some of the below safety check booleans are redundantly inverted
		// twice, but that's intended since a "Yes" for the debug print of that
		// check should indicate the "desired" value
		
		// CHECK: Destination is safe
		if(!test(
			!destination.getBlock().getType().isSolid() && !destination.getBlock().isLiquid(),
			"footIsSafeBlock", "tp.error.foot"
		)) return false;		
		if(!test(
			!eye.getBlock().getType().isSolid(),
			"eyeIsNonSolid", "tp.error.eye"
		)) return false;
		
		if(player.isFlying()) {
			TeleportCommand.debugLine("checks.is-flying");
			return true;
		}
		
		// Set glass support block if desired:
		if(Configurator.fetchToggle(Option.SUPPORT_BLOCKS)
			&& !testAndLogBoolean("destGroundIsSolid", !ground.getBlock().isEmpty())) {
			TeleportCommand.debugLine("checks.support-placed");
			ground.getBlock().setType(Material.GLASS);
		}
		// CHECK: If no block was placed, the destination ground must be safe
		else if(!testAndLogBoolean("destHasSupport", ground.getBlock().getType().isSolid())) {
			return error("tp.error.support-failed");
		}
		
		return true;
	}
	
	/**
	 * Runs a particular test (that returns a primitive boolean value), and
	 * associates it with a String name and an error message to print if it
	 * fails. The provision of both strings is for debug logging and error
	 * messaging respectively.<br>
	 * <br>
	 * This method returns such that, if it returns {@code false}, {@link
	 * #testAll()} should return {@code false} too. 
	 * @param condition Simple check to validate. This will technically be
	 * validated at method call time, rather than <i>within</i> this method, but
	 * that's okay since effectively the value of {@code condition} dictates the
	 * return value (by design)
	 * @param name Test name
	 * @param errorMessage Error message to print if the test fails
	 * @return Whatever the value of {@code condition} is
	 */
	private boolean test(boolean condition, String name, String errorTranslationKey) {
		testAndLogBoolean(name, condition);
		
		if(!condition) return error(errorTranslationKey);		
		return true;
	}
	
	/**
	 * Prints an error message to the player and returns false.
	 * @param message Message to print
	 * @return {@code false}
	 */
	private boolean error(String translationKey) {
		player.sendMessage(ChatColor.RED+Lang.create(translationKey));
		return false;
	}
	
	/**
	 * Debug logs the value of some primitive boolean condition (as
	 * pretty-print), returning its value too.<br>
	 * <br>
	 * This method is generally to extend the {@link #test(boolean, String,
	 * String)} method of this class, but can be used statically as well
	 * @param name Name of the test associated with the condition
	 * @param condition Conditional evaluated at method call time
	 * @return {@code condition}
	 * @see #test(boolean, String, String)
	 */
	static boolean testAndLogBoolean(CommandSender sender, String name, boolean condition) {
		AboutCommand.printValue(sender, "debug.checks.check",
			name, AboutCommand.boolToWord(condition));
		return condition;
	}
	
	private boolean testAndLogBoolean(String name, boolean condition) {
		return testAndLogBoolean(player, name, condition);
	}
}
