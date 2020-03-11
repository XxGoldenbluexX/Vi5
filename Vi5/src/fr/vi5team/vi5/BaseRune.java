package fr.vi5team.vi5;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class BaseRune implements Listener {
	
	private final Player player;
	private final PlayerWrapper wraper;
	private final Vi5Main mainref;
	
	public BaseRune(Vi5Main _mainref,PlayerWrapper _wraper, Player _player) {
		player=_player;
		wraper=_wraper;
		mainref=_mainref;
	}

	public Player getPlayer() {
		return player;
	}

	public PlayerWrapper getWraper() {
		return wraper;
	}

	public Vi5Main getMainref() {
		return mainref;
	}
}