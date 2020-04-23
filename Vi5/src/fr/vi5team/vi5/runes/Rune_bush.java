package fr.vi5team.vi5.runes;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_bush extends BaseRune{
	private static final Material[] BUSHMATERIALS = {Material.PEONY,Material.TALL_GRASS,Material.LARGE_FERN,Material.LILAC,Material.ROSE_BUSH};
	private double SQUARED_SPOT_RANGE = 1;
	private boolean isInBush=false;
	public Rune_bush(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}

	@Override
	public void cast() {
	}
	private boolean guardNear() {
		for (Player p : wraper.getGame().getGardeList()) {
			if (p.getLocation().distanceSquared(player.getLocation())<=SQUARED_SPOT_RANGE) {
				return true;
			}
		}
		return false;
	}
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.getPlayer()==player) {
			if(Arrays.asList(BUSHMATERIALS).contains(player.getLocation().getBlock().getType())){
				isInBush=true;
			}else {
				isInBush=false;
			}
		}
	}
	@Override
	public void tick() {	
		if (isInBush) {
			if (guardNear() || wraper.isJammed()) {
				if (wraper.isInvisible()) {
					wraper.setInvisible(false);
				}
			}else {
				if (!wraper.isInvisible()) {
					wraper.setInvisible(true);
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
