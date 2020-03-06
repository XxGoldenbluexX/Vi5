package fr.vi5team.vi5;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Game implements Listener {
	
	private Vi5Main mainref;
	private String name="Vi5Game";
	
	HashMap<Player,PlayerWrapper> playersInGame = new HashMap<Player,PlayerWrapper>();//Liste des joueurs présents dans la partie et de leur wrapper
	
	public Game(Vi5Main main) {
		mainref=main;
	}
	
	public boolean addPlayer(Player player) {
		if (playersInGame.containsKey(player)) {
			return false;
		}else{
			if (mainref.isPlayerIngame(player)) {
				return false;
			}else {
				PlayerWrapper wrap = new PlayerWrapper();
				playersInGame.put(player, wrap);
				messagePlayersInGame(ChatColor.GOLD+player.getName()+ChatColor.DARK_GREEN+" joined this vi5");
				return true;
			}
		}
	}
	
	public boolean removePlayer(Player player) {
		if (playersInGame.containsKey(player)) {
			playersInGame.remove(player);
			messagePlayersInGame(ChatColor.GOLD+player.getName()+ChatColor.RED+" leaved this game");
			return false;
		}else {
			return false;
		}
	}
	
	public boolean hasPlayer(Player player) {
		return playersInGame.containsKey(player);
	}
	
	public void messagePlayersInGame(String message) {
		for (Player p : playersInGame.keySet()) {
			p.sendMessage(ChatColor.AQUA+"["+name+"]"+message);
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		removePlayer(player);
		return;
	}
}
