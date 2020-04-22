package fr.vi5team.vi5.runes;

import org.bukkit.entity.Player;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_speed extends BaseRune {

	public Rune_speed(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}

	@Override
	public void cast() {
	}

	@Override
	public void tick() {
	}

	@Override
	public void gameEnd() {
		player.setWalkSpeed(0.2f);
	}

	@Override
	public void gameStart() {
		player.setWalkSpeed(0.24f);
	}

	@Override
	public void enterZone() {
	}

}
