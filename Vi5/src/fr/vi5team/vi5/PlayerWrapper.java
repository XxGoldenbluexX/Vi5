package fr.vi5team.vi5;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.vi5team.vi5.enums.Vi5Team;
import fr.vi5team.vi5.enums.VoleurStatus;

public class PlayerWrapper implements Listener {
	
	private Vi5Team team=Vi5Team.SPECTATEUR;
	private BaseRune primaire;
	private BaseRune secondaire;
	private BaseRune tertiaire;
	private short nbItemStealed=0;
	private VoleurStatus currentStatus=VoleurStatus.OUTSIDE;
	private boolean enterStealCooldown=true;
	
	private final Game game;//référence a la game ou le joueur appartient, null si il n'en a pas
	boolean ready=false;
	private final Player player;
	
	public PlayerWrapper(Game game, Player player) {
		this.player=player;
		this.game=game;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void gameStart() {
		primaire.gameStart();
		secondaire.gameStart();
		tertiaire.gameStart();
	}
	
	public void gameEnd() {
		primaire.gameEnd();
		secondaire.gameEnd();
		tertiaire.gameEnd();
	}
	
	public void tickRunes() {
		primaire.tick();
		secondaire.tick();
		tertiaire.tick();
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

	public VoleurStatus getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(VoleurStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isEnterStealCooldown() {
		return enterStealCooldown;
	}

	public void setEnterStealCooldown(boolean enterStealCooldown) {
		this.enterStealCooldown = enterStealCooldown;
	}
}
