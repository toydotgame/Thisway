package net.toydotgame.Thisway;

/**
 * Dead simple enum to wrap constant values and their associated YAML key names
 * for writing to the config.yml.
 * <dl><dt><b>Created on:</b></dt><dd>2025-07-15</dd></dl>
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
	/**
	 * The default value of this option if not set by the user.
	 */
	public final boolean defaultValue;
	
	Option(String string, boolean defaultValue) { // Private
		yamlName = string;
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Gets an {@link Option} by its {@link #yamlName} value.
	 * @param name Name of the option in YAML
	 * @return {@link Option} enum value if found, {@code null} if not
	 */
	public static Option get(String name) {
		for(Option o : Option.values())
			if(o.yamlName.equals(name)) return o;
		return null;
	}
}
