package net.toydotgame.Thisway.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import net.toydotgame.Thisway.Lang;
import net.toydotgame.Thisway.commands.utils.MessageUtils;

class Debugger {
	private final CommandSender recipient;
	private final boolean enabled;
	
	Debugger(TeleportCommand caller, CommandSender recipient, boolean enabled) {
		//this.caller = caller; // TODO: needed?
		this.recipient = recipient;
		this.enabled = enabled;
	}
	
	boolean isEnabled() {
		return enabled;
	}
	
	void println(String s, Object... args) {
		if(!enabled) return;
		
		if(s == null || s.equals("")) {
			recipient.sendMessage((String)null);
			return;
		}
		
		// If `s` is a translation key, replace it with the translation;
		if(Lang.containsKey("debug."+s)) s = Lang.create("debug."+s, args);
		
		recipient.sendMessage(ChatColor.GRAY+s);
	}
	void println() {
		println(null);
	}
	
	void heading(String translationKey, Object... args) {
		if(!enabled) return;
		
		MessageUtils.printHeading(recipient, "debug."+translationKey, args);
	}
	
	void hRule(String translationKey) {
		if(!enabled) return;
		
		MessageUtils.sendHorizontalRule(recipient, '-', ChatColor.YELLOW,
			Lang.create("debug."+translationKey), ChatColor.DARK_GREEN, ChatColor.BOLD);
	}
}
