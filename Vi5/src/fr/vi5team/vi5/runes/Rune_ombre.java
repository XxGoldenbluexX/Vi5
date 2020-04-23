package fr.vi5team.vi5.runes;

import org.bukkit.entity.Player;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class Rune_ombre extends BaseRune {
	
	private enum OmbreStatu {
		READY,
		POSED,
		WASTED
	}
	
	private OmbreStatu statu;

	public Rune_ombre(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
	}

	@Override
	public void cast() {
		if (statu==OmbreStatu.READY) {
			statu=OmbreStatu.POSED;
		}
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
	}
}
