package fr.vi5team.vi5.runes;

import org.bukkit.entity.Player;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_tough extends BaseRune {

	public Rune_tough(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cast() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameEnd() {
		player.setHealth(10);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterZone() {
		Activate();
		player.setHealth(12);
		// TODO Auto-generated method stub
		
	}

}
