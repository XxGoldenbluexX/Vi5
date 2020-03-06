package fr.vi5team.vi5;

import org.bukkit.event.Listener;

public class PlayerWrapper implements Listener {
	
	
	Game game = null;//référence a la game ou le joueur appartient, null si il n'en a pas
	
	
	public Game getGame() {
		return game;
	}
	
	public boolean is_ingame() {
		//permet de savoir si le joueur a rejoin une partie (/!\ NE DIT PAS SI LA PARTIE EST EN COURS)
		return !(game.equals(null));
	}
}
