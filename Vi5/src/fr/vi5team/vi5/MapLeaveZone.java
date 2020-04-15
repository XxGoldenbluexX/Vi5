package fr.vi5team.vi5;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import fr.vi5team.vi5.enums.Vi5Team;
import fr.vi5team.vi5.enums.VoleurStatus;

public class MapLeaveZone implements Listener {
	
	private final Location loc;
	private final Vector size;
	private final Game game;
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Location ploc = event.getTo();
		Player player = event.getPlayer();
		if (game.hasPlayer(player)) {
			if (game.getPlayerWrapper(player).getCurrentStatus()==VoleurStatus.INSIDE) {
				if (loc.getX()<=ploc.getX() && ploc.getX()<=loc.getX()+size.getX()) {
					if (loc.getY()<=ploc.getY() && ploc.getY()<=loc.getY()+size.getY()) {
						if (loc.getZ()<=ploc.getZ() && ploc.getZ()<=loc.getZ()+size.getZ()) {
							if (game.getPlayerWrapper(player).getTeam()==Vi5Team.VOLEUR) {
								game.playerLeaveMap(player);
								return;
							}else if (game.getPlayerWrapper(player).getTeam()==Vi5Team.GARDE) {
								event.setCancelled(true);
								return;
							}
						}else {
							return;
						}
					}else {
						return;
					}
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
