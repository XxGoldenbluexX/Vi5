package fr.vi5team.vi5.runes;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_inviSneak extends BaseRune{

	private static final double SQUARED_SPOT_RANGE=9;
	private boolean isSneaking=false;
	
	public Rune_inviSneak(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}
	
	private boolean guardNear() {
		for (Player p : wraper.getGame().getGardeList()) {
			if (p.getLocation().distanceSquared(player.getLocation())<=SQUARED_SPOT_RANGE) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void cast() {
	}

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {
		if (event.getPlayer().equals(player)) {
			isSneaking=event.isSneaking();
			if (!isSneaking) {
				wraper.setInvisible(false);
			}
		}
	}
	@Override
	public void tick() {
		if (isSneaking) {
			if (guardNear() || wraper.isJammed()) {
				player.getWorld().playSound(player.getLocation(),Sound.BLOCK_LAVA_EXTINGUISH, SoundCategory.MASTER, 0.1f, 2f);
				wraper.setInvisible(false);
			}else {
				wraper.setInvisible(true);
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
