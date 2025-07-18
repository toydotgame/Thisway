package net.toydotgame.Thisway;

/**
 * Dead simple enum to wrap constant values and their associated YAML key names
 * for writing to the config.yml.
 * <dt><b>Created on:</b></dt><dd>2025-07-15</dd>
 * @author toydotgame
 */
public enum Option {
	VERSION_ALERTS("version-alerts", true),
	BROADCAST_VERSION_ALERTS("in-game-version-alerts-to-ops", true),
	LOG_TELEPORTS("log-teleports-in-console", false),
	SUPPORT_BLOCKS("place-support-blocks", true);
	
	/**
	 * The name of this option in YAML.
	 */
	final String yamlName;
	final boolean defaultValue;
	
	Option(String string, boolean defaultValue) { // Private
		yamlName = string;
		this.defaultValue = defaultValue;
	} 
}
