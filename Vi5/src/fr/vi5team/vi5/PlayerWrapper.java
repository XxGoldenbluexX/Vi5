package fr.vi5team.vi5;

import org.bukkit.event.Listener;

import fr.vi5team.vi5.enums.Vi5Team;

public class PlayerWrapper implements Listener {
	
	private Vi5Team team=Vi5Team.SPECTATEUR;
	private BaseRune primaire;
	private BaseRune secondaire;
	private BaseRune tertiaire;
	private short nbItemStealed=0;
	
	Game game = null;//référence a la game ou le joueur appartient, null si il n'en a pas
	boolean ready=false;
	
	
	public Game getGame() {
		return game;
	}
	
	public boolean is_ingame() {
		//permet de savoir si le joueur a rejoin une partie (/!\ NE DIT PAS SI LA PARTIE EST EN COURS)
		return !(game.equals(null));
	}
	
	public boolean is_ready() {
		return ready;
	}
	
	public void setReady(boolean r) {
		ready=r;
	}

	public Vi5Team getTeam() {
		return team;
	}

	public void setTeam(Vi5Team team) {
		this.team = team;
	}

	public BaseRune getRunePrimaire() {
		return primaire;
	}

	public void setRunePrimaire(BaseRune primaire) {
		this.primaire = primaire;
	}

	public BaseRune getRuneSecondaire() {
		return secondaire;
	}

	public void setRuneSecondaire(BaseRune secondaire) {
		this.secondaire = secondaire;
	}

	public BaseRune getRuneTertiaire() {
		return tertiaire;
	}

	public void setRuneTertiaire(BaseRune tertiaire) {
		this.tertiaire = tertiaire;
	}

	public short getNbItemStealed() {
		return nbItemStealed;
	}

	public void setNbItemStealed(short nbItemStealed) {
		this.nbItemStealed = nbItemStealed;
	}
	
	public void addItemStealed() {
		nbItemStealed++;
	}
}
