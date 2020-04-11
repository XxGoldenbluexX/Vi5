package fr.vi5team.vi5;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
	private ItemStack readyItem;
	private ItemStack runeSelectionItem;
	private ItemStack TeamSelectionItem;
	
	public PlayerWrapper(Game game, Player player) {
		this.player=player;
		this.game=game;
		ItemStack item = new ItemStack(Material.EMERALD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA+"Runes");
		ArrayList<String> lore= new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE+"Drop this to select your runes");
		meta.setLore(lore);
		item.setItemMeta(meta);
		runeSelectionItem=item;
		player.getInventory().setItem(1, item);
		setReady(false);
		setTeam(Vi5Team.GARDE);
	}
	
	public Game getGame() {
		return game;
	}
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		ItemStack itm = event.getItemDrop().getItemStack();
		if (itm.equals(readyItem)) {
			if (ready) {
				setReady(false);
			}else {
				setReady(true);
			}
			event.setCancelled(true);
		}else if (itm.equals(runeSelectionItem)) {
			//OPEN RUNE SELECTION
			event.setCancelled(true);
		}else if (itm.equals(TeamSelectionItem)) {
			//OPEN TEAM SELECTION
			event.setCancelled(true);
		}
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
		//permet de savoir si le joueur a rejoint une partie (/!\ NE DIT PAS SI LA PARTIE EST EN COURS)
		return !(game.equals(null));
	}
	
	public boolean is_ready() {
		return ready;
	}
	
	public void setReady(boolean r) {
		ready=r;
		if (ready) {
			ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_GREEN+"Ready");
			ArrayList<String> lore= new ArrayList<String>();
			lore.add(ChatColor.LIGHT_PURPLE+"Drop this to set yourself not ready");
			meta.setLore(lore);
			item.setItemMeta(meta);
			player.getInventory().setItem(0, item);
		}else {
			ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED+"Not Ready");
			ArrayList<String> lore= new ArrayList<String>();
			lore.add(ChatColor.LIGHT_PURPLE+"Drop this to set yourself ready");
			meta.setLore(lore);
			item.setItemMeta(meta);
			player.getInventory().setItem(0, item);
		}
	}

	public Vi5Team getTeam() {
		return team;
	}

	public void setTeam(Vi5Team team) {
		this.team = team;
		ItemStack item = new ItemStack(Material.WHITE_BANNER);
		switch (team) {
		case GARDE:
			item = new ItemStack(Material.CYAN_BANNER);
			break;
		case VOLEUR:
			item = new ItemStack(Material.RED_BANNER);
			break;
		case SPECTATEUR:
			item = new ItemStack(Material.LIME_BANNER);
			break;
		default:
			break;
		}
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED+"Team");
		ArrayList<String> lore= new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE+"Drop this to select your team");
		meta.setLore(lore);
		item.setItemMeta(meta);
		TeamSelectionItem=item;
		player.getInventory().setItem(2, item);
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
	
	public void resetItemStealed() {
		nbItemStealed=0;
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
