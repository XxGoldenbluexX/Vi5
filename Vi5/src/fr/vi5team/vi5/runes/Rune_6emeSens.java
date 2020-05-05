package fr.vi5team.vi5.runes;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;
import fr.vi5team.vi5.enums.VoleurStatus;

public class Rune_6emeSens extends BaseRune {

	private static final float SQUARED_RANGE=36;
	
	public Rune_6emeSens(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}

	@Override
	public void cast() {
	}

	@Override
	public void tick() {
		if (wraper.getCurrentStatus()==VoleurStatus.INSIDE && !wraper.isJammed()) {
			Location ourLoc = player.getLocation();
			for (Player p : wraper.getGame().getGardeList()) {
				if (ourLoc.distanceSquared(p.getLocation())<=SQUARED_RANGE) {
					mainref.packetGlowPlayer(player, p);
				}else {
					mainref.packetUnGlowPlayer(player, p);
				}
			}
		}
	}

	@Override
	public void gameEnd() {
	}

	@Override
	public void gameStart() {
	}

	@Override
	public void enterZone() {
		Activate();
	}

}
