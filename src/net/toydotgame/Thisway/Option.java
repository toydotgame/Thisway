package net.toydotgame.Thisway;

/**
 * Dead simple enum to wrap constant values and their associated YAML key names
 * for writing to the config.yml.
 */
enum Option {
	VERSION_ALERTS("version-alerts"),
	BROADCAST_VERSION_ALERTS("broadcast-version-alerts-to-ops"),
	LOG_TELEPORTS("log-teleports-in-console");
	
	final String yamlName;
	
	Option(String string) { // Private
		this.yamlName = string;
	} 
}
