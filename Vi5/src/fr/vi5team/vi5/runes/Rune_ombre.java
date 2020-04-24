package fr.vi5team.vi5.runes;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_ombre extends BaseRune {
	
	private enum OmbreStatus {
		READY,
		POSED,
		WASTED
	}
	
	private OmbreStatus status;
	private ArmorStand ombreRef;

	public Rune_ombre(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (wraper.getGame().getGardeList().contains(p)) {
			
		}
	}

	@Override
	public void cast() {
		switch (status) {
		case POSED:
			break;
		case READY:
			
			break;
		case WASTED:
			if (ombreRef.isOnGround()) {};
			player.sendMessage("Your shadow has disapeared");
			return;
		default:
			return;
		}
		setCooldown(2);
	}

	@Override
	public void tick() {
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
