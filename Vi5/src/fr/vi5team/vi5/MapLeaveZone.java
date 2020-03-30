package fr.vi5team.vi5;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class MapLeaveZone implements Listener {
	
	private final Location loc;
	private final Vector size;
	private final Game game;
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Location ploc = event.getTo();
		if (loc.getX()<ploc.getX() && ploc.getX()<loc.getX()+size.getX()) {
			if (loc.getY()<ploc.getY() && ploc.getY()<loc.getY()+size.getY()) {
				if (loc.getZ()<ploc.getZ() && ploc.getZ()<loc.getZ()+size.getZ()) {
					game.playerLeaveMap(event.getPlayer());
				}else {
					return;
				}
			}else {
				return;
			}
		}else {
			return;
		}
	}
	public MapLeaveZone(Game _game,Location _loc, Vector _size) {
		loc=_loc;
		size=_size;
		game=_game;
	}

	public Location getLoc() {
		return loc;
	}
	public Vector getSize() {
		return size;
	}
}