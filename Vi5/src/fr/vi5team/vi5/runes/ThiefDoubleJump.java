package fr.vi5team.vi5.runes;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import fr.vi5team.vi5.BaseRune;
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

public class ThiefDoubleJump extends BaseRune implements Listener{
	boolean isAirborne;
	public ThiefDoubleJump(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, RunesList.DOUBLE_JUMP);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void cast() {
		// TODO Auto-generated method stub
	}
	@EventHandler
	public void onDoubleJump(PlayerToggleFlightEvent event) {
		if(player.getGameMode()==GameMode.SURVIVAL||player.getGameMode()==GameMode.ADVENTURE) {
			event.setCancelled(true);
			if(isAirborne==false) {
				Vector vector = player.getLocation().getDirection().multiply(0.2).setY(1);
				player.setVelocity(vector);
				isAirborne=true;
			}
		}
	}
	@Override
	public void tick() {
		if(isAirborne=true) {
			if(player.isOnGround()) {
				isAirborne=false;
			}
		}	
	}
	@Override
	public void gameEnd() {
		player.setAllowFlight(false);
		isAirborne = false;
		// TODO Auto-generated method stub		
	}
	@Override
	public void gameStart() {
		player.setAllowFlight(true);
		isAirborne = false;
		// TODO Auto-generated method stub		
	}
	@Override
	public void enterZone() {
		// TODO Auto-generated method stub
		
	}

}
