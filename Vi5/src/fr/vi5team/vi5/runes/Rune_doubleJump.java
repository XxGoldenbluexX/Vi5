package fr.vi5team.vi5.runes;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_doubleJump extends BaseRune {
	
	private boolean canDoubleJump=false;
	
	public Rune_doubleJump(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}

	@EventHandler
	public void onToogleFly(PlayerToggleFlightEvent event) {
		if (event.getPlayer().equals(player) && canDoubleJump) {
			event.setCancelled(true);
			player.setVelocity(player.getVelocity().setY(0.5));
			player.playSound(player.getLocation(),Sound.ITEM_FIRECHARGE_USE, SoundCategory.AMBIENT, 0.3f, 1.5f);
			player.playSound(player.getLocation(),Sound.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1f, 0.1f);
			player.setAllowFlight(false);
		}
	}
	
	@Override
	public void cast() {
	}

	@Override
	public void tick() {
		if (player.isOnGround() && canDoubleJump) {
			player.setAllowFlight(true);
		}
	}

	@Override
	public void gameEnd() {
		player.setAllowFlight(false);
	}

	@Override
	public void gameStart() {
	}

	@Override
	public void enterZone() {
		Activate();
		canDoubleJump=true;
	}

}
