package fr.vi5team.vi5.runes;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_technicien extends BaseRune{

	public Rune_technicien(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}

	@Override
	public void cast() {
		
	}

	@Override
	public void tick() {
		for(Player garde : wraper.getGame().getGardeList()) {
			PlayerWrapper wrap = mainref.getPlayerWrapper(garde);
			if(wrap.getRuneSecondaire() instanceof Rune_piege) {
				ArrayList<Location> piegesLoc = ((Rune_piege)wrap.getRuneSecondaire()).getPiegePos();
				for(Location piegeLoc : piegesLoc) {
					player.spawnParticle(Particle.PORTAL, piegeLoc, 1);
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
