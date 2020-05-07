package fr.vi5team.vi5.runes;

import org.bukkit.entity.Player;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_magicien extends BaseRune{

	public Rune_magicien(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
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
		
	}

	@Override
	public void gameStart() {
		
	}

	@Override
	public void enterZone() {
		Activate();
		
	}

}
