package fr.vi5team.vi5;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MapLeavePlate implements Listener {
	
	private Location loc;
	
	public void onPlayerMove(PlayerMoveEvent event) {
		//check for player leaving the area
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
}
