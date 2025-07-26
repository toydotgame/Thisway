package net.toydotgame.Thisway.commands;

import org.bukkit.Location;

class Destination {
	private final Location location, eye, ground;
	
	Destination(Location start, double eyeHeight, DirectionVector direction, int distance) {
		this.location = start.clone()                  // Target location (=start+distance)
			.add(direction.createVector(distance));
		this.eye = this.location.clone()                       // Target location+eye height
			.add(0, eyeHeight, 0);
		this.ground = this.location.clone().subtract(0, 1, 0); // Target location-1 y (standing on block)
	}
	
	Location get() {
		return location;
	}
	
	Location getEye() {
		return eye;
	}
	
	Location getGround() {
		return ground;
	}
}
