package net.toydotgame.Thisway.commands.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import net.toydotgame.Thisway.Lang;

/**
 * Wrapper class adapted from the method on the SpigotMC forums.
 * <dl>
 * <dt><b>Created on:</b></dt>
 * <dd>2015-10-05<br>
 * </dl>
 * 
 * @see <a href=
 *      "https://www.spigotmc.org/threads/free-code-sending-perfectly-centered-chat-message.95872/"
 *      target="_top">(Free Code) Sending Perfectly Centered Chat Message</a>
 * @author SirSpoodles
 * @author ConnorLinfoot
 * @author toydotgame
 */
public class MessageUtils {
	private MessageUtils() {} // Static class
	
	private static final int KERNING_GAP = 1; // Minecraft font px between characters
	
	/**
	 * Sends a horizontal rule to chat with an optional heading (+ formatting)
	 * to be placed in the centre if so desired.
	 * @param sender {@link CommandSender} or (ideally) {@link
	 * org.bukkit.entity.Player Player} to send the line to
	 * @param ruleChar {@code char} literal to use as the rule line
	 * @param ruleColor {@link ChatColor} instance to format the line with. Can
	 * be {@code null}
	 * @param heading Heading text (can be {@code null} or empty string)
	 * @param headingFormatting Optional {@code ChatColor[]} of format strings
	 * to apply to the title
	 */
	public static void sendHorizontalRule(
		CommandSender sender, char ruleChar, ChatColor ruleColor, String heading, ChatColor... headingFormatting) {
		// Allow passing in null for default formatting:
		if(ruleColor == null) ruleColor = ChatColor.RESET;
		
		// If there's actually heading text, format it:
		if(heading != null && !heading.equals("")) {
			StringBuilder formatString = new StringBuilder();
			for(ChatColor c : headingFormatting)
				formatString.append(c);
			
			heading = "[ "+formatString.toString()+heading+ChatColor.RESET+ruleColor+" ]";
		} // If there's no heading text, pass it straight through
		
		sender.sendMessage(ruleColor+createCentredMessage(heading, ruleChar));
	}
	
	/**
	 * Creates a message with the parameter {@code message} centred within it.
	 * This message originated as the original SpigotMC {@code
	 * sendCentredMessage(...)} snippet, but has been modified to just do the
	 * same calculations and return a {@link java.lang.String String} instead.
	 * <br><br>
	 * This method takes the input string, parses it for formatting, and
	 * calculates it total width in pixels, then using the remaining space to
	 * calculate and pre- and post-fix correct "padding" to thus yield a centred
	 * line of text {@code message} with padding determined by {@code
	 * paddingChar}.
	 * @param message Message to center. Can be {@code null} or empty string
	 * @param paddingChar Character to use for padding, i.e. {@code ' '}
	 * @return {@code message} centred between two equal-width {@code
	 * paddingChar} strings, or just a single full line of {@code paddingChar}
	 * if no {@code message} is specified
	 * @see #createPadding(int, char)
	 */
	private static String createCentredMessage(String message, char paddingChar) {
		if(message == null) message = ""; // Only the padding char will be printed
		//message = ChatColor.translateAlternateColorCodes('&', message);
		
		int messageWidthPx = 0;       // Minecraft font px
		boolean previousCode = false; // If the last char only was ChatColor.COLOR_CHAR
		boolean formatBold = false;   // If the current format code is the bold one
		
		for(char c : message.toCharArray()) {
			if(c == ChatColor.COLOR_CHAR) {
				previousCode = true;
			} else if(previousCode) {
				// Note how if the last code was ChatColor.COLOR_CHAR, this one
				// will be its corresponding format char, so even if it's not
				// bold, we still don't count it into messageWidthPx
				previousCode = false;
				formatBold = c == 'l' || c == 'L';
			} else {
				DefaultFontInfo d = DefaultFontInfo.getDefaultFontInfo(c);
				messageWidthPx += (formatBold ? d.getBoldWidth():d.getWidth())+KERNING_GAP;
			}
		}
		
		String padding = createPadding(messageWidthPx, paddingChar);
		return padding+message+padding;
	}
	
	/**
	 * Padding creator that takes in a message width, and yields a string of
	 * however many instances of {@code c} are needed to pad a message of that
	 * width to the centre.<br>
	 * <br>
	 * <b>Note:</b> As a result, this will just yield the padding string,
	 * excluding the message itself. To centre a message (as in
	 * padding–message–padding, see the {@link
	 * #createCentredMessage(String, char)} method).
	 * @param width Number of Minecraft font pixels wide the message is
	 * @param c Padding {@code char} to use
	 * @return A string of {@code c}s
	 * @see #createCentredMessage(String, char)
	 */
	private static String createPadding(int width, char c) {
		// Minecraft chat width is 320 px by default, half of which (the center)
		// is 160. Minecraft then takes another 3 px of padding each side (6
		// px), then we take a 1 px more each side (2 px) to allow for bold text
		final int CENTER_PX = 152;
		final int TO_BE_COMPENSATED = CENTER_PX-width/2;
		final int PADDING_CHAR_WIDTH = DefaultFontInfo.getDefaultFontInfo(c)
			.getWidth()+KERNING_GAP;
		int compensated = 0;
		
		StringBuilder sb = new StringBuilder();
		while(compensated < TO_BE_COMPENSATED) {
			sb.append(c);
			compensated += PADDING_CHAR_WIDTH;
		}
		
		return sb.toString();
	}
	
	public static void printHeading(CommandSender sender, String key, Object... args) {
		// Print empty line before heading. Using a null string causes less
		// visual issues than "\n" and uses less memory than ""
		sender.sendMessage((String)null);
		
		String heading = ""+ChatColor.RESET+ChatColor.BOLD
			+Lang.create(key, args);
		int i = heading.indexOf(':');
		if(i >= 0) { // If the heading contains a key-value pair, format it as such:
			heading = heading.substring(0, ++i)
				+ChatColor.RESET+ChatColor.GRAY
				+heading.substring(i);
		}
		
		sender.sendMessage(heading);
	}
	
	public static void printKeyValue(CommandSender sender, String translationKey, Object... args) {
		String line = "", linePrefix = ChatColor.GRAY.toString();
		if(Lang.isList(translationKey))
			for(String s : Lang.fetchList(translationKey))
				sender.sendMessage(linePrefix+s);
		else {
			for(int i = 0; i < args.length; i++) // Fix formatting resets:
				args[i] += ChatColor.GRAY.toString();
			line = linePrefix+Lang.create(translationKey, args);
			sender.sendMessage(line);
		}
	}
	
	/**
	 * Converts an input boolean value to a coloured text string "Yes" or "No".
	 * @param b Input formula or expression, or boolean constant to evaluate
	 * @return A green "Yes" for printing to chat if {@code b == true}, or a red
	 * "No" if {@code b == false}
	 */
	public static String boolToWord(boolean b) {
		return (b ? ChatColor.GREEN+Lang.create("boolean.true")
			:ChatColor.RED+Lang.create("boolean.false"))+ChatColor.RESET;
	}
}
